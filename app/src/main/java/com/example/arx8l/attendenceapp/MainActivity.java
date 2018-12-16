package com.example.arx8l.attendenceapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new Database();

        Intent mainMenuIntent = new Intent(this, MainMenuActivity.class);
        startActivity(mainMenuIntent);

//    @Override
//    public void onClick(View v) {
        //Create new user
        /*database.createUser("12345678", "Jim", "Jim@my.jcu.edu.au", "password");*/

        //Login authentication
        /*if(database.isLoginCorrect("Jim@my.jcu.edu.au", "password"))
            textView.setText("Login Correct");
        else
            textView.setText("Login Failed");*/
//    }

    }
}
