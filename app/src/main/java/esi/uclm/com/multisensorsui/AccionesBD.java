package esi.uclm.com.multisensorsui;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Javier on 21/11/2014.
 */
public class AccionesBD extends SQLiteOpenHelper {
    public AccionesBD(Context context) {
        super(context, "acciones", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase bd){

        bd.execSQL("CREATE TABLE acciones ("+
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "tipo INTEGER, "+
                "nombreAccion TEXT, "+
                "minY FLOAT, "+
                "maxY FLOAT, "+
                "minZ FLOAT, "+
                "maxZ FLOAT, "+
                "nShakes INTEGER, "+
                "accionSel TEXT, "+
                "time LONG)");

        bd.execSQL("INSERT INTO acciones VALUES (null, "+
        "0, 'SILENCIO', "+
        " -0.5, -0.5, -0.5, 0.5, 0, 'SILENCIO', "+ System.currentTimeMillis()+")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
