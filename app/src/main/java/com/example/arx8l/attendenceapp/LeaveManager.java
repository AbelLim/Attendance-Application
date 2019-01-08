package com.example.arx8l.attendenceapp;

import android.net.Uri;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LeaveManager
{
    private FileManager fm = new FileManager();

    public LeaveManager(){}

    public void postLeaveApplication(String userID, String certificateNumber, Uri file, String startDate, String endDate, String additionalComment, PostLeaveApplicationListener listener)
    {
        DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference("leave-applications");
        String key = userDatabase.push().getKey();

        fm.uploadFile(file, key, new FileManager.OnFileUploadListener() {
            @Override
            public void OnStart() {
                listener.OnStart();
            }

            @Override
            public void OnSuccess() {
                LeaveApplication application = new LeaveApplication(userID, key, certificateNumber, startDate, endDate, additionalComment);
                if(key!=null)
                    userDatabase.child(key).setValue(application);
                listener.OnSuccess();
            }

            @Override
            public void OnFailure() {
                listener.OnFailure();
            }
        });
    }

    public interface PostLeaveApplicationListener
    {
        void OnStart();
        void OnSuccess();
        void OnFailure();
    }
}