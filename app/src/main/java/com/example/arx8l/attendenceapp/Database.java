package com.example.arx8l.attendenceapp;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Database
{
    public Database(){}

    public void createUser(String userID, String name, String loginID, String password)
    {
        DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference("users");
        String hPassword = hashPassword(password);
        User user = new User(userID, name, loginID, hPassword);
        String key = userDatabase.push().getKey();
        if(key!=null)
            userDatabase.child(key).setValue(user);
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

    public String hashPassword(String string)
    {
        String result = "";
        try
        {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(string.getBytes(StandardCharsets.UTF_8));
            result = bytesToHex(encodedhash);
        }
        catch (NoSuchAlgorithmException e)
        {

        }
        return result;
    }

    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}