package com.example.arx8l.attendenceapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

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
        CampusAttendanceFragment.OnFragmentInteractionListener, MainScreenFragment.OnAttendanceChangeListener ,
        DetailClassAttendanceFragment.OnFragmentInteractionListener{

    private Bundle bundle;
    private int classAttendance;
    private int campusAttendance;
    private HashMap<String, Boolean> campusAttendanceDaysCheck;
    private HashMap<String, HashMap<String, Boolean>> classAttendanceDaysCheck;
    private HashMap<String, Boolean> cp3408Lecture;
    private HashMap<String, Boolean> cp3408Practical;
    private int daysTappedIn;
    private int daysTappedInRequired;
    private String studyPeriodStartDateInString = "05/11/2018";
    private String studyPeriodEndDateInString = "02/02/2019";
    private String startDate = "05/11/2018";
    private String endDate = "02/02/2019";
    private String startDateInStringCP3408L = "07/11/2018";
    private String endDateInStringCP3408L = "16/01/2019";
    private String startDateInStringCP3408P = "08/11/2018";
    private String endDateInStringCP3408P = "09/01/2019";
    private LocalDate currentDate;
    private String currentDateString;
    private String userIdForTesting = "12345678";
    private String userId;
    private User userInfo;

    
    ImageView settings;
    ImageView tapInTapOut;
    ImageView checkMyAttendance;
    ImageView medicalLeave;

    AttendanceManager attendanceManager;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getSupportActionBar().hide();

        campusAttendanceDaysCheck = new HashMap<String, Boolean>();
        classAttendanceDaysCheck = new HashMap<String, HashMap<String, Boolean>>();
        cp3408Lecture = new HashMap<String, Boolean>();
        cp3408Practical = new HashMap<String, Boolean>();



        attendanceManager = new AttendanceManager();

        //Get User info from server.
        requestUserInfo();

        loadHashMap();

        classAttendanceDaysCheck.put("CP3408-Lecture", cp3408Lecture);
        classAttendanceDaysCheck.put("CP3408-Practical", cp3408Practical);

        registerReceiver(mDateReceiver, new IntentFilter(Intent.ACTION_DATE_CHANGED));

        currentDate = LocalDate.now();
        currentDateString = currentDate.format(formatter);

//        campusAttendanceDaysCheck.clear();

        if(campusAttendanceDaysCheck.get(getYesterdayDateString())== null &&
                campusAttendanceDaysCheck.containsKey(getYesterdayDateString())){
            attendanceManager.tapOut(userIdForTesting, new AttendanceManager.OnTapOutListener() {
                @Override
                public void OnStart() {

                }

                @Override
                public void OnSuccess() {

                }

                @Override
                public void OnFailure() {

                }
            });
        }

        newDayUpdateAttendance(campusAttendanceDaysCheck);
        for (String key : classAttendanceDaysCheck.keySet()){
            newDayUpdateAttendance(classAttendanceDaysCheck.get(key));
        }

//        campusAttendanceDaysCheck.put("01/01/2019", null);
//        campusAttendanceDaysCheck.put("02/01/2019", null);
//        classAttendanceDaysCheck.get("CP3408-Lecture").put("02/01/2019" , null);
//        classAttendanceDaysCheck.get("CP3408-Practical").put("03/01/2019" , null);

//        LocalDate startDate = LocalDate.parse(studyPeriodStartDateInString, formatter);
//        LocalDate endDate = LocalDate.parse(studyPeriodEndDateInString, formatter);
//        LocalDate startDateCP3408L = LocalDate.parse(startDateInStringCP3408L, formatter);
//        LocalDate endDateCP3408L = LocalDate.parse(endDateInStringCP3408L, formatter);
//        LocalDate startDateCP3408P = LocalDate.parse(startDateInStringCP3408P, formatter);
//        LocalDate endDateCP3408P = LocalDate.parse(endDateInStringCP3408P, formatter);
//        LocalDate startDate = LocalDate.parse(this.startDate, formatter);
//        LocalDate endDate = LocalDate.parse(this.endDate, formatter);
//        LocalDate currentDate = LocalDate.parse("01/01/2019", formatter);

//        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
//            if(!date.getDayOfWeek().name().equals("SATURDAY") && !date.getDayOfWeek().name().equals("SUNDAY")) {
//                campusAttendanceDaysCheck.put(date.format(formatter), null);
//            }
//            System.out.println("Hey Hey Hey");
//        }
//
//        for (LocalDate date = startDate; date.isBefore(currentDate); date = date.plusDays(1))
//        {
//            if(!date.getDayOfWeek().name().equals("SATURDAY") && !date.getDayOfWeek().name().equals("SUNDAY")) {
//                int randNum = new Random().nextInt(2);
//                if (randNum == 0) {
//                    campusAttendanceDaysCheck.put(date.format(formatter), false);
//                } else if (randNum == 1) {
//                    campusAttendanceDaysCheck.put(date.format(formatter), true);
//                }
//            }
//        }

        saveHashMap();

//        for (String name : classAttendanceDaysCheck.keySet()){
//            System.out.println(name + ": " + classAttendanceDaysCheck.get(name));
//        }
//
//        System.out.println("campusAttendanceDaysCheck >>> ");
//        ArrayList<String> sortedKeys2 =
//                new ArrayList<String>(campusAttendanceDaysCheck.keySet());
//        Collections.sort(sortedKeys2, new Comparator<String>() {
//            DateFormat f = new SimpleDateFormat("dd/MM/yyyy");
//            @Override
//            public int compare(String o1, String o2) {
//                try {
//                    return f.parse(o1).compareTo(f.parse(o2));
//                } catch (ParseException e) {
//                    throw new IllegalArgumentException(e);
//                }
//            }
//        });


        checkMyAttendance = findViewById(R.id.check_my_attendance);
        tapInTapOut = findViewById(R.id.tap_in_tap_out);
        medicalLeave = findViewById(R.id.medical_leave);
        settings = findViewById(R.id.settings);

        campusAttendance = calculateAttendance(campusAttendanceDaysCheck);
        int totalAttendanceOfClasses = 0;
        for (String key : classAttendanceDaysCheck.keySet()){
            totalAttendanceOfClasses += calculateAttendance(classAttendanceDaysCheck.get(key));
        }
        classAttendance = totalAttendanceOfClasses/classAttendanceDaysCheck.size();

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
                showSettingPop();
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
                Bundle args = new Bundle();
                args.putString("userID", userId);
                medicalLeaveFragment.setArguments(args);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_frag, medicalLeaveFragment, "");
                fragmentTransaction.commit();
            }
        });
    }

    private void newDayUpdateAttendance(HashMap<String, Boolean> attendanceDaysCheck){
        String mostRecentDate = getMostRecentDateString(attendanceDaysCheck);
        if (attendanceDaysCheck.containsKey(currentDateString) &&
                attendanceDaysCheck.containsKey(mostRecentDate)){
            for (LocalDate date = LocalDate.parse(mostRecentDate, formatter).plusDays(1);
                 date.isBefore(currentDate); date = date.plusDays(1)) {
                if (attendanceDaysCheck.get(date.format(formatter)) == null &&
                        attendanceDaysCheck.containsKey(date.format(formatter))) {
                    attendanceDaysCheck.put(date.format(formatter), false);
                }
            }
        }
        else if (!attendanceDaysCheck.containsKey(currentDateString) &&
                attendanceDaysCheck.containsKey(mostRecentDate)){
            for (LocalDate date = LocalDate.parse(mostRecentDate, formatter).plusDays(1);
                 date.isBefore(currentDate); date = date.plusDays(1)) {
                if (attendanceDaysCheck.get(date.format(formatter)) == null &&
                        attendanceDaysCheck.containsKey(date.format(formatter))) {
                    attendanceDaysCheck.put(date.format(formatter), false);
                }
            }
        }
        else if (attendanceDaysCheck.containsKey(currentDateString) &&
                mostRecentDate == null){
            mostRecentDate = getFirstDateOfDaysCheck(attendanceDaysCheck);
            for (LocalDate date = LocalDate.parse(mostRecentDate, formatter);
                 date.isBefore(currentDate); date = date.plusDays(1)) {
                if (attendanceDaysCheck.get(date.format(formatter)) == null &&
                        attendanceDaysCheck.containsKey(date.format(formatter))) {
                    attendanceDaysCheck.put(date.format(formatter), false);
                }
            }
        }
    }

    private String getMostRecentDateString(HashMap<String, Boolean> attendanceDaysCheck){
        ArrayList<String> daysAlreadyCheckAttendance = new ArrayList<>();
        for (String key : attendanceDaysCheck.keySet()){
            if(attendanceDaysCheck.get(key) != null){
                daysAlreadyCheckAttendance.add(key);
            }
        }
        Collections.sort(daysAlreadyCheckAttendance, new Comparator<String>() {
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
        if (daysAlreadyCheckAttendance.size()>0) {
            return daysAlreadyCheckAttendance.get(daysAlreadyCheckAttendance.size() - 1);
        }
        return null;
    }

    private String getFirstDateOfDaysCheck(HashMap<String, Boolean> attendanceDaysCheck){
        ArrayList<String> sortedKeys = new ArrayList<>(attendanceDaysCheck.keySet());
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
        return sortedKeys.get(0);
    }

    private void requestUserInfo() {
        Intent intent = getIntent();
        userId = intent.getStringExtra("UserID");
        attendanceManager.getUser(userId, new AttendanceManager.onGetUserListener() {
            @Override
            public void OnStart() {

            }

            @Override
            public void OnSuccess(User user) {
                userInfo = user;
            }

            @Override
            public void OnFailure() {

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
    public void attendanceChange() {
        upDateMainScreen();
    }

    public void upDateMainScreen(){
        loadHashMap();
//        daysTappedIn = 0;
//        daysTappedInRequired = 0;
//
//        for (String name: campusAttendanceDaysCheck.keySet()){
//            if (campusAttendanceDaysCheck.get(name) != null) {
//                daysTappedInRequired += 1;
//                if (campusAttendanceDaysCheck.get(name)) {
//                    daysTappedIn += 1;
//                }
//            }
//        }

        campusAttendance = calculateAttendance(campusAttendanceDaysCheck);
        int totalAttendanceOfClasses = 0;
        for (String key : classAttendanceDaysCheck.keySet()){
            totalAttendanceOfClasses += calculateAttendance(classAttendanceDaysCheck.get(key));
        }
        classAttendance = totalAttendanceOfClasses/classAttendanceDaysCheck.size();

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
            outputStream.writeObject(classAttendanceDaysCheck);
            outputStream.writeObject(cp3408Lecture);
            outputStream.writeObject(cp3408Practical);
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
            classAttendanceDaysCheck = (HashMap<String, HashMap<String, Boolean>>) inputStream.readObject();
            cp3408Lecture = (HashMap<String, Boolean>) inputStream.readObject();
            cp3408Practical = (HashMap<String, Boolean>) inputStream.readObject();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private int calculateAttendance(HashMap<String, Boolean> daysCheck){
        daysTappedIn = 0;
        daysTappedInRequired = 0;

        for (String name: daysCheck.keySet()){
            if (daysCheck.get(name) != null) {
                daysTappedInRequired += 1;
                if (daysCheck.get(name)) {
                    daysTappedIn += 1;
                }
            }
        }
        return (int) (((float) daysTappedIn/ (float) daysTappedInRequired) * 100);
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
            attendanceManager.tapOut(userIdForTesting, new AttendanceManager.OnTapOutListener() {
                @Override
                public void OnStart() {

                }

                @Override
                public void OnSuccess() {

                }

                @Override
                public void OnFailure() {

                }
            });
            currentDate = LocalDate.now();
            currentDateString = currentDate.format(formatter);
            newDayUpdateAttendance(campusAttendanceDaysCheck);
            for (String key : classAttendanceDaysCheck.keySet()){
                newDayUpdateAttendance(classAttendanceDaysCheck.get(key));
            }
            upDateMainScreen();
        }
    }


    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void sendEmail() {


        Intent data=new Intent(Intent.ACTION_SENDTO);
        data.setData(Uri.parse("mailto:studentservices-singapore@jcu.edu.au"));
        data.putExtra(Intent.EXTRA_SUBJECT, "");
        data.putExtra(Intent.EXTRA_TEXT, "");
        startActivity(data);
    }

    private void showUp(View v, PopupWindow window) {
        v.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int measuredHeight = v.getMeasuredHeight();
        int[] location = new int[2];
        settings.getLocationOnScreen(location);
        window.showAtLocation(settings, Gravity.NO_GRAVITY, 0, location[1] - measuredHeight);
    }


    private void showContactPop() {
        PopupWindow popupWindow = new PopupWindow(this);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);

        View view = LayoutInflater.from(this).inflate(R.layout.pop_contacts, null);
        RelativeLayout phone = view.findViewById(R.id.layout_phone);
        RelativeLayout email = view.findViewById(R.id.layout_email);

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                call("+65 6709 3888");
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                sendEmail();
            }
        });

        popupWindow.setContentView(view);
        popupWindow.setBackgroundDrawable(new ColorDrawable(66000000));
        popupWindow.setOutsideTouchable(true);
        showUp(view, popupWindow);
        popupWindow.update();
    }


    public void showSettingPop() {
        PopupWindow popupWindow = new PopupWindow(this);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);

        View view = LayoutInflater.from(this).inflate(R.layout.pop_setting, null);
        RelativeLayout contacts = view.findViewById(R.id.layout_contacts);

        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                showContactPop();
            }
        });

        popupWindow.setContentView(view);
        popupWindow.setBackgroundDrawable(new ColorDrawable(66000000));
        popupWindow.setOutsideTouchable(true);
        showUp(view, popupWindow);
        popupWindow.update();
    }
}
