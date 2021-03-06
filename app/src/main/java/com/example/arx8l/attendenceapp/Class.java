/*This class defines the Class database object. It is used to contain class specific information.
* Code by Tung*/

package com.example.arx8l.attendenceapp;

import java.util.HashMap;

public class Class {
    private String classID;
    private String startTime;
    private String endTime;
    private boolean isUserTappedIn;
    private HashMap<String, String> attendance = new HashMap<>();

    public Class(){

    }

    public Class(String classID, String startTime, String endTime, boolean isUserTappedIn){
        this.classID = classID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isUserTappedIn = isUserTappedIn;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public void setUserTappedIn(boolean userTappedIn) {
        isUserTappedIn = userTappedIn;
    }

    public boolean getUserTappedIn() {
        return isUserTappedIn;
    }

    public HashMap<String, String> getAttendance() {
        return attendance;
    }

    public void setAttendance(HashMap<String, String> attendance) {
        this.attendance = attendance;
    }

    public void putAttendance(String date, String isPresent){
        attendance.put(date, isPresent);
    }
}
