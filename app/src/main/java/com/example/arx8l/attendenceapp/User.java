package com.example.arx8l.attendenceapp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class User {
    private String userID;
    private String name;
    private String loginID;
    private String password;
    private Boolean isTappedIn;
    private String tapInTime;

    public User(){}

    public User(String userID, String name, String loginID, String password)
    {
        this.userID = userID;
        this.name = name;
        this.loginID = loginID;
        this.password = password;
        this.isTappedIn = false;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginID() {
        return loginID;
    }

    public void setLoginID(String loginID) {
        this.loginID = loginID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getTappedIn() {
        return isTappedIn;
    }

    public void setTappedIn(Boolean tappedIn) {
        isTappedIn = tappedIn;
    }

    public void setTapInTime() {
        this.tapInTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
    }

    public String getTapInTime(){return tapInTime;}
}
