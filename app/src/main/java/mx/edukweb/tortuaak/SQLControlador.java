package mx.edukweb.tortuaak;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Leonel on 08/02/2015.
 */
public class SQLControlador {
    private DBhelper dbhelper;
    private Context ourcontext;
    private SQLiteDatabase database;

    public SQLControlador(Context c) {
        ourcontext = c;
    }

    public SQLControlador abrirBaseDeDatos() throws SQLException {
        dbhelper = new DBhelper(ourcontext);
        database = dbhelper.getWritableDatabase();
        return this;
    }

    public void cerrar() {
        dbhelper.close();
    }

    public void insertarDatos(String name, String huevos, String playa, String latitud, String longitud) {
        ContentValues cv = new ContentValues();
        cv.put(DBhelper.REGISTRO_RESPONSABLE, name);
        cv.put(DBhelper.REGISTRO_HUEVOS, huevos);
        cv.put(DBhelper.REGISTRO_PLAYA, playa);
        cv.put(DBhelper.REGISTRO_LAT, latitud);
        cv.put(DBhelper.REGISTRO_LON, longitud);
        cv.put(DBhelper.REGISTRO_FECHA, getDateTime());



       database.insert(DBhelper.TABLE_REGISTRO, null, cv);
        //Devolver el id del nido insertado para comprobar que se haya realizado con exito
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }


    public Cursor leerDatos() {
        String[] todasLasColumnas = new String[] {
                DBhelper.REGISTRO_ID,
                DBhelper.REGISTRO_RESPONSABLE,
                DBhelper.REGISTRO_HUEVOS,
                DBhelper.REGISTRO_LAT,
                DBhelper.REGISTRO_LON,
                DBhelper.REGISTRO_FECHA

        };
        Cursor c = database.query(DBhelper.TABLE_REGISTRO, todasLasColumnas, null,
                null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }
/*
    public int actualizarDatos(long memberID, String memberName) {
        ContentValues cvActualizar = new ContentValues();
        cvActualizar.put(DBhelper.MIEMBRO_NOMBRE, memberName);
        int i = database.update(DBhelper.TABLE_MEMBER, cvActualizar,
                DBhelper.MIEMBRO_ID + " = " + memberID, null);
        return i;
    }
*/
    public void deleteData(long memberID) {
        database.delete(DBhelper.TABLE_REGISTRO, DBhelper.REGISTRO_ID + "="
                + memberID, null);
    }

}
