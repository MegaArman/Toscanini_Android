package com.studionobume.musicalgoogle.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.studionobume.musicalgoogle.Database.DBController;
import com.studionobume.musicalgoogle.Networking.SocketIO;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by Togame on 4/30/2017.
 */

public class BackgroundService extends Service {

    ArrayList<ContentValues> list = new ArrayList<ContentValues>();
    ArrayList<Long> listTime = new ArrayList<Long>();
    private DBController database_controller;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);

        registerReceiver(receiver, filter);

        Log.d("background_service", "BackgroundService has Started!");

        database_controller = new DBController(getApplication(), this, getApplication());
        database_controller.OpenDB();

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("background_service", "BackgroundService has Stopped!");
        unregisterReceiver(receiver);
        database_controller.CloseDB();
        database_controller = null;

        super.onDestroy();
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.contains(Intent.ACTION_TIME_TICK)) {
               // database_controller.InsertQueries();
            }
        }
    };

    public void EraseBufferedQueries() {
        Log.d("db", "buffered cleared");
    }

    public void EraseConfirmedQueries() {
        Log.d("db", "confirmed cleared");
    }
}
