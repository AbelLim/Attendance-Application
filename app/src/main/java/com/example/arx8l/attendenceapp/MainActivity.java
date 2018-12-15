package com.example.arx8l.attendenceapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textView;
    private Button testButton;
    private LoginManager loginManager = new LoginManager();
    private AttendanceManager attendanceManager = new AttendanceManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        testButton = (Button) findViewById(R.id.testButton);
        testButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {

    }

    //THE FOLLOWING ARE TEST FUNCTIONS RELATED TO THE DATABASE

    private void login()
    {
        loginManager.login("tom@my.jcu.edu.au", "Password", new LoginManager.OnLoginListener() {
            @Override
            public void OnStart() {
                textView.setText("Logging in...");
            }

            @Override
            public void OnSuccess(String userID) {
                textView.setText("Log in successful");
            }

            @Override
            public void OnFailure() {
                textView.setText("Log in failed");
            }
        });
    }

    private void tapIn()
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

    private void tapOut()
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

    private void getAttendance()
    {
        attendanceManager.getAttendance("51235434", new AttendanceManager.OnReceiveAttendanceListener() {
            @Override
            public void OnStart() {
                textView.setText("Getting attendance...");
            }

            @Override
            public void OnSuccess(String timestamp, boolean isTappedIn) {
                textView.setText(timestamp + " : " + isTappedIn);
            }

            @Override
            public void OnFailure() {
                textView.setText("Get attendance failed");
            }
        });
    }
}
