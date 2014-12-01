package esi.uclm.com.multisensorsui;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.IBinder;
import android.widget.Toast;

import java.text.DecimalFormat;

import static android.util.FloatMath.cos;
import static android.util.FloatMath.sin;
import static android.util.FloatMath.sqrt;

public class MyFirstService extends Service implements FusedGyroscopeSensorListener, SensorEventListener {

    private AudioManager mAudioManager;

    public static final float EPSILON = 0.000000001f;

    private static final float NS2S = 1.0f / 1000000000.0f;
    private static final int MEAN_FILTER_WINDOW = 10;
    private static final int MIN_SAMPLE_COUNT = 30;

    private boolean hasInitialOrientation = false;
    private boolean stateInitializedCalibrated = false;

    private boolean useFusedEstimation = false;

    private DecimalFormat df;

    // Calibrated maths.
    private float[] currentRotationMatrix;
    private float[] deltaRotationMatrix;
    private float[] deltaRotationVector;
    private float[] gyroscopeOrientation;

    // accelerometer and magnetometer based rotation matrix
    private float[] initialRotationMatrix;

    // accelerometer vector
    private float[] acceleration;

    // magnetic field vector
    private float[] magnetic;

    private FusedGyroscopeSensor fusedGyroscopeSensor;

    private int accelerationSampleCount = 0;
    private int magneticSampleCount = 0;

    private long timestampOld = 0;

    private MeanFilter accelerationFilter;
    private MeanFilter magneticFilter;

    /* We need the SensorManager to register for Sensor Events */
    private SensorManager mSensorManager;

    private String calibrationX;
    private String calibrationY;
    private String calibrationZ;

    private Cursor cAcciones;

    /* SHAKE DETECTOR VARIABLES*/
    private long last_update = 0, last_movement = 0;
    private float prevX = 0, prevY = 0, prevZ = 0;
    private float curX = 0, curY = 0, curZ = 0;
    private int mCount = 0;


    public void onCreate() {
//        Toast.makeText(this, "Servicio creado",
//                Toast.LENGTH_SHORT).show();

        initSensors();
        initMaths();
        initFilters();

        Acciones.inicializarBD(this);

        mAudioManager = (AudioManager)getSystemService(AUDIO_SERVICE);

    }

    @Override
    public int onStartCommand(Intent intenc, int flags, int idArranque) {
//        Toast.makeText(this,"Servicio arrancado "+ idArranque,
//                Toast.LENGTH_SHORT).show();
        registerListeners();

        initUI();
        initFilters();

        return START_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            onAccelerationSensorChanged(event.values, event.timestamp);
        }

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            onMagneticSensorChanged(event.values, event.timestamp);
        }

        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            onGyroscopeSensorChanged(event.values, event.timestamp);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onAngularVelocitySensorChanged(float[] angularVelocity, long timeStamp) {
        calibrationX = (df.format(Math.toDegrees(angularVelocity[0])));
        calibrationY = (df.format(Math.toDegrees(angularVelocity[1])));
        calibrationZ = (df.format(Math.toDegrees(angularVelocity[2])));

    }

    public void onAccelerationSensorChanged(float[] acceleration, long timeStamp) {
        /* Copy of the values of the sensor inputs */
        System.arraycopy(acceleration, 0, this.acceleration, 0,
                acceleration.length);

        /* Apply mean Filter to smooth the sensor inputs */
        this.acceleration = accelerationFilter.filterFloat(this.acceleration);

        /* Count the number of counts */
        accelerationSampleCount++;
/*
            long current_time = timeStamp;

            curX = acceleration[0];
            curY = acceleration[1];
            curZ = acceleration[2];

            if (prevX == 0) {
                last_update = current_time;
                last_movement = current_time;
                prevX = curX;
            }

            long time_difference = current_time - last_update;
            if (time_difference > 0) {
                float movement = Math.abs(curX - prevX / time_difference);
                int limit = 500000000;
                float min_movement = 19;
                if (movement > min_movement) {
                    if (current_time - last_movement >= limit) {
                        Toast.makeText(getApplicationContext(), "Hay movimiento de " + movement, Toast.LENGTH_SHORT).show();
                        mCount++;
                    }
                    last_movement = current_time;
                }

                if(current_time - last_movement > 700000000){
                    comprobarAccionesShake(mCount);
                    mCount = 0;
                }

                prevX = curX;
                prevY = curY;
                prevZ = curZ;

                last_update = current_time;
        }*/

        /**
         * Only determine the initial orientation after the acceleration sensor
         * and magnetic sensor have had enough time to be smoothed by the mean
         * filters. Also, only do this if the orientation hasn't already been
         * determined since we only need it once.
         **/
        if (accelerationSampleCount > MIN_SAMPLE_COUNT
                && magneticSampleCount > MIN_SAMPLE_COUNT
                && !hasInitialOrientation)
        {
            calculateOrientation();
        }
    }
