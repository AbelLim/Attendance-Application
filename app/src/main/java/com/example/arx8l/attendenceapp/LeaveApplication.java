package com.example.arx8l.attendenceapp;

public class LeaveApplication
{
    private String userID;
    private String key;
    private String certificateNumber;
    private String startDate;
    private String endDate;
    private String additionalComment;

    public LeaveApplication(){}

    public LeaveApplication(String userID, String key, String certificateNumber, String startDate, String endDate, String additionalComment)
    {
        this.userID = userID;
        this.key = key;
        this.certificateNumber = certificateNumber;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String leaveDate) {
        this.startDate = leaveDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
