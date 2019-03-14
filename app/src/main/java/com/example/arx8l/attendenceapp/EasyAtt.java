package com.example.arx8l.attendenceapp;

import android.app.Application;
import android.content.Context;

/**
 * Created by User on 30/1/2019.
 */

public class EasyAtt extends Application {
    private static EasyAtt instance;

    public static EasyAtt getInstance() {
        return instance;
    }

    public static Context getContext(){
        return instance;
        // or return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }
}
