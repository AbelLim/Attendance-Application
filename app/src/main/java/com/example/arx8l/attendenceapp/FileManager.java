package com.example.arx8l.attendenceapp;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FileManager {
    private StorageReference mStorageReference;

    public FileManager()
    {
        mStorageReference = FirebaseStorage.getInstance().getReference();
    }

    public void uploadFile(Uri file, String fileID, OnFileUploadListener listener)
    {
        listener.OnStart();
        StorageReference fileRef = mStorageReference.child("leave application/" + fileID);

        fileRef.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                listener.OnSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.OnFailure();
            }
        });
    }

    public interface OnFileUploadListener
    {
        void OnStart();
        void OnSuccess();
        void OnFailure();
    }
}
