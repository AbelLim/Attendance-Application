package com.example.arx8l.attendenceapp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class User {
    private String userID;
    private String userName;
    private String email;
    private String password;
    private Boolean isTappedIn;
    private String tapInTime;

    public User(String userID, String name, String email, String password)
    {
        this.userID = userID;
        this.userName = name;
        this.email = email;
        this.password = password;
        this.isTappedIn = false;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getuserName() {
        return userName;
    }

    public void setuserName(String name) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
