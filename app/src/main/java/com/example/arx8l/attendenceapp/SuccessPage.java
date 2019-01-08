package com.example.arx8l.attendenceapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SuccessPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success_layout);
        getSupportActionBar().hide();
    }
}
