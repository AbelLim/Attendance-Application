package com.example.arx8l.attendenceapp;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by User on 14/12/2018.
 */

public class BroadcastService extends Service {
    private final static String TAG = "BroadcastService";

    public static final String COUNTDOWN_BR = "your_package_name.countdown_br";
    Intent bi = new Intent(COUNTDOWN_BR);
    Intent fi = new Intent(COUNTDOWN_BR);

    CountDownTimer cdt = null;

    private boolean userTappedIn;
    private boolean timerFinished;
    private long mTimeLeftInMillis = 30000;
    private long timeUserTappedIn;

    @Override
    public void onCreate() {
        super.onCreate();

        final SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        userTappedIn = prefs.getBoolean("userTappedIn", false);

        timeUserTappedIn = prefs.getLong("timeUserTappedIn", 0);

        mTimeLeftInMillis = mTimeLeftInMillis - (System.currentTimeMillis() - timeUserTappedIn);

        Log.i(TAG, "Starting timer...");

        cdt = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                Log.i(TAG, "Countdown seconds remaining: " + millisUntilFinished / 1000);
                bi.putExtra("countdown", millisUntilFinished);
                sendBroadcast(bi);
            }

            @Override
            public void onFinish() {
                timerFinished = true;
                fi.putExtra("timer finish", timerFinished);
                sendBroadcast(fi);

                Log.i(TAG, "Timer finished");
            }
        };
        cdt.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
