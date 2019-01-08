/*This class defines the LeaveManager entity. It is used to hold methods related to the submission of LeaveApplications.
* Code by Abel*/
package com.example.arx8l.attendenceapp;

import android.net.Uri;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LeaveManager
{
    private FileManager fm = new FileManager();

    //Constructor
    public LeaveManager(){}

    //Push leave application to the Firebase database
    public void postLeaveApplication(String userID, String certificateNumber, Uri file, String startDate, String endDate, String additionalComment, PostLeaveApplicationListener listener)
    {
        //Create a new database reference key
        DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference("leave-applications");
        String key = userDatabase.push().getKey();

        //Upload image file to database under reference key
        fm.uploadFile(file, key, new FileManager.OnFileUploadListener() {
            @Override
            public void OnStart() {
                listener.OnStart();
            }

            @Override
            public void OnSuccess() {
                //Push leave application to database under reference key
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

    //Listener
    public interface PostLeaveApplicationListener
    {
        void OnStart();
        void OnSuccess();
        void OnFailure();
    }
}