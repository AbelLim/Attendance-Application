package com.example.arx8l.attendenceapp;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.gson.Gson;


/**
 * Created by User on 28/1/2019.
 */

public class DetectAppIsKilledService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Intent serviceIntent = new Intent(this, LocationService.class);
        startService(serviceIntent);
        this.stopSelf();
    }
}
