package com.example.arx8l.attendenceapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.List;

public class Database
{
    private List<User> userList = new ArrayList<>();
    DatabaseReference databaseUsers;

    public Database()
    {
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
    }

    public void createUser(String userID, String username, String email, String password)
    {
        User user = new User(userID, username, email, password);
        String key = databaseUsers.push().getKey();
        databaseUsers.child(key).setValue(user);
    }

    public boolean isLogin()
    {
        return false;
    }
}