package com.example.arx8l.attendenceapp;

/**
 * Created by User on 31/12/2018.
 */

public class Class {
    private String name;
    private String startTime;
    private String endTime;

    public Class(String name, String startTime, String endTime){
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getName() {
        return name;
    }
}
