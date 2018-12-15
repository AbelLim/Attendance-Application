package com.example.arx8l.attendenceapp;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class LoginManager
{
    private List<User> userList = new ArrayList<>();
    private Database database = new Database();

    public LoginManager(){}

    public void login(String email, final String passwordUser, final OnLoginListener listener)
    {
        final DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference("users");
        Query query = userDatabase.orderByChild("email").equalTo(email);
        database.readData(query, new Database.OnGetDataListener() {
            @Override
            public void OnStart() {
                listener.OnStart();
            }

            @Override
            public void OnSuccess(DataSnapshot snapshot) {
                    for(DataSnapshot userSnapshot : snapshot.getChildren())
                    {
                        User user = userSnapshot.getValue(User.class);
                        userList.add(user);
                    }
                    String passwordDatabase = userList.get(0).getPassword();
                    if(passwordDatabase.equals(passwordUser))
                        listener.OnSuccess();
                    else
                        listener.OnFailure();
            }

            @Override
            public void OnFailure() {
                listener.OnFailure();
            }
        });
    }

    public interface OnLoginListener
    {
        void OnStart();
        void OnSuccess();
        void OnFailure();
    }
}
