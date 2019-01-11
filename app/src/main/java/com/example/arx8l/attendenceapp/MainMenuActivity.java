/*This activity contains the bottom navigation menu, background image and the middle container that can be switched between different screens
* Code by Tung*/

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

    //Contain user data to pass around different fragments
    private Bundle userDataBundle;
    private int classAttendancePercentage;
    private int campusAttendancePercentage;
    private boolean isNewDay = false;

    //Key is date in string, value is "True", "False" or "Null"
    private HashMap<String, String> campusAttendance;

    //Key is classID, value is HashMap with same format to campusAttendance
    private HashMap<String, HashMap<String, String>> classAttendance;
    private LocalDate currentDate;
    private String currentDateString;
    private String userId;
    private AlarmManager alarmManager;
    
    ImageView settings;
    ImageView tapInTapOut;
    ImageView checkMyAttendance;
    ImageView medicalLeave;

    AttendanceManager attendanceManager;
    PendingIntent notificationPendingIntent;
    PendingIntent notificationPendingIntent1;
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //Create required notification channel that allows the app to push out notifications
        createNotificationChannel();
        getSupportActionBar().hide();

        campusAttendance = new HashMap<String, String>();
        classAttendance = new HashMap<String, HashMap<String, String>>();

        attendanceManager = new AttendanceManager();
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //Get user ID from Login Activity
        Intent intent = getIntent();
        userId = intent.getStringExtra("UserID");

        registerReceiver(mDateChangeReceiver, new IntentFilter(Intent.ACTION_DATE_CHANGED));

        currentDate = LocalDate.now();
        currentDateString = currentDate.format(dateFormatter);

        attendanceManager.getUser(userId, new AttendanceManager.onGetUserListener() {
            @Override
            public void OnStart() {

            }

            @Override
            public void OnSuccess(User user) {
                //Update user attendance when it comes to the new day
                newDayUpdateAttendance(user.getCampusAttendance());
                for (Class userClass : user.getClasses()){
                    newDayUpdateAttendance(userClass.getAttendance());
                }

                campusAttendance = user.getCampusAttendance();
                for (Class userClass : user.getClasses()){
                    classAttendance.put(userClass.getClassID(), userClass.getAttendance());
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

                campusAttendancePercentage = calculateAttendancePercentage(campusAttendance);
                int totalAttendanceOfClasses = 0;
                for (String key : classAttendance.keySet()){
                    totalAttendanceOfClasses += calculateAttendancePercentage(classAttendance.get(key));
                }
                classAttendancePercentage = totalAttendanceOfClasses/classAttendance.size();

                userDataBundle = new Bundle();
                userDataBundle.putString("user name", user.getName());
                userDataBundle.putString("user id", userId);
                userDataBundle.putInt("class attendance", classAttendancePercentage);
                userDataBundle.putInt("campus attendance", campusAttendancePercentage);
                userDataBundle.putSerializable("class days check", classAttendance);

                //Delay 1s to wait for FireBase finishes updating user data
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MainScreenFragment mainScreenFragment = new MainScreenFragment();
                        mainScreenFragment.setArguments(userDataBundle);
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
                checkMyAttendanceFragment.setArguments(userDataBundle);
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
                Bundle args = new Bundle();
                args.putString("userID", userId);
                medicalLeaveFragment.setArguments(args);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_frag, medicalLeaveFragment, "").addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    private void newDayUpdateAttendance(HashMap<String, String> attendance){
        String mostRecentDate = getMostRecentDateString(attendance);
        if (attendance.containsKey(currentDateString) &&
                attendance.containsKey(mostRecentDate)){
            for (LocalDate date = LocalDate.parse(mostRecentDate, dateFormatter).plusDays(1);
                 date.isBefore(currentDate); date = date.plusDays(1)) {
                if ( attendance.containsKey(date.format(dateFormatter))) {
                    if (attendance.get(date.format(dateFormatter)).equals("Null")) {
                        attendance.put(date.format(dateFormatter), "False");
                        isNewDay = true;
                    }
                }
            }
        }
        else if (!attendance.containsKey(currentDateString) &&
                attendance.containsKey(mostRecentDate)){
            for (LocalDate date = LocalDate.parse(mostRecentDate, dateFormatter).plusDays(1);
                 date.isBefore(currentDate); date = date.plusDays(1)) {
                if (attendance.containsKey(date.format(dateFormatter))) {
                    if (attendance.get(date.format(dateFormatter)).equals("Null")) {
                        attendance.put(date.format(dateFormatter), "False");
                        isNewDay = true;
                    }
                }
            }
        }
        else if (attendance.containsKey(currentDateString) &&
                mostRecentDate == null){
            mostRecentDate = getFirstDateOfDaysCheck(attendance);
            for (LocalDate date = LocalDate.parse(mostRecentDate, dateFormatter);
                 date.isBefore(currentDate); date = date.plusDays(1)) {
                if ( attendance.containsKey(date.format(dateFormatter))) {
                    if (attendance.get(date.format(dateFormatter)).equals("Null")) {
                        attendance.put(date.format(dateFormatter), "False");
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



    // Create the NotificationChannel
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Test Channel";
            String description = "This is only for testing";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("testChannel", name, importance);
            channel.setDescription(description);
            // Register the channel with the system
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
        unregisterReceiver(mDateChangeReceiver);
    }

    @Override
    public void attendanceChange() {
        upDateMainScreen();
    }

    //Update the campus and class attendance circle progress bars in the main screen
    public void upDateMainScreen(){
        //Get user data from database
        attendanceManager.getUser(userId, new AttendanceManager.onGetUserListener() {
            @Override
            public void OnStart() {

            }

            @Override
            public void OnSuccess(User user) {
                campusAttendance = user.getCampusAttendance();
                for (Class userClass : user.getClasses()){
                    classAttendance.put(userClass.getClassID(), userClass.getAttendance());
                }

                campusAttendancePercentage = calculateAttendancePercentage(campusAttendance);
                int totalAttendanceOfClasses = 0;
                for (String key : classAttendance.keySet()){
                    totalAttendanceOfClasses += calculateAttendancePercentage(classAttendance.get(key));
                }
                classAttendancePercentage = totalAttendanceOfClasses/classAttendance.size();

                userDataBundle.putInt("class attendance", classAttendancePercentage);
                userDataBundle.putInt("campus attendance", campusAttendancePercentage);

                MainScreenFragment mainScreenFragment = new MainScreenFragment();
                mainScreenFragment.setArguments(userDataBundle);
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

    private int calculateAttendancePercentage(HashMap<String, String> attendance){
        int daysTappedIn = 0;
        int daysTappedInRequired = 0;

        for (String name: attendance.keySet()){
            if (!attendance.get(name).equals("Null")) {
                daysTappedInRequired += 1;
                if (attendance.get(name).equals("True")) {
                    daysTappedIn += 1;
                }
            }
        }
        return (int) (((float) daysTappedIn/ (float) daysTappedInRequired) * 100);
    }

    private final MyDateChangeReceiver mDateChangeReceiver = new MyDateChangeReceiver();

    @Override
    public void passNotificationPendingIntents(ArrayList<PendingIntent> notificationPendingIntents) {
        notificationPendingIntent = notificationPendingIntents.get(0);
        notificationPendingIntent1 = notificationPendingIntents.get(1);
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
                    currentDateString = currentDate.format(dateFormatter);
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
                if (notificationPendingIntent != null && notificationPendingIntent1 != null){
                    notificationPendingIntent.cancel();
                    notificationPendingIntent1.cancel();
                    alarmManager.cancel(notificationPendingIntent);
                    alarmManager.cancel(notificationPendingIntent1);
                }
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
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
