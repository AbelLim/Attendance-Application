/*This class defines the AttendanceManager entity. It is used to hold methods related to the Tap in/ Tap out process as well as the view attendance process.
* Code by Abel*/
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

    //Constructor
    public AttendanceManager(){}

    //Handles tap in process
    public void tapIn(String userID, final OnTapInListener listener)
    {
        //Creates query based on userID
        final DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference("users");
        Query query = userDatabase.orderByChild("userID").equalTo(userID);
        //Request data snapshot from database
        database.readData(query, new Database.OnGetDataListener() {
            @Override
            public void OnStart() {
                listener.OnStart();
            }

            @Override
            public void OnSuccess(DataSnapshot snapshot) {
                //Extract user data from data snapshot
                userList.clear();
                String key=null;
                for(DataSnapshot userSnapshot : snapshot.getChildren())
                {
                    if(userList.isEmpty())
                        key = userSnapshot.getKey();
                    User user = userSnapshot.getValue(User.class);
                    userList.add(user);
                }

                //Sets tap in status to true and tap in time to current time
                userList.get(0).setTapInTime();
                userList.get(0).setTappedIn(true);

                //Push updated user data back to database
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

    //Handles tap out process
    public void tapOut(String userID, final OnTapOutListener listener)
    {
        //Creates query based on userID
        final DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference("users");
        Query query = userDatabase.orderByChild("userID").equalTo(userID);
        //Request data snapshot from database
        database.readData(query, new Database.OnGetDataListener() {
            @Override
            public void OnStart() {
                listener.OnStart();
            }

            @Override
            public void OnSuccess(DataSnapshot snapshot) {
                //Extract user data from data snapshot
                userList.clear();
                String key=null;
                for(DataSnapshot userSnapshot : snapshot.getChildren())
                {
                    if(userList.isEmpty())
                        key = userSnapshot.getKey();
                    User user = userSnapshot.getValue(User.class);
                    userList.add(user);
                }
                //Sets tap in status to false
                userList.get(0).setTappedIn(false);
                //Push updated user data back to database
                if(key!=null) {
                    userDatabase.child(key).setValue(userList.get(0));

                    //Handle tap out actions in listener
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

    //Returns user data
    public void getUser(String userID, final onGetUserListener listener)
    {
        //Creates query based on userID
        final DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference("users");
        Query query = userDatabase.orderByChild("userID").equalTo(userID);
        //Request data snapshot from database
        database.readData(query, new Database.OnGetDataListener() {
            @Override
            public void OnStart() {
                listener.OnStart();
            }

            @Override
            public void OnSuccess(DataSnapshot snapshot) {
                //Extract user data from data snapshot
                userList.clear();
                for(DataSnapshot userSnapshot : snapshot.getChildren())
                {
                    User user = userSnapshot.getValue(User.class);
                    userList.add(user);
                }
                User mUser = userList.get(0);
                //Push user data to listener
                if(mUser!=null)
                    //Handle actions in listener
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

    //Push user data to database
    public void updateUser(String userID, User user)
    {
        //Creates query based on userID
        final DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference("users");
        Query query = userDatabase.orderByChild("userID").equalTo(userID);
        //Request data snapshot from database
        database.readData(query, new Database.OnGetDataListener() {
            @Override
            public void OnStart() {}

            @Override
            public void OnSuccess(DataSnapshot snapshot) {
                //Extract current user data from data snapshot
                userList.clear();
                String key=null;
                for(DataSnapshot userSnapshot : snapshot.getChildren())
                {
                    if(userList.isEmpty())
                        //Find key of current user data
                        key = userSnapshot.getKey();
                    userList.add(user);
                }
                if(key!=null) {
                    //Push user data back to database
                    userDatabase.child(key).setValue(user);
                }
            }

            @Override
            public void OnFailure() {}
        });
    }

    //Listeners
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
