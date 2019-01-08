/*This class defines the FileManager entity. It is used to hold methods related to uploading files to Firebase Storage.
* Code by Abel*/
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

    //Constructor
    public FileManager()
    {
        //Get root reference for Firebase Storage
        mStorageReference = FirebaseStorage.getInstance().getReference();
    }

    //Handles uploading of files
    public void uploadFile(Uri file, String fileID, OnFileUploadListener listener)
    {
        listener.OnStart();
        //Creates file reference based on fileID
        StorageReference fileRef = mStorageReference.child("leave application/" + fileID);

        //Upload files and reflect success state in listener
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

    //Listener
    public interface OnFileUploadListener
    {
        void OnStart();
        void OnSuccess();
        void OnFailure();
    }
}
