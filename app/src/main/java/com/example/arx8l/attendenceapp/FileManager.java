package com.example.arx8l.attendenceapp;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class FileManager {
    private StorageReference mStorageReference;

    public FileManager()
    {
        mStorageReference = FirebaseStorage.getInstance().getReference();
    }

    public void uploadFile(String filePath, OnFileUploadListener listener)
    {
        listener.OnStart();
        Uri mFile = Uri.fromFile(new File("TEST.docx"));
        //String[] filename = filePath.split("/");
        StorageReference fileRef = mStorageReference.child("images/TEST.docx");

        fileRef.putFile(mFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
