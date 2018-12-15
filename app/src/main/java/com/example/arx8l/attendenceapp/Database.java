package com.example.arx8l.attendenceapp;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Database
{
    private DatabaseReference databaseUsers;

    public Database()
    {
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
    }

    private void createUser(String userID, String name, String email, String password)
    {
        User user = new User(userID, name, email, password);
        String key = databaseUsers.push().getKey();
        if(key!=null)
            databaseUsers.child(key).setValue(user);
    }

    //Data sent/completion listener methods
    public interface OnGetDataListener
    {
        void OnStart();
        void OnSuccess(DataSnapshot snapshot);
        void OnFailure();
    }

    public void readData(Query query, final OnGetDataListener listener)
    {
        listener.OnStart();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                    listener.OnSuccess(dataSnapshot);
                else
                    listener.OnFailure();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.OnFailure();
            }
        });
    }
}