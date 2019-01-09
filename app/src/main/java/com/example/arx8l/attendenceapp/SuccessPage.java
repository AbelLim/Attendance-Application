package com.example.arx8l.attendenceapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SuccessPage extends AppCompatActivity {

    TextView message;
    RelativeLayout successLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success_layout);
        getSupportActionBar().hide();

        message = findViewById(R.id.message_text);
        successLayout = findViewById(R.id.success_layout);

        successLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        String mess = getIntent().getStringExtra("message");
        message.setText(mess);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 3000);

    }
}
