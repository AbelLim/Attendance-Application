package com.example.arx8l.attendenceapp;

import android.*;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
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
import android.widget.ProgressBar;
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
        DetailClassAttendanceFragment.OnFragmentInteractionListener, MainScreenFragment.OnRegisterNotificationListener{

    private Bundle attendanceBundle;
    private int classAttendance;
    private int campusAttendance;
    private boolean isNewDay = false;
    private HashMap<String, String> campusAttendanceDaysCheck;
    private HashMap<String, HashMap<String, String>> classAttendanceDaysCheck;
    private HashMap<String, String> cp3408Lecture;
    private HashMap<String, String> cp3408Practical;
    private int daysTappedIn;
    private int daysTappedInRequired;
    private String studyPeriodStartDateInString = "01-12-2018";
    private String studyPeriodEndDateInString = "01-02-2019";
    private String startDate = "05/11/2018";
    private String endDate = "02/02/2019";
    private String startDateInStringCP3408L = "05-12-2018";
    private String endDateInStringCP3408L = "06-02-2019";
    private String startDateInStringCP3408P = "06-12-2018";
    private String endDateInStringCP3408P = "07-02-2019";
    private LocalDate currentDate;
    private String currentDateString;
    private String userIdForTesting = "12345678";
    private String userId;
    private User userInfo;
    private AlarmManager alarmManager;

    
    ImageView settings;
    ImageView tapInTapOut;
    ImageView checkMyAttendance;
    ImageView medicalLeave;

    AttendanceManager attendanceManager;
    PendingIntent pendingIntent;
    PendingIntent pendingIntent1;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        createNotificationChannel();
        getSupportActionBar().hide();

        campusAttendanceDaysCheck = new HashMap<String, String>();
        classAttendanceDaysCheck = new HashMap<String, HashMap<String, String>>();
        cp3408Lecture = new HashMap<String, String>();
        cp3408Practical = new HashMap<String, String>();


        attendanceManager = new AttendanceManager();
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //Get User info from server.
        requestUserInfo();

        registerReceiver(mDateReceiver, new IntentFilter(Intent.ACTION_DATE_CHANGED));

        currentDate = LocalDate.now();
        currentDateString = currentDate.format(formatter);

//        LocalDate startDate = LocalDate.parse(studyPeriodStartDateInString, formatter);
//        LocalDate endDate = LocalDate.parse(studyPeriodEndDateInString, formatter);
//        LocalDate startDateCP3408L = LocalDate.parse(startDateInStringCP3408L, formatter);
//        LocalDate endDateCP3408L = LocalDate.parse(endDateInStringCP3408L, formatter);
//        LocalDate startDateCP3408P = LocalDate.parse(startDateInStringCP3408P, formatter);
//        LocalDate endDateCP3408P = LocalDate.parse(endDateInStringCP3408P, formatter);

//        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
//            if(!date.getDayOfWeek().name().equals("SATURDAY") && !date.getDayOfWeek().name().equals("SUNDAY")) {
//                campusAttendanceDaysCheck.put(date.format(formatter), "Null");
//            }
//            System.out.println("Hey Hey Hey");
//        }
//
//        for (LocalDate date = startDate; date.isBefore(currentDate); date = date.plusDays(1))
//        {
//            if(!date.getDayOfWeek().name().equals("SATURDAY") && !date.getDayOfWeek().name().equals("SUNDAY")) {
//                int randNum = new Random().nextInt(2);
//                if (randNum == 0) {
//                    campusAttendanceDaysCheck.put(date.format(formatter), "False");
//                } else if (randNum == 1) {
//                    campusAttendanceDaysCheck.put(date.format(formatter), "True");
//                }
//            }
//        }

//        ArrayList<String> sortedKeys2 =
//                new ArrayList<String>(cp3408Lecture.keySet());
//        Collections.sort(sortedKeys2, new Comparator<String>() {
//            DateFormat f = new SimpleDateFormat("dd-MM-yyyy");
//            @Override
//            public int compare(String o1, String o2) {
//                try {
//                    return f.parse(o1).compareTo(f.parse(o2));
//                } catch (ParseException e) {
//                    throw new IllegalArgumentException(e);
//                }
//            }
//        });
//
//        for (String key: sortedKeys2){
//            System.out.println(key + ": " + cp3408Lecture.get(key));
//        }

        attendanceManager.getUser(userId, new AttendanceManager.onGetUserListener() {
            @Override
            public void OnStart() {

            }

            @Override
            public void OnSuccess(User user) {
                newDayUpdateAttendance(user.getCampusAttendance());
                for (Class userClass : user.getClasses()){
                    newDayUpdateAttendance(userClass.getAttendance());
                }
//                user.getCampusAttendance().put("07-01-2019", "Null");
                user.getCampusAttendance().put("08-01-2019", "Null");
                user.setTappedIn(false);
//                user.getCampusAttendance().put("09-01-2019", "Null");
//                user.getClasses().get(0).putAttendance("09-01-2019", "Null");
//                user.getClasses().get(1).putAttendance("10-01-2019", "Null");

                campusAttendanceDaysCheck = user.getCampusAttendance();
                for (Class userClass : user.getClasses()){
                    classAttendanceDaysCheck.put(userClass.getClassID(), userClass.getAttendance());
                }

                if(isNewDay){
                    attendanceManager.tapOut(userId, new AttendanceManager.OnTapOutListener() {
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

                    for (Class userClass : user.getClasses()){
                        userClass.setUserTappedIn(false);
                    }
                    isNewDay = false;
                }

                attendanceManager.updateUser(userId, user);

                campusAttendance = calculateAttendance(campusAttendanceDaysCheck);
                int totalAttendanceOfClasses = 0;
                for (String key : classAttendanceDaysCheck.keySet()){
                    totalAttendanceOfClasses += calculateAttendance(classAttendanceDaysCheck.get(key));
                }
                classAttendance = totalAttendanceOfClasses/classAttendanceDaysCheck.size();

                attendanceBundle = new Bundle();
                attendanceBundle.putString("user name", user.getName());
                attendanceBundle.putString("user id", userId);
                attendanceBundle.putInt("class attendance", classAttendance);
                attendanceBundle.putInt("campus attendance", campusAttendance);
                attendanceBundle.putSerializable("class days check", classAttendanceDaysCheck);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MainScreenFragment mainScreenFragment = new MainScreenFragment();
                        mainScreenFragment.setArguments(attendanceBundle);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.add(R.id.main_frag, mainScreenFragment, "");
                        fragmentTransaction.commit();                    }
                }, 1000);
            }

            @Override
            public void OnFailure() {

            }
        });

