package mx.edukweb.tortuaak;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Leonel on 08/02/2015.
 */
public class DBhelper extends SQLiteOpenHelper {

    // Información de la tabla
    public static final String TABLE_REGISTRO = "Registro";
    public static final String REGISTRO_ID = "_id";
    public static final String REGISTRO_RESPONSABLE = "responsable";
    public static final String REGISTRO_HUEVOS = "huevos";
    public static final String REGISTRO_PLAYA = "playa";
    public static final String REGISTRO_LAT = "latitud";
    public static final String REGISTRO_LON = "longitud";
    public static final String REGISTRO_FECHA = "fecha";



    // información del a base de datos
    static final String DB_NAME = "TortuAak";
    static final int DB_VERSION = 6;

    // Información de la base de datos
    private static final String CREATE_TABLE = "create table "
            + TABLE_REGISTRO + "(" + REGISTRO_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + REGISTRO_RESPONSABLE + " TEXT NOT NULL, "
            + REGISTRO_HUEVOS + " TEXT, "
            + REGISTRO_PLAYA + " TEXT, "
            + REGISTRO_FECHA + " TIMESTAMP NOT NULL DEFAULT current_timestamp, "
            + REGISTRO_LAT + " TEXT NOT NULL, "
            + REGISTRO_LON + " TEXT NOT NULL);";

    public DBhelper(Context context) {
        super(context, DB_NAME, null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGISTRO);
        onCreate(db);
    }
}