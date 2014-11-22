package esi.uclm.com.multisensorsui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Javier on 11/11/2014.
 */
public class AdaptadorAcciones extends BaseAdapter{
    private LayoutInflater inflador; // Crea Layouts a partir del XML
    TextView nombre;

    public AdaptadorAcciones(Context contexto) {
        inflador = (LayoutInflater) contexto
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public View getView(int posicion, View vistaReciclada, ViewGroup padre) {
        Accion accion = Acciones.elemento(posicion);
        if (vistaReciclada == null) {
            vistaReciclada = inflador.inflate(R.layout.elemento_accion, null);
        }
        nombre = (TextView) vistaReciclada.findViewById(R.id.nombreAccionElemento);

        nombre.setText(accion.getNombre());

        return vistaReciclada;
    }

    @Override
    public int getCount() {
        return 0;
    }

    public Object getItem(int posicion) {
        return Acciones.elemento(posicion);
    }

    public long getItemId(int posicion) {
        return posicion;
    }
}


