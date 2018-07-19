package com.prcalibradores.app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.prcalibradores.app.database.DbSchema.UserTable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABE_NAME = "dbpr.db";

    public DatabaseHelper(Context context) {
        super(context, DATABE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + UserTable.NAME + "(" +
                UserTable.Cols.ID + ", " +
                UserTable.Cols.ID_DB + ", " +
                UserTable.Cols.USERNAME + ", " +
                UserTable.Cols.PASSWORD + ", " +
                UserTable.Cols.PROCESS_ID + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
