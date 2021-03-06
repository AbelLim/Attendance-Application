/*This class defines the User database object. It is used to contain user specific information.
* Code by Abel and Tung*/
package com.example.arx8l.attendenceapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class User {
    private String userID;
    private String name;
    private String loginID;
    private String password;
    private Boolean isTappedIn;
    private Boolean isStudent;

    private String tapInTime;
    private HashMap<String, String> campusAttendance = new HashMap<>();
    private ArrayList<Class> classes = new ArrayList<>();

    //Constructors
    public User(){}

    public User(String userID, String name, String loginID, String password)
    {
        this.userID = userID;
        this.name = name;
        this.loginID = loginID;
        this.password = password;
        this.isTappedIn = false;
    }

    

    //This function appends a date and boolean to the HashMap used to track the user's attendance.
    public void putCampusAttendance(String date, String isPresent)
    {
        String mDate = date.replace('/','-');
        campusAttendance.put(mDate, isPresent);
    }

    //Getters and Setters
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

    //This setter sets the tap in time to the current date and time.
    public void setTapInTime() {
        this.tapInTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public String getTapInTime(){return tapInTime;}

    public HashMap<String, String> getCampusAttendance() {
        return campusAttendance;
    }

    public void setCampusAttendance(HashMap<String, String> campusAttendance) {
        this.campusAttendance = campusAttendance;
    }

    public ArrayList<Class> getClasses() {
        return classes;
    }

    public void setClasses(ArrayList<Class> classes) {
        this.classes = classes;
    }

    public void putClass(Class userClass){
        classes.add(userClass);
    }

    public Boolean getIsStudent() {
        return isStudent;
    }

    public void setIsStudent(Boolean isStudent) {
        this.isStudent = isStudent;
    }
}
