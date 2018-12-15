package com.example.arx8l.attendenceapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textView;
    private Button testButton;
    private LoginManager loginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        testButton = (Button) findViewById(R.id.testButton);
        testButton.setOnClickListener(this);
        loginManager = new LoginManager();
    }

    @Override
    public void onClick(View view)
    {
        loginManager.login("tom@my.jcu.edu.au", "Password", new LoginManager.OnLoginListener() {
            @Override
            public void OnStart() {
                textView.setText("Logging in...");
            }

            @Override
            public void OnSuccess() {
                textView.setText("Log in successful");
            }

            @Override
            public void OnFailure() {
                textView.setText("Log in failed");
            }
        });
    }
}
