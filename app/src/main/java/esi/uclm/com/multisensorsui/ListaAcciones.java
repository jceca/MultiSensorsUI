package esi.uclm.com.multisensorsui;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

/**
 * Created by Javier on 09/11/2014.
 */
public class ListaAcciones extends ListActivity {

    public BaseAdapter adaptador;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_acciones);

        adaptador = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                Acciones.listaNombres());
        setListAdapter(adaptador);
    }

}
