package esi.uclm.com.multisensorsui;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaCodecInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;

import static android.util.FloatMath.cos;
import static android.util.FloatMath.sin;
import static android.util.FloatMath.sqrt;

/**
 * Created by Javier on 09/11/2014.
 */
public class ListaAcciones extends ListActivity {

    public BaseAdapter adaptador;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_acciones);

        adaptador = new ArrayAdapter<String>(this,
                R.layout.elemento_accion, R.id.nombreAccionElemento,
                Acciones.listaNombres());
        setListAdapter(adaptador);

    }

    public void onResume(){
        super.onResume();

        adaptador = new ArrayAdapter<String>(this,
                R.layout.elemento_accion, R.id.nombreAccionElemento,
                Acciones.listaNombres());
        setListAdapter(adaptador);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lista_acciones, menu);
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

            case R.id.accion_añadir:
                Intent intent = new Intent(this, EditarAccion.class);
                long id = Acciones.nuevo();
                intent.putExtra("id", id);
                startActivityForResult(intent, 1234);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    protected void onListItemClick(ListView listView,
                                   View vista, int posicion, long id) {
        super.onListItemClick(listView, vista, posicion, id);
        Intent intent = new Intent(this, VistaAccion.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}
