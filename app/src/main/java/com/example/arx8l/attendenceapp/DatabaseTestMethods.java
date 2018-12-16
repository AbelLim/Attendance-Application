package com.example.arx8l.attendenceapp;

import android.widget.TextView;

public class DatabaseTestMethods {
    private TextView textView;
    private Database database = new Database();
    private LoginManager loginManager = new LoginManager();
    private AttendanceManager attendanceManager = new AttendanceManager();

    public DatabaseTestMethods(TextView textView)
    {
        this.textView = textView;
    }

    //THE FOLLOWING ARE TEST FUNCTIONS RELATED TO THE DATABASE

    public void createUser()
    {
        database.createUser("51235434", "Tom", "tom@my.jcu.edu.au", "Password");
    }

    public void login()
    {
        loginManager.login("tom@my.jcu.edu.au", "Password", new LoginManager.OnLoginListener() {
            @Override
            public void OnStart() {
                textView.setText("Logging in...");
            }

            @Override
            public void OnSuccess(User user) {
                textView.setText("Log in successful: " + user.getName() + " : " + user.getUserID());
            }

            @Override
            public void OnFailure() {
                textView.setText("Log in failed");
            }
        });
    }

    public void tapIn()
    {
        attendanceManager.tapIn("51235434", new AttendanceManager.OnTapInListener() {
            @Override
            public void OnStart() {
                textView.setText("Tapping in...");
            }

            @Override
            public void OnSuccess() {
                textView.setText("Tap in successful");
            }

            @Override
            public void OnFailure() {
                textView.setText("Tap in failed");
            }
        });
    }

    public void tapOut()
    {
        attendanceManager.tapOut("51235434", new AttendanceManager.OnTapOutListener() {
            @Override
            public void OnStart() {
                textView.setText("Tapping out...");
            }

            @Override
            public void OnSuccess() {
                textView.setText("Tap out successful");
            }

            @Override
            public void OnFailure() {
                textView.setText("Tap out failed");
            }
        });
    }

    public void getUser()
    {
        attendanceManager.getUser("51235434", new AttendanceManager.onGetUserListener() {
            @Override
            public void OnStart() {
                textView.setText("Getting attendance...");
            }

            @Override
            public void OnSuccess(User user) {
                textView.setText("Hello " + user.getName() + ". You last tapped in on " + user.getTapInTime());
            }

            @Override
            public void OnFailure() {
                textView.setText("Get attendance failed");
            }
        });
    }
}
