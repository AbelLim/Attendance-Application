/*This activity is the landing page. It redirects the program into the next step. It is also marked to not be added to the program history stack*/
package com.example.arx8l.attendenceapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.remove("current screen").commit();
    }
}