//        campusAttendanceDaysCheck.put("01/01/2019", null);
//        campusAttendanceDaysCheck.put("02/01/2019", null);
//        classAttendanceDaysCheck.get("CP3408-Lecture").put("02/01/2019" , null);
//        classAttendanceDaysCheck.get("CP3408-Practical").put("03/01/2019" , null);


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

//        saveHashMap();

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
                checkMyAttendanceFragment.setArguments(attendanceBundle);
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
                fragmentManager.popBackStack();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_frag, medicalLeaveFragment, "").addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    private void newDayUpdateAttendance(HashMap<String, String> attendanceDaysCheck){
        String mostRecentDate = getMostRecentDateString(attendanceDaysCheck);
        if (attendanceDaysCheck.containsKey(currentDateString) &&
                attendanceDaysCheck.containsKey(mostRecentDate)){
            for (LocalDate date = LocalDate.parse(mostRecentDate, formatter).plusDays(1);
                 date.isBefore(currentDate); date = date.plusDays(1)) {
                if ( attendanceDaysCheck.containsKey(date.format(formatter))) {
                    if (attendanceDaysCheck.get(date.format(formatter)).equals("Null")) {
                        attendanceDaysCheck.put(date.format(formatter), "False");
                        isNewDay = true;
                    }
                }
            }
        }
        else if (!attendanceDaysCheck.containsKey(currentDateString) &&
                attendanceDaysCheck.containsKey(mostRecentDate)){
            for (LocalDate date = LocalDate.parse(mostRecentDate, formatter).plusDays(1);
                 date.isBefore(currentDate); date = date.plusDays(1)) {
                if (attendanceDaysCheck.containsKey(date.format(formatter))) {
                    if (attendanceDaysCheck.get(date.format(formatter)).equals("Null")) {
                        attendanceDaysCheck.put(date.format(formatter), "False");
                        isNewDay = true;
                    }
                }
            }
        }
        else if (attendanceDaysCheck.containsKey(currentDateString) &&
                mostRecentDate == null){
            mostRecentDate = getFirstDateOfDaysCheck(attendanceDaysCheck);
            for (LocalDate date = LocalDate.parse(mostRecentDate, formatter);
                 date.isBefore(currentDate); date = date.plusDays(1)) {
                if ( attendanceDaysCheck.containsKey(date.format(formatter))) {
                    if (attendanceDaysCheck.get(date.format(formatter)).equals("Null")) {
                        attendanceDaysCheck.put(date.format(formatter), "False");
                        isNewDay = true;
                    }
                }
            }
        }
    }

    private String getMostRecentDateString(HashMap<String, String> attendanceDaysCheck){
        ArrayList<String> daysAlreadyCheckAttendance = new ArrayList<>();
        for (String key : attendanceDaysCheck.keySet()){
            if(!attendanceDaysCheck.get(key).equals("Null")){
                daysAlreadyCheckAttendance.add(key);
            }
        }
        Collections.sort(daysAlreadyCheckAttendance, new Comparator<String>() {
            DateFormat f = new SimpleDateFormat("dd-MM-yyyy");
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

    private String getFirstDateOfDaysCheck(HashMap<String, String> attendanceDaysCheck){
        ArrayList<String> sortedKeys = new ArrayList<>(attendanceDaysCheck.keySet());
        Collections.sort(sortedKeys, new Comparator<String>() {
            DateFormat f = new SimpleDateFormat("dd-MM-yyyy");
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

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Test Channel";
            String description = "This is only for testing";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("testChannel", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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
        attendanceManager.getUser(userId, new AttendanceManager.onGetUserListener() {
            @Override
            public void OnStart() {

            }

            @Override
            public void OnSuccess(User user) {
                campusAttendanceDaysCheck = user.getCampusAttendance();
                for (Class userClass : user.getClasses()){
                    classAttendanceDaysCheck.put(userClass.getClassID(), userClass.getAttendance());
                }

                campusAttendance = calculateAttendance(campusAttendanceDaysCheck);
                int totalAttendanceOfClasses = 0;
                for (String key : classAttendanceDaysCheck.keySet()){
                    totalAttendanceOfClasses += calculateAttendance(classAttendanceDaysCheck.get(key));
                }
                classAttendance = totalAttendanceOfClasses/classAttendanceDaysCheck.size();

                attendanceBundle.putInt("class attendance", classAttendance);
                attendanceBundle.putInt("campus attendance", campusAttendance);

                MainScreenFragment mainScreenFragment = new MainScreenFragment();
                mainScreenFragment.setArguments(attendanceBundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_frag, mainScreenFragment, "");
                fragmentTransaction.commitAllowingStateLoss();
            }

            @Override
            public void OnFailure() {

            }
        });

    }


    private int calculateAttendance(HashMap<String, String> daysCheck){
        daysTappedIn = 0;
        daysTappedInRequired = 0;

        for (String name: daysCheck.keySet()){
            if (!daysCheck.get(name).equals("Null")) {
                daysTappedInRequired += 1;
                if (daysCheck.get(name).equals("True")) {
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

    @Override
    public void passPendingIntents(ArrayList<PendingIntent> pendingIntents) {
        pendingIntent = pendingIntents.get(0);
        pendingIntent1 = pendingIntents.get(1);
    }

    public class MyDateChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, Intent intent) {
            attendanceManager.getUser(userId, new AttendanceManager.onGetUserListener() {
                @Override
                public void OnStart() {

                }

                @Override
                public void OnSuccess(User user) {
                    attendanceManager.tapOut(userId, new AttendanceManager.OnTapOutListener() {
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
                    newDayUpdateAttendance(user.getCampusAttendance());
                    for (Class userClass : user.getClasses()){
                        newDayUpdateAttendance(userClass.getAttendance());
                        userClass.setUserTappedIn(false);
                    }
                    attendanceManager.updateUser(userId, user);

                    isNewDay = false;

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            upDateMainScreen();
                        }
                    }, 1000);
                }

                @Override
                public void OnFailure() {

                }
            });
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
        RelativeLayout logout = view.findViewById(R.id.layout_log_out);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pendingIntent!=null && pendingIntent1 != null){
                    pendingIntent.cancel();
                    pendingIntent1.cancel();
                    alarmManager.cancel(pendingIntent);
                    alarmManager.cancel(pendingIntent1);
                }
            }
        });

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
