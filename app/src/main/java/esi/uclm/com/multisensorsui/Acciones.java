package esi.uclm.com.multisensorsui;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

/**
 * Created by Javier on 30/10/2014.
 */
public class Acciones extends ListActivity {
    public BaseAdapter adaptador;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