/*
    private void comprobarAccionesShake(int mCount) {

        switch (mCount){
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            default:
                break;
        }
    }*/

    public void onMagneticSensorChanged(float[] magnetic, long timeStamp) {
        /* Copy of the values of the sensor inputs */
        System.arraycopy(magnetic, 0, this.magnetic, 0, magnetic.length);

        /* Apply mean Filter to smooth the sensor inputs */
        this.magnetic = magneticFilter.filterFloat(this.magnetic);

        /* Count the number of counts */
        magneticSampleCount++;
    }

    /* Calculate orientation over time*/
    public void onGyroscopeSensorChanged(float[] gyroscope, long timestamp) {

        /* Don't start until first orientation has been calculated */
        if (!hasInitialOrientation) {
            return;
        }

        /* Initialization of the gyroscope based rotation matrix */
        if (!stateInitializedCalibrated) {
            currentRotationMatrix = matrixMultiplication(
                    currentRotationMatrix, initialRotationMatrix);

            stateInitializedCalibrated = true;
        }
        // This timestep's delta rotation to be multiplied by the current rotation
        // after computing it from the gyro sample data.
        if (timestampOld != 0 && stateInitializedCalibrated) {

            final float dT = (timestamp - timestampOld) * NS2S;

            // Axis of the rotation sample, not normalized yet.
            float axisX = gyroscope[0];
            float axisY = gyroscope[1];
            float axisZ = gyroscope[2];

            /* Angular speed */
            float omegaMagnitude = sqrt(axisX * axisX + axisY * axisY + axisZ * axisZ);


            /**
             * Normalize rotator vector (in case it is big enough to get the axis
             *
             * EPSILON represents maximum allowable margin of error
             *
             * */
            if (omegaMagnitude > EPSILON) {
                axisX /= omegaMagnitude;
                axisY /= omegaMagnitude;
                axisZ /= omegaMagnitude;
            }

            // Integrate around this axis with the angular speed by the timestep
            // in order to get a delta rotation from this sample over the timestep
            // We will convert this axis-angle representation of the delta rotation
            // into a quaternion before turning it into the rotation matrix.
            float thetaOverTwo = omegaMagnitude * dT / 2.0f;

            float sinThetaOverTwo = sin(thetaOverTwo);
            float cosThetaOverTwo = cos(thetaOverTwo);

            deltaRotationVector[0] = sinThetaOverTwo * axisX;
            deltaRotationVector[1] = sinThetaOverTwo * axisY;
            deltaRotationVector[2] = sinThetaOverTwo * axisZ;
            deltaRotationVector[3] = cosThetaOverTwo;

            SensorManager.getRotationMatrixFromVector(
                    deltaRotationMatrix,
                    deltaRotationVector);

            currentRotationMatrix = matrixMultiplication(
                    currentRotationMatrix,
                    deltaRotationMatrix);

            SensorManager.getOrientation(currentRotationMatrix,
                    gyroscopeOrientation);
        }
        timestampOld = timestamp;

        // User code should concatenate the delta rotation we computed with the current rotation
        // in order to get the updated rotation.
        // rotationCurrent = rotationCurrent * deltaRotationMatrix;

        calibrationX = df.format(Math.toDegrees(gyroscopeOrientation[0]));
        calibrationY = df.format(Math.toDegrees(gyroscopeOrientation[1]));
        calibrationZ = df.format(Math.toDegrees(gyroscopeOrientation[2]));

        comprobarAccionesOrientation();

    }

    /* Mathematical formula for calculating the rotation */
    private float[] matrixMultiplication(float[] a, float[] b) {
        float[] result = new float[9];

        result[0] = a[0] * b[0] + a[1] * b[3] + a[2] * b[6];
        result[1] = a[0] * b[1] + a[1] * b[4] + a[2] * b[7];
        result[2] = a[0] * b[2] + a[1] * b[5] + a[2] * b[8];

        result[3] = a[3] * b[0] + a[4] * b[3] + a[5] * b[6];
        result[4] = a[3] * b[1] + a[4] * b[4] + a[5] * b[7];
        result[5] = a[3] * b[2] + a[4] * b[5] + a[5] * b[8];

        result[6] = a[6] * b[0] + a[7] * b[3] + a[8] * b[6];
        result[7] = a[6] * b[1] + a[7] * b[4] + a[8] * b[7];
        result[8] = a[6] * b[2] + a[7] * b[5] + a[8] * b[8];

        return result;
    }

    /* Initializing the variables for the data input */
    private void initMaths() {

        acceleration = new float[3];
        magnetic = new float[3];

        initialRotationMatrix = new float[9];

        deltaRotationVector = new float[4];
        deltaRotationMatrix = new float[9];
        currentRotationMatrix = new float[9];
        gyroscopeOrientation = new float[3];

        // Initialize the current rotation matrix as an identity matrix...
        currentRotationMatrix[0] = 1.0f;
        currentRotationMatrix[4] = 1.0f;
        currentRotationMatrix[8] = 1.0f;
    }

    /* Initializing interface elements */
    private void initUI() {
        /* Decimal formatter for TextViews */
        df = new DecimalFormat("#");

    }

    /* Initializing data filters to smooth the sensors input */
    private void initFilters() {
        accelerationFilter = new MeanFilter();
        accelerationFilter.setWindowSize(MEAN_FILTER_WINDOW);

        magneticFilter = new MeanFilter();
        magneticFilter.setWindowSize(MEAN_FILTER_WINDOW);
    }

    /* Initializing sensors managers */
    private void initSensors() {
        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        fusedGyroscopeSensor = new FusedGyroscopeSensor();
    }

    /* Unregister all listeners & clening data */
    private void unregisterListeners(){

        mSensorManager.unregisterListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));

        mSensorManager.unregisterListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD));

        if (!useFusedEstimation)
        {
            mSensorManager.unregisterListener(this,
                    mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE));
        }

        if (useFusedEstimation)
        {
            mSensorManager.unregisterListener(fusedGyroscopeSensor,
                    mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY));

            mSensorManager.unregisterListener(fusedGyroscopeSensor,
                    mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));

            mSensorManager.unregisterListener(fusedGyroscopeSensor,
                    mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD));

            mSensorManager.unregisterListener(fusedGyroscopeSensor,
                    mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE));

            fusedGyroscopeSensor.removeObserver(this);
        }

        initMaths();

        accelerationSampleCount = 0;
        magneticSampleCount = 0;

        hasInitialOrientation = false;
        stateInitializedCalibrated = false;
    }

    /* Registering the sensors listeners & start monitoring data*/
    private void registerListeners() {

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_FASTEST);

        /* If we don't use the FusedSensor*/
        if (!useFusedEstimation)
        {
            mSensorManager.registerListener(this,
                    mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                    SensorManager.SENSOR_DELAY_FASTEST);

        }

        /* If we use the FusedSensor*/
        if (useFusedEstimation)
        {
            boolean hasGravity = mSensorManager.registerListener(
                    fusedGyroscopeSensor, mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
                    SensorManager.SENSOR_DELAY_FASTEST);

            /* If the device does not have a gravity sensor, fall back */
            if (!hasGravity)
            {
                mSensorManager.registerListener(fusedGyroscopeSensor,
                        mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                        SensorManager.SENSOR_DELAY_FASTEST);
            }

            mSensorManager.registerListener(fusedGyroscopeSensor,
                    mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                    SensorManager.SENSOR_DELAY_FASTEST);

            mSensorManager.registerListener(fusedGyroscopeSensor,
                    mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                    SensorManager.SENSOR_DELAY_FASTEST);

            fusedGyroscopeSensor.registerObserver(this);
        }
    }

    /* Calculates the initial device orientation, need only once */
    private void calculateOrientation() {
        hasInitialOrientation = SensorManager.getRotationMatrix(
                initialRotationMatrix, null, acceleration, magnetic);

        /* Remove the sensor observers since they are no longer required. */
        if (hasInitialOrientation)
        {
            mSensorManager.unregisterListener(this,
                    mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
            mSensorManager.unregisterListener(this,
                    mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD));
        }
    }

    public void onDestroy(){
        unregisterListeners();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void comprobarAccionesOrientation(){
        cAcciones = Acciones.listado();

        while(cAcciones.moveToNext()){

            if(Double.parseDouble(calibrationY) == 0.0 && Double.parseDouble(calibrationZ) == 0.0){
                return;
            }else{
                if(((Double.parseDouble(calibrationY)) >= cAcciones.getFloat(2)) &&
                        (Double.parseDouble(calibrationY) <= cAcciones.getFloat(3))
                        ){
                    if((Double.parseDouble(calibrationZ)) >= cAcciones.getFloat(4) &&
                            (Double.parseDouble(calibrationZ) <= cAcciones.getFloat(5)
                            )){
                        if(cAcciones.getString(6).equalsIgnoreCase("SILENCIO") && mAudioManager.getRingerMode() == 2){
                            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);

                        }else if (cAcciones.getString(6).equalsIgnoreCase("CORREO")){
                            Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "your_email"));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                }
            }
        }
        cAcciones.close();
    }
}
