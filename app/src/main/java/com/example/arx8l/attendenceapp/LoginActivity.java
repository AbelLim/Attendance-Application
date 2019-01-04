package com.example.arx8l.attendenceapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 登录
 * @author  Lijinfeng
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etName;
    private EditText etPwd;
    private Button btLogin;
    private LoginManager mLogin = new LoginManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        etName = findViewById(R.id.et_name);
        etPwd = findViewById(R.id.et_pwd);
        btLogin = findViewById(R.id.bt_login);
        btLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String userLogin = etName.getText().toString();
        String userPassword = etPwd.getText().toString();

        if(userLogin == null || userPassword == null)
            inputFieldEmptyError();
        else
            login(userLogin, userPassword);
    }

    private void login(String login, String password)
    {
        mLogin.login(login, password, new LoginManager.OnLoginListener() {
            @Override
            public void OnStart() {
                Log.e("TAG", "-------OnStart");
                Toast.makeText(LoginActivity.this, "Logging in...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnSuccess(User user) {
                Log.e("TAG", "-------OnSuccess");
                Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                intent.putExtra("UserID", user.getUserID());
                startActivity(intent);
            }

            @Override
            public void OnFailure() {
                Log.e("TAG", "-------OnFailure");
                loginFailedError();
            }
        });
    }

    private void inputFieldEmptyError()
    {
        Toast.makeText(this, "Please input your user ID and Password", Toast.LENGTH_SHORT).show();
    }

    private void loginFailedError()
    {
        Toast.makeText(this, "Unable to log in. Please try again later.", Toast.LENGTH_SHORT).show();
    }
}
