package com.studionobume.musicalgoogle.Database;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.studionobume.musicalgoogle.Service.BackgroundService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Togame on 5/1/2017.
 */

public class DBController {
    private SQLiteDatabase db;
    private QueryDatabase db_helper;
    private volatile Boolean processing =false;
    private BackgroundService background_service;
    private Context context;


    public DBController(Context context, BackgroundService backgdround_service, Application application) {
        db_helper = new QueryDatabase(context);
        this.background_service = background_service;
        this.context = context;

    }

    public void OpenDB() { db = db_helper.getWritableDatabase(); }

    public void CloseDB() {
        db.close();
        db = null;
    }

    private boolean IsFree() { return !processing; }

    public void RemovedQueries(final ArrayList<Long> confirmed) {
        Log.d("db", "Removing the past queries");
        for(Long epoch : confirmed) {
            db.delete(DBConstants.TABLE_NAME, DBConstants.COLUMN_NAME_ENTRY_DATE + "=" + epoch, null);
        }
        background_service.EraseConfirmedQueries();
    }

    public void InsertQueries(final ArrayList<ContentValues> tracked_queries) {
        Log.d("db", "Inserting steps");
        for(ContentValues value: tracked_queries) {
            db.insert(DBConstants.TABLE_NAME, null, value);
            Log.d("db", "Something is added.");
        }
        background_service.EraseBufferedQueries();
    }

}
