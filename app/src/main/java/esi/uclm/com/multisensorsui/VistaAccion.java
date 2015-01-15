package esi.uclm.com.multisensorsui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by Javier on 18/11/2014.
 */
public class VistaAccion extends Activity {

    private Accion accion;
    private long id;
    private Intent intent;

    final static int RESULTADO_EDITAR_EORI = 1;
    static final int RESULTADO_EDITAR_ESHA = 2;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        id = extras.getLong("id", -1);
        accion = Acciones.elemento((int) id);

        if(accion.getTipo() == 0){
            setContentView(R.layout.vista_accion_orientation);
            actualizarVistaOri(accion);
        }else{
            setContentView(R.layout.vista_accion_shaking);
            actualizarVistaShake(accion);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.vista_accion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /**
         * Handle action bar item clicks here. The action bar will
         * automatically handle clicks on the Home/Up button, so long
         * as you specify a parent activity in AndroidManifest.xml.
         **/
        switch (item.getItemId()){

            case R.id.accion_editar:
                if(accion.getTipo() == 0){
                    intent = new Intent(VistaAccion.this, EditarAccionOrientation.class);
                    intent.putExtra("id", id);
                    startActivityForResult(intent, RESULTADO_EDITAR_EORI);
                }else{
                    intent = new Intent(VistaAccion.this, EditarAccionShaking.class);
                    intent.putExtra("id", id);
                    startActivityForResult(intent, RESULTADO_EDITAR_ESHA);
                }



                return true;

            case R.id.accion_borrar:
                Acciones.borrar((int)id);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void actualizarVistaOri(Accion accion){

        accion = Acciones.elemento((int) id);

        TextView nombreAccion = (TextView) findViewById(R.id.t_nombreAccion);
        nombreAccion.setText(accion.getNombre());

        TextView accionSel = (TextView)findViewById(R.id.accionSel);
        accionSel.setText(accion.getAccionSel());

        TextView minY = (TextView)findViewById(R.id.minY);
        minY.setText(Float.toString(accion.getMinY()));

        TextView maxY = (TextView)findViewById(R.id.maxY);
        maxY.setText(Float.toString(accion.getMaxY()));

        TextView minZ = (TextView)findViewById(R.id.minZ);
        minZ.setText(Float.toString(accion.getMinZ()));

        TextView maxZ = (TextView)findViewById(R.id.maxZ);
        maxZ.setText(Float.toString(accion.getMaxZ()));
    }

    public void actualizarVistaShake(Accion accion){
        accion = Acciones.elemento((int) id);

        TextView nombreAccion = (TextView) findViewById(R.id.t_nombreAccion);
        nombreAccion.setText(accion.getNombre());

        TextView accionSel = (TextView)findViewById(R.id.accionSel);
        accionSel.setText(accion.getAccionSel());

        TextView nShakes = (TextView)findViewById(R.id.t_nShakes);
        nShakes.setText(Integer.toString(accion.getNShakes()));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULTADO_EDITAR_EORI) {
            actualizarVistaOri(accion);
            findViewById(R.id.rel1).invalidate();
        }else if (requestCode == RESULTADO_EDITAR_ESHA) {
            actualizarVistaShake(accion);
            findViewById(R.id.rel2).invalidate();
        }
    }
}
