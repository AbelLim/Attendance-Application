package com.example.arx8l.attendenceapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FileManager fm = new FileManager();
        fm.uploadFile("", new FileManager.OnFileUploadListener() {
            @Override
            public void OnStart() {
                Toast.makeText(MainActivity.this, "Uploading...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnSuccess() {
                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnFailure() {
                //Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_SHORT).show();
            }
        });

        //Intent intent = new Intent(this, LoginActivity.class);
        //startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.remove("current screen").commit();
    }
}
