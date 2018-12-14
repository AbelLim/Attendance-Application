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

public class MainActivity extends AppCompatActivity
        implements MainScreenFragment.OnFragmentInteractionListener {

    private Database database;

    ImageView settings;
    ImageView tapInTapOut;
    ImageView checkAttendance;
    ImageView medical;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        database = new Database();

//        Intent loginIntent = new Intent(this, LoginActivity.class);
//        startActivity(loginIntent);

        checkAttendance = findViewById(R.id.check_attendance);
        tapInTapOut = findViewById(R.id.tap_in_tap_out);
        medical = findViewById(R.id.medical);
        settings = findViewById(R.id.settings);

        MainScreenFragment mainScreenFragment = new MainScreenFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_frag, mainScreenFragment, "");
        fragmentTransaction.commit();

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        checkAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                CheckAttendanceFragment checkAttendanceFragment = new CheckAttendanceFragment();
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.main_frag, checkAttendanceFragment, "");
//                fragmentTransaction.commit();
            }
        });


        medical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                MedicalLeaveFragment medicalLeaveFragment = new MedicalLeaveFragment();
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.main_frag, medicalLeaveFragment, "");
//                fragmentTransaction.commit();

            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

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
