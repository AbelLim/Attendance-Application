package com.example.arx8l.attendenceapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class MainMenuActivity extends AppCompatActivity implements
        MainScreenFragment.OnFragmentInteractionListener,
        CheckMyAttendanceFragment.OnFragmentInteractionListener,
         ClassAttendanceFragment.OnFragmentInteractionListener,
        CampusAttendanceFragment.OnFragmentInteractionListener{

    private Database database;
    private Bundle bundle;
    private int classAttendance = 95;
    private int campusAttendance = 93;

    ImageView settings;
    ImageView tapInTapOut;
    ImageView checkMyAttendance;
    ImageView medicalLeave;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getSupportActionBar().hide();

        preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);

        database = new Database();
        bundle = new Bundle();

        bundle.putInt("class attendance", classAttendance);
        bundle.putInt("campus attendance", campusAttendance);

        checkMyAttendance = findViewById(R.id.check_my_attendance);
        tapInTapOut = findViewById(R.id.tap_in_tap_out);
        medicalLeave = findViewById(R.id.medical_leave);
        settings = findViewById(R.id.settings);

        MainScreenFragment mainScreenFragment = new MainScreenFragment();
        mainScreenFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_frag, mainScreenFragment, "");
        fragmentTransaction.commit();

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        checkMyAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckMyAttendanceFragment checkMyAttendanceFragment = new CheckMyAttendanceFragment();
                checkMyAttendanceFragment.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStack();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_frag, checkMyAttendanceFragment, "").addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        medicalLeave.setOnClickListener(new View.OnClickListener() {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
