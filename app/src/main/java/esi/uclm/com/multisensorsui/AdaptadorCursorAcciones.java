package esi.uclm.com.multisensorsui;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.CursorAdapter;

/**
 * Created by Javier on 21/11/2014.
 */
public class AdaptadorCursorAcciones extends CursorAdapter{
    private LayoutInflater inflador; // Crea Layouts a partir de xml

    TextView nombreAccion, minY, maxY, minZ, maxZ, accionSel;

    public AdaptadorCursorAcciones(Context context, Cursor c) {
        super(context, c, false);
    }

    @Override
    public View newView(Context context, Cursor c, ViewGroup padre){
        inflador = (LayoutInflater)context

                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vista = inflador.inflate(R.layout.elemento_accion, padre, false);
        return vista;
    }

    @Override
    public void bindView(View vista, Context context, Cursor c){
        nombreAccion = (TextView)vista.findViewById(R.id.nombreAccionElemento);
        nombreAccion.setText(c.getString(c.getColumnIndex("nombreAccion")));

/*        minY = (TextView)vista.findViewById(R.id.minY);
        minY.setText(c.getString(c.getColumnIndex("minY")));

        maxY = (TextView)vista.findViewById(R.id.maxY);
        maxY.setText(c.getString(c.getColumnIndex("maxY")));

        minZ = (TextView)vista.findViewById(R.id.minZ);
        minZ.setText(c.getString(c.getColumnIndex("minZ")));

        maxZ = (TextView)vista.findViewById(R.id.maxZ);
        maxZ.setText(c.getString(c.getColumnIndex("maxZ")));*/
    }
}
