package com.example.arx8l.attendenceapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textView;
    private Button testButton;
    private Database database;
    private String stringer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new Database();
        textView = (TextView) findViewById(R.id.textView);
        testButton = (Button) findViewById(R.id.testButton);
        testButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //Create new user
        /*database.createUser("12345678", "Jim", "Jim@my.jcu.edu.au", "password");*/

        //Login authentication
        /*if(database.isLoginCorrect("Jim@my.jcu.edu.au", "password"))
            textView.setText("Login Correct");
        else
            textView.setText("Login Failed");*/
    }
}
