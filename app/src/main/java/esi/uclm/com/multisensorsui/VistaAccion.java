package esi.uclm.com.multisensorsui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    final static int RESULTADO_EDITAR = 1;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_accion);
        Bundle extras = getIntent().getExtras();
        id = extras.getLong("id", -1);
        accion = Acciones.elemento((int) id);
        actualizarVista(accion);

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
                intent = new Intent(VistaAccion.this, EditarAccion.class);
                intent.putExtra("id", id);
                Log.d(Acciones.TAG, "VistaAccion - onOptionsItemSelected - id -> "
                        + id);
                startActivityForResult(intent, RESULTADO_EDITAR);
                return true;

            case R.id.accion_borrar:
                Acciones.borrar((int)id);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void actualizarVista(Accion accion){
        TextView nombreAccion = (TextView) findViewById(R.id.t_nombreAccion);
        nombreAccion.setText(accion.getNombre());

        TextView minY = (TextView)findViewById(R.id.minY);
        minY.setText(Float.toString(accion.getMinY()));

        TextView maxY = (TextView)findViewById(R.id.maxY);
        maxY.setText(Float.toString(accion.getMaxY()));

        TextView minZ = (TextView)findViewById(R.id.minZ);
        minZ.setText(Float.toString(accion.getMinZ()));

        TextView maxZ = (TextView)findViewById(R.id.maxZ);
        maxZ.setText(Float.toString(accion.getMaxZ()));

        TextView accionSel = (TextView)findViewById(R.id.accionSel);
        accionSel.setText(accion.getAccionSel());
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULTADO_EDITAR) {
            actualizarVista(accion);
            findViewById(R.id.rel1).invalidate();
        }
    }

}
