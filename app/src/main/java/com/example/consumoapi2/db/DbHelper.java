package com.example.consumoapi2.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NOMBRE = "trabajador.db";
    public static final String TABLE_TRABAJADOR = "t_trabajador";

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NOMBRE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_TRABAJADOR + "(" +
                "idempresa INTEGER," +
                "idtrabajador TEXT," +
                "nombresall TEXT," +
                "habilitado INTEGER," +
                "cnrodocumento TEXT," +
                "idplanilla TEXT," +
                "listanegra TEXT," +
                "liquidado TEXT," +
                "fecha_ingreso TEXT," +
                "fecha_cese TEXT," +
                "fecha_liquidado TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_TRABAJADOR);
        onCreate(sqLiteDatabase);

    }
}
