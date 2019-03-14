/*This activity is the landing page. It redirects the program into the next step. It is also marked to not be added to the program history stack*/
package com.example.arx8l.attendenceapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences("login", MODE_PRIVATE);
        editor  = getSharedPreferences("login", MODE_PRIVATE).edit();

        String loggedInUserId = preferences.getString("userID", "");

        if (loggedInUserId.equals("")) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Intent intent = new Intent(this, MainMenuActivity.class);
            intent.putExtra("UserID", loggedInUserId);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
