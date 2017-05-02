package com.studionobume.musicalgoogle.Database;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.studionobume.musicalgoogle.Service.BackgroundService;
import com.studionobume.musicalgoogle.Networking.SocketIO;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
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
    private Socket mSocket;

    public DBController(Context context, BackgroundService backgdround_service, Application application) {
        db_helper = new QueryDatabase(context);
        this.background_service = background_service;
        this.context = context;

        SocketIO app = (SocketIO) application;
        mSocket = app.getSocket();
        mSocket.connect();
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
        UploadQueries();
        background_service.EraseBufferedQueries();
    }

    private void UploadQueries() {
        Log.d("db", "Uploaded queries was called.");

        if(db.isOpen() && IsFree()) {
            new AsyncTask<Void, Void, Void>() {
                Cursor cursor = null;
                JSONObject data;

                @Override
                protected Void doInBackground(Void... params) {
                    if (!mSocket.connected())
                        mSocket.connected();

                    JSONObject data;
                    JSONObject data2 = new JSONObject();

                    processing = true;
                    cursor = db.rawQuery("SELECT * FROM " + DBConstants.TABLE_NAME, null);

                    if (cursor.moveToFirst()) {
                        do {
                            Integer _id = cursor.getInt(cursor.getColumnIndex((DBConstants.COLUMN_NAME_ENTRY_ID)));
                            String date = cursor.getString(cursor.getColumnIndex(DBConstants.COLUMN_NAME_ENTRY_DATE));
                            String _query = cursor.getString(cursor.getColumnIndex(DBConstants.COLUMN_NAME_QUERY));

                            data = new JSONObject();

                            try {

                                data.put("id", _id + "");
                                data.put("date", date + "");
                                data.put("query", _query + "");
                                data2 = data;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (mSocket != null) {
                                mSocket.emit("results", data);
                            }

                        } while (cursor.moveToNext());
                    }
                    cursor.close();

                    processing = false;
                    mSocket.emit("query_upload_done", data2);

                    return null;
                    }

                    @Override
                    protected void onPostExecute (Void result){
                    }
            }.execute();
        }
    }

}
