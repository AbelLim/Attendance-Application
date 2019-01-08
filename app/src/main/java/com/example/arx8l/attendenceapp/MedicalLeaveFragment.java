/*This file manages the fragment to submit user's medical leave application.
* Code by Abel and Jin Feng*/

package com.example.arx8l.attendenceapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

public class MedicalLeaveFragment extends Fragment implements View.OnClickListener {
    private View view;
    private EditText et_cer;
    private EditText et_reason;
    private Button btnStartDate;
    private Button btnEndDate;
    private Button btPhoto;
    private Button btSubmit;

    private int mYear;
    private int mMonth;
    private int mDay;
    private String userID;
    private String startDate;
    private String endDate;

    private int PICK_IMAGE_REQUEST;
    private Uri attachedFile;
    private LeaveManager lm = new LeaveManager();

    //Tasks that are performed on the creation of this fragment.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_medical, null);
        initView(view);
        Bundle args =getArguments();
        userID = args.getString("userID");
        clearFields();
        return view;
    }

    //Initializes view elements
    private void initView(View view) {
        btnStartDate = view.findViewById(R.id.btn_startdate);
        btnEndDate = view.findViewById(R.id.btn_enddate);
        btPhoto = view.findViewById(R.id.bt_photo);
        btSubmit = view.findViewById(R.id.bt_submit);
        et_cer = view.findViewById(R.id.et_cer);
        et_reason = view.findViewById(R.id.et_reason);
        btnStartDate.setOnClickListener(this);
        btnEndDate.setOnClickListener(this);
        btSubmit.setOnClickListener(this);
        btPhoto.setOnClickListener(this);
    }

    //Manages buttons on click.
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_photo:
                selectImage();
                break;
            case R.id.btn_startdate:
                showTime(true);
                break;
            case R.id.btn_enddate:
                showTime(false);
                break;
            case R.id.bt_submit:
                submitLeaveApplication();
                break;
        }
    }

    //Displays date picker dialogue to select date. isStartDate true to input start date, false to input end date.
    private void showTime(boolean isStartDate) {
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity() , new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
            {
                //Formats values into a yyyy-mm-dd format.
                String date = String.format("%04d",year) + "/" + String.format("%02d",month+1) + "/" + String.format("%02d",dayOfMonth);
                if (isStartDate)
                {
                    startDate = date;
                    btnStartDate.setText(startDate);
                }
                else
                {
                    endDate = date;
                    btnEndDate.setText(endDate);
                }
            }
        },mYear,mMonth,mDay);

        datePickerDialog.show();
    }

    //Starts image selector activity
    public void selectImage()
    {
        Intent intent = new Intent();
        //Select only image types
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //Opens image selector. Results are returned in method onActivityResult
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //Listens for results of image selector activity
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            attachedFile = data.getData();
            btPhoto.setText("Uploaded");
        }
    }

    //Take inputs and submits them through a LeaveManager entity.
    public void submitLeaveApplication()
    {
        String certificateNumber = et_cer.getText().toString();
        String additionalComment = et_reason.getText().toString();

        //Checks if all fields are filled
        if(certificateNumber=="" || attachedFile==null || startDate=="" || endDate=="" || additionalComment==null)
        {
            Toast.makeText(this.getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }

        else
        {
            //Submits LeaveApplication
            lm.postLeaveApplication(userID, certificateNumber, attachedFile, startDate, endDate, additionalComment, new LeaveManager.PostLeaveApplicationListener() {
                @Override
                public void OnStart() {
                    Toast.makeText(view.getContext(), "Uploading files...", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void OnSuccess() {
                    //Clears all fields once submission is complete.
                    clearFields();
                    startActivity(new Intent(getActivity(),SuccessActivity.class));
                }

                @Override
                public void OnFailure() {
                    Toast.makeText(view.getContext(), "Upload failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    //Clears all fields
    private void clearFields()
    {
        et_cer.setText("");
        et_reason.setText("");
        attachedFile = null;
        startDate ="";
        endDate ="";
        btnStartDate.setText("Select Here");
        btnEndDate.setText("Select Here");
        btPhoto.setText("Upload Photo");
    }
}
