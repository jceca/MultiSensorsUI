package esi.uclm.com.multisensorsui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Javier on 03/12/2014.
 */
public class EditarAccionShaking extends Activity {

    private Accion accion;
    private long id;
    private EditText nShakes;
    private Spinner sItems;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edicion_accion_shaking);

        Bundle extras = getIntent().getExtras();
        id = extras.getLong("id", -1);
        accion = Acciones.elemento((int) id);

        initUI();
    }

    private void initUI() {

        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("EMAIL");
        spinnerArray.add("EVENTO");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sItems = (Spinner) findViewById(R.id.t_spinnerAction);
        sItems.setAdapter(adapter);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.editar_accion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /**
         * Handle action bar item clicks here. The action bar will
         * automatically handle clicks on the Home/Up button, so long
         * as you specify a parent activity in AndroidManifest.xml.
         **/
        switch (item.getItemId()) {

            case R.id.accion_guardar:
                accion.setTipo(1);
                nShakes = (EditText)findViewById(R.id.nShakes);
                if(Integer.parseInt(nShakes.getText().toString()) == 1){
                    accion.setNShakes(Integer.parseInt(nShakes.getText().toString()));
                }else if (Integer.parseInt(nShakes.getText().toString()) == 2) {
                    accion.setNShakes(Integer.parseInt(nShakes.getText().toString()));
                }
                accion.setNombre(sItems.getSelectedItem().toString());
                accion.setAccionSel(sItems.getSelectedItem().toString());

                Acciones.updateAccion((int) id, accion);
                finish();
                return true;

            case R.id.accion_cancelar:
                Toast.makeText(getApplicationContext(), "Sin cambios", Toast.LENGTH_LONG).show();
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
