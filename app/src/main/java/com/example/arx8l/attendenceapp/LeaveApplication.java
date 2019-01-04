package com.example.arx8l.attendenceapp;

public class LeaveApplication
{
    private String userID;
    private String key;
    private String certificateNumber;
    private String additionalComment;

    public LeaveApplication(){}

    public LeaveApplication(String userID, String key, String certificateNumber, String additionalComment)
    {
        this.userID = userID;
        this.key = key;
        this.certificateNumber = certificateNumber;
        this.additionalComment = additionalComment;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public String getAdditionalComment() {
        return additionalComment;
    }

    public void setAdditionalComment(String additionalComment) {
        this.additionalComment = additionalComment;
    }
}
