package esi.uclm.com.multisensorsui;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Javier on 30/10/2014.
 */
public class Acciones {
    public static final String TAG = "ACCIONES TAG";

    private static AccionesBD accionesBD;

    public static Accion elemento(int id) {
        Accion accion = null;
        SQLiteDatabase bd = accionesBD.getReadableDatabase();
        Cursor cursor = bd.rawQuery("SELECT * FROM acciones WHERE _id = " + id, null);
        if (cursor.moveToNext()){
            accion = new Accion();
            accion.setNombre(cursor.getString(1));
            accion.setMinY(cursor.getFloat(2));
            accion.setMaxY(cursor.getFloat(3));
            accion.setMinZ(cursor.getFloat(4));
            accion.setMaxZ(cursor.getFloat(5));
            accion.setAccionSel(cursor.getString(6));
            accion.setTime(cursor.getLong(7));
        }

        cursor.close();
        bd.close();

        return accion;
    }

    public static void updateAccion(int id, Accion accion){
        SQLiteDatabase bd = accionesBD.getWritableDatabase();
        bd.execSQL("UPDATE acciones SET nombreAccion = '"+ accion.getAccionSel() +
        "', minY = " + accion.getMinY() +
        ", maxY = " + accion.getMaxY() +
        ", minZ = " + accion.getMinZ() +
        ", maxZ = " + accion.getMaxZ() +
        ", accionSel = '" + accion.getAccionSel() +
        "', time = " + accion.getTime() +
        " WHERE _id = "+ id);

        bd.close();
    }

    public static int nuevo(){
        int id = -1;
        Accion accion = new Accion();
        SQLiteDatabase bd = accionesBD.getWritableDatabase();
        bd.execSQL("INSERT INTO acciones (time) VALUES("+
                accion.getTime() +")");
        Cursor c = bd.rawQuery("SELECT _id FROM acciones WHERE time = " +
                accion.getTime(), null);

        if(c.moveToNext()){
            id = c.getInt(0);
        }

        c.close();
        bd.close();
        return id;
    }

    static void borrar(int id){
        SQLiteDatabase bd = accionesBD.getWritableDatabase();
        bd.execSQL("DELETE FROM acciones WHERE _id =" + id);
        bd.close();
    }

    public static void inicializarBD(Context contexto){
        accionesBD = new AccionesBD(contexto);
    }

    public static Cursor listado() {
        SQLiteDatabase bd = accionesBD.getReadableDatabase();
        return bd.rawQuery("SELECT * FROM acciones", null);
    }
}
