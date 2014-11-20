package esi.uclm.com.multisensorsui;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Javier on 30/10/2014.
 */
public class Acciones {
    public static final String TAG = "ACCIONES TAG";

    protected static List<Accion> vectorAcciones = ejemploAcciones();

    public Acciones() {
        vectorAcciones = ejemploAcciones();
    }

    static Accion elemento(int id){
        return vectorAcciones.get(id);
    }

    static void anyade(Accion accion){
        vectorAcciones.add(accion);
    }

    static int nuevo(){
        Accion lugar = new Accion();
        vectorAcciones.add(lugar);
        return vectorAcciones.size()-1;
    }

    static void borrar(int id){
        vectorAcciones.remove(id);
    }

    public static int size() {
        return vectorAcciones.size();
    }

    public static ArrayList<Accion> ejemploAcciones() {
        ArrayList<Accion> acciones = new ArrayList<Accion>();
        acciones.add(new Accion("Silencio", -5.0f, 5.0f, -0.5f, -0.5f, "SILENCIO"));
        return acciones;
    }

    static List<String> listaNombres() {
        ArrayList<String> resultado = new ArrayList<String>();
        for (Accion accion : vectorAcciones) {
            resultado.add("Acción: "+accion.getAccionSel());
        }
        return resultado;
    }
}
