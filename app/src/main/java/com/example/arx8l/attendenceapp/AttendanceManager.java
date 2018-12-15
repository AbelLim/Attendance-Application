package com.example.arx8l.attendenceapp;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class AttendanceManager {
    private List<User> userList = new ArrayList<>();
    private Database database = new Database();

    public AttendanceManager(){}

    public void tapIn(String userID, final OnTapInListener listener)
    {
        final DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference("users");
        Query query = userDatabase.orderByChild("userID").equalTo(userID);
        database.readData(query, new Database.OnGetDataListener() {
            @Override
            public void OnStart() {
                listener.OnStart();
            }

            @Override
            public void OnSuccess(DataSnapshot snapshot) {
                userList.clear();
                String key=null;
                for(DataSnapshot userSnapshot : snapshot.getChildren())
                {
                    if(userList.isEmpty())
                        key = userSnapshot.getKey();
                    User user = userSnapshot.getValue(User.class);
                    userList.add(user);
                }
                userList.get(0).setTapInTime();
                userList.get(0).setTappedIn(true);
                if(key!=null) {
                    userDatabase.child(key).setValue(userList.get(0));
                    listener.OnSuccess();
                }
                else
                    listener.OnFailure();

            }

            @Override
            public void OnFailure() {
                listener.OnFailure();
            }
        });
    }

    public void tapOut(String userID, final OnTapOutListener listener)
    {
        final DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference("users");
        Query query = userDatabase.orderByChild("userID").equalTo(userID);
        database.readData(query, new Database.OnGetDataListener() {
            @Override
            public void OnStart() {
                listener.OnStart();
            }

            @Override
            public void OnSuccess(DataSnapshot snapshot) {
                userList.clear();
                String key=null;
                for(DataSnapshot userSnapshot : snapshot.getChildren())
                {
                    if(userList.isEmpty())
                        key = userSnapshot.getKey();
                    User user = userSnapshot.getValue(User.class);
                    userList.add(user);
                }
                userList.get(0).setTappedIn(false);
                if(key!=null) {
                    userDatabase.child(key).setValue(userList.get(0));
                    listener.OnSuccess();
                }
                else
                    listener.OnFailure();
            }

            @Override
            public void OnFailure() {
                listener.OnFailure();
            }
        });
    }

    public void getUser(String userID, final onGetUserListener listener)
    {
        final DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference("users");
        Query query = userDatabase.orderByChild("userID").equalTo(userID);
        database.readData(query, new Database.OnGetDataListener() {
            @Override
            public void OnStart() {
                listener.OnStart();
            }

            @Override
            public void OnSuccess(DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot userSnapshot : snapshot.getChildren())
                {
                    User user = userSnapshot.getValue(User.class);
                    userList.add(user);
                }
                User mUser = userList.get(0);
                if(mUser!=null)
                    listener.OnSuccess(mUser);
                else
                    listener.OnFailure();
            }

            @Override
            public void OnFailure() {
                listener.OnFailure();
            }
        });
    }

    public interface onGetUserListener
    {
        void OnStart();
        void OnSuccess(User user);
        void OnFailure();
    }

    public interface OnTapInListener
    {
        void OnStart();
        void OnSuccess();
        void OnFailure();
    }

    public interface OnTapOutListener
    {
        void OnStart();
        void OnSuccess();
        void OnFailure();
    }
}
