/*This class defines the LoginManager entity. It is used to store methods related to the login activity.
* Code by Abel.*/
package com.example.arx8l.attendenceapp;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class LoginManager
{
    private List<User> userList = new ArrayList<>();
    private Database database = new Database();

    //Constructor
    public LoginManager(){}

    //Handles login process
    public void login(String loginID, final String password, final OnLoginListener listener)
    {
        //Creates query from loginID
        final DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference("users");
        Query query = userDatabase.orderByChild("loginID").equalTo(loginID);
        //Request data screenshot from database
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

                //Extract password from user data
                String passwordDatabase = userList.get(0).getPassword();
                User mUser = userList.get(0);
                String hPassword = database.hashPassword(password);

                //Compare hashed password against database's password
                if(passwordDatabase.equals(hPassword))
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

    //Listener
    public interface OnLoginListener
    {
        void OnStart();
        void OnSuccess(User user);
        void OnFailure();
    }
}
