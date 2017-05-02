package com.studionobume.musicalgoogle.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.studionobume.musicalgoogle.Constants.Constants;
import com.studionobume.musicalgoogle.Database.DBController;
import com.studionobume.musicalgoogle.Fragments.SheetFragment;
import com.studionobume.musicalgoogle.MyApplication;
import com.studionobume.musicalgoogle.Networking.NetWorker;
import com.studionobume.musicalgoogle.Networking.SocketIO;
import com.studionobume.musicalgoogle.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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

//        database_controller = new DBController(getApplication(), this, getApplication());
//        database_controller.OpenDB();

        NetWorker nw = NetWorker.getSInstance();
        nw.get(Constants.newFiles, new NetWorker.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                Log.d("success", result);
                if (result.contains("error")) {
                    Toast.makeText(MyApplication.getAppContext(), "Toscanini says: " + result, Toast.LENGTH_LONG).show();
                    return;
                }

                PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext())
                        .edit().putString("newFiles", result).commit();

                Log.d("newFiles prefs", PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext())
                        .getString("newFiles", "defaultStringIfNothingFound"));
            }

            @Override
            public void onFailure(String result) {
                Log.d("failure", result);
            }
        });


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
