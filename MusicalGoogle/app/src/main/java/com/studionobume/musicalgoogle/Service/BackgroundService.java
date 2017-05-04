package com.studionobume.musicalgoogle.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.studionobume.musicalgoogle.Constants.Constants;
import com.studionobume.musicalgoogle.Database.DBController;
import com.studionobume.musicalgoogle.MainActivity;
import com.studionobume.musicalgoogle.MyApplication;
import com.studionobume.musicalgoogle.Networking.NetWorker;
import com.studionobume.musicalgoogle.R;

import java.util.ArrayList;

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
            Log.d("recieved", "recieved");

            //check if there's new music:
            NetWorker nw = NetWorker.getSInstance();
            nw.get(Constants.newFiles, new NetWorker.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    Log.d("success", result);
                    String oldstring = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext())
                            .getString("newFiles", "defaultStringIfNothingFound");

                    if (!oldstring.equals(result)) {
                        PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext())
                                .edit().putString("newFiles", result).commit();
                        notifyNewScores();
                    }
                }

                @Override
                public void onFailure(String result) {
                    Log.d("failure", result);
                }
            });
        }
    };

    public void notifyNewScores() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.ic_a_c_d_notes)
                        .setContentTitle("Toscanini")
                        .setContentText("New scores added! Tap to see which!")
                        .setAutoCancel(true);
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
        resultIntent.setAction("OPEN_RECENTLY_ADDED");
        PendingIntent.getService(getApplicationContext(), 0, resultIntent, 0);
// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(0, mBuilder.build());
    }

    public void EraseBufferedQueries() {
        Log.d("db", "buffered cleared");
    }

    public void EraseConfirmedQueries() {
        Log.d("db", "confirmed cleared");
    }
}
