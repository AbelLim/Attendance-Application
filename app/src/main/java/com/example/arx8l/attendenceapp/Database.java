/*This class defines a database entity which is used to communicate with the Firebase database.
* Code by Abel*/
package com.example.arx8l.attendenceapp;

import android.support.annotation.NonNull;
import android.util.Log;

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
    private final String TAG = this.getClass().getName();
    //Constructors
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

    // Query is passed into the method.
    // Data Snapshot of the database is then returned through the listener.
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

    //Hash password using SHA-256 for security purposes
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
            Log.e(TAG, "hashPassword: ", e);
        }
        return result;
    }

    //Used by hashPassword to convert the hash from bytes to hexadecimal
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