package com.studionobume.musicalgoogle.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Togame on 5/1/2017.
 */

public class QueryDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "query.db";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " +
            DBConstants.TABLE_NAME + "( " +
            DBConstants.COLUMN_NAME_ENTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMNT, " +
            DBConstants.COLUMN_NAME_ENTRY_DATE + " TEXT, " +
            DBConstants.COLUMN_NAME_QUERY + " TEXT);";

    public QueryDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //No need
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, DBConstants.COLUMN_NAME_ENTRY_ID);
        return numRows;
    }
}
