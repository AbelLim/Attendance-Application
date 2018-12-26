package com.example.arx8l.attendenceapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.common.base.Functions;
import com.google.common.collect.Ordering;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainMenuActivity extends AppCompatActivity implements
        MainScreenFragment.OnFragmentInteractionListener,
        CheckMyAttendanceFragment.OnFragmentInteractionListener,
        ClassAttendanceFragment.OnFragmentInteractionListener,
        CampusAttendanceFragment.OnFragmentInteractionListener, MainScreenFragment.OnSomeEventListener {

    private Bundle bundle;
    private int classAttendance = 95;
    private int campusAttendance;
    private HashMap<String, Boolean> campusAttendanceDaysCheck;
    private int daysTappedIn;
    private int daysTappedInRequired;
    private String studyPeriodStartDateInString = "05/11/2018";
    private String studyPeriodEndDateInString = "01/02/2019";
    private String startDateInString = "05/11/2018";
    private String endDateInString = "25/12/2018";
    private LocalDate currentDate;
    private String currentDateString;
    private String userId = "12345678";

    ImageView settings;
    ImageView tapInTapOut;
    ImageView checkMyAttendance;
    ImageView medicalLeave;

    AttendanceManager attendanceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getSupportActionBar().hide();

        campusAttendanceDaysCheck = new HashMap<String, Boolean>();

        attendanceManager = new AttendanceManager();


        loadHashMap();

        registerReceiver(mDateReceiver, new IntentFilter(Intent.ACTION_DATE_CHANGED));
        System.out.println("Register service>>>>>>>>>>>>>>>>>>>");

        currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        currentDateString = currentDate.format(formatter);

        if (campusAttendanceDaysCheck.containsKey(currentDateString)
                && campusAttendanceDaysCheck.containsKey(getYesterdayDateString())) {
            if(campusAttendanceDaysCheck.get(getYesterdayDateString()) == null){
                campusAttendanceDaysCheck.put(getYesterdayDateString(), false);
                attendanceManager.tapOut(userId, new AttendanceManager.OnTapOutListener() {
                    @Override
                    public void OnStart() {

                    }

                    @Override
                    public void OnSuccess() {
                        upDateMainScreen();
                    }

                    @Override
                    public void OnFailure() {

                    }
                });
            }
        }
        else if (!campusAttendanceDaysCheck.containsKey(currentDateString)
                && campusAttendanceDaysCheck.containsKey(getYesterdayDateString())){
            if(campusAttendanceDaysCheck.get(getYesterdayDateString()) == null){
                campusAttendanceDaysCheck.put(getYesterdayDateString(), false);
                attendanceManager.tapOut(userId, new AttendanceManager.OnTapOutListener() {
                    @Override
                    public void OnStart() {

                    }

                    @Override
                    public void OnSuccess() {
                        upDateMainScreen();
                    }

                    @Override
                    public void OnFailure() {

                    }
                });
            }
        }

//        campusAttendanceDaysCheck.put("25/12/2018", null);
//        campusAttendanceDaysCheck.put("26/12/2018", null);


//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        LocalDate startDate = LocalDate.parse(studyPeriodStartDateInString, formatter);
//        LocalDate endDate = LocalDate.parse(studyPeriodEndDateInString, formatter);
//        LocalDate startDate = LocalDate.parse(startDateInString, formatter);
//        LocalDate endDate = LocalDate.parse(endDateInString, formatter);

//        if (!currentDate.getDayOfWeek().name().equals("SATURDAY") && !currentDate.getDayOfWeek().name().equals("SUNDAY")) {
//            if (!campusAttendanceDaysCheck.containsKey(currentDate)) {
//                campusAttendanceDaysCheck.put(currentDate, false);
//            }
//        }

//        campusAttendanceDaysCheck.clear();

//        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1))
//        {
//            if(!date.getDayOfWeek().name().equals("SATURDAY") && !date.getDayOfWeek().name().equals("SUNDAY")) {
//                campusAttendanceDaysCheck.put(date.format(formatter), null);
//            }
//        }
//        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1))
//        {
//            int randNum = new Random().nextInt(2);
//            if(randNum == 0) {
//                campusAttendanceDaysCheck.put(date.format(formatter), false);
//            }
//            else if (randNum == 1){
//                campusAttendanceDaysCheck.put(date.format(formatter), true);
//            }
//
//        }

        saveHashMap();

        System.out.println("MainMenuActivity >>> ");
        ArrayList<String> sortedKeys =
                new ArrayList<String>(campusAttendanceDaysCheck.keySet());
        Collections.sort(sortedKeys, new Comparator<String>() {
            DateFormat f = new SimpleDateFormat("dd/MM/yyyy");
            @Override
            public int compare(String o1, String o2) {
                try {
                    return f.parse(o1).compareTo(f.parse(o2));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });

        for (String x : sortedKeys)
            System.out.println(x + ": " + campusAttendanceDaysCheck.get(x));


        checkMyAttendance = findViewById(R.id.check_my_attendance);
        tapInTapOut = findViewById(R.id.tap_in_tap_out);
        medicalLeave = findViewById(R.id.medical_leave);
        settings = findViewById(R.id.settings);

        daysTappedIn = 0;
        daysTappedInRequired = 0;
        for (String name: campusAttendanceDaysCheck.keySet()){
            if (campusAttendanceDaysCheck.get(name) != null) {
                daysTappedInRequired += 1;
                if (campusAttendanceDaysCheck.get(name)) {
                    daysTappedIn += 1;
                }
            }
        }

        campusAttendance = (int) (((float) daysTappedIn/ (float) daysTappedInRequired) * 100);

        bundle = new Bundle();

        bundle.putInt("class attendance", classAttendance);
        bundle.putInt("campus attendance", campusAttendance);

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
                MedicalLeaveFragment medicalLeaveFragment = new MedicalLeaveFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_frag, medicalLeaveFragment, "");
                fragmentTransaction.commit();

            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mDateReceiver);
        System.out.println("Unregister service>>>>>>>>>>>>>>>>>>>");
    }

    @Override
    public void someEvent(HashMap<String,Boolean> campusDaysCheck) {
//        campusAttendanceDaysCheck = campusDaysCheck;
        upDateMainScreen();
    }

    public void upDateMainScreen(){
        loadHashMap();
        daysTappedIn = 0;
        daysTappedInRequired = 0;

        for (String name: campusAttendanceDaysCheck.keySet()){
            if (campusAttendanceDaysCheck.get(name) != null) {
                daysTappedInRequired += 1;
                if (campusAttendanceDaysCheck.get(name)) {
                    daysTappedIn += 1;
                }
            }
        }

        System.out.println(daysTappedIn);
        System.out.println(daysTappedInRequired);

        campusAttendance = (int) (((float) daysTappedIn/ (float) daysTappedInRequired) * 100);

        System.out.println(campusAttendance);

        bundle.putInt("class attendance", classAttendance);
        bundle.putInt("campus attendance", campusAttendance);

        MainScreenFragment mainScreenFragment = new MainScreenFragment();
        mainScreenFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frag, mainScreenFragment, "");
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void saveHashMap(){
        File file = new File(getDir("data", MODE_PRIVATE), "map");
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(campusAttendanceDaysCheck);
            outputStream.flush();
            outputStream.close();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void loadHashMap(){
        File file = new File(getDir("data", MODE_PRIVATE), "map");
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
            campusAttendanceDaysCheck = (HashMap<String, Boolean>) inputStream.readObject();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private String getYesterdayDateString(){
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(cal.getTime());
    }

    private final MyDateChangeReceiver mDateReceiver = new MyDateChangeReceiver();

    public class MyDateChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, Intent intent) {
            currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            currentDateString = currentDate.format(formatter);

            if (campusAttendanceDaysCheck.containsKey(currentDateString)
                    && campusAttendanceDaysCheck.containsKey(getYesterdayDateString())) {
                if(campusAttendanceDaysCheck.get(getYesterdayDateString()) == null){
                    campusAttendanceDaysCheck.put(getYesterdayDateString(), false);
                    saveHashMap();
                    attendanceManager.tapOut(userId, new AttendanceManager.OnTapOutListener() {
                        @Override
                        public void OnStart() {

                        }

                        @Override
                        public void OnSuccess() {
                            upDateMainScreen();
                        }

                        @Override
                        public void OnFailure() {

                        }
                    });
                }
            }
            else if (!campusAttendanceDaysCheck.containsKey(currentDateString)
                    && campusAttendanceDaysCheck.containsKey(getYesterdayDateString())){
                if(campusAttendanceDaysCheck.get(getYesterdayDateString()) == null){
                    campusAttendanceDaysCheck.put(getYesterdayDateString(), false);
                    saveHashMap();
                    attendanceManager.tapOut(userId, new AttendanceManager.OnTapOutListener() {
                        @Override
                        public void OnStart() {

                        }

                        @Override
                        public void OnSuccess() {
                            upDateMainScreen();
                        }

                        @Override
                        public void OnFailure() {

                        }
                    });
                }
            }
        }
    }
}
