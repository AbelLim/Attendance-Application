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
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

public class MedicalLeaveFragment extends Fragment implements View.OnClickListener {
    private View view;
    private EditText et_cer;
    private EditText et_reason;

    private int mYear;
    private int mMonth;
    private int mDay;
    private String userID;
    private String startDate;
    private String endDate;

    private int PICK_IMAGE_REQUEST;
    private Uri attachedFile;
    private LeaveManager lm = new LeaveManager();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_medical, null);
        initView(view);
        Bundle args =getArguments();
        userID = args.getString("userID");
        clearFields();
        endDate = "TEST-DATE";
        return view;
    }

    private void initView(View view) {
        Button btnDate = view.findViewById(R.id.btn_date);
        Button btSubmit = view.findViewById(R.id.bt_submit);
        Button btPhoto = view.findViewById(R.id.bt_photo);
        et_cer = view.findViewById(R.id.et_cer);
        et_reason = view.findViewById(R.id.et_reason);
        btnDate.setOnClickListener(this);
        btSubmit.setOnClickListener(this);
        btPhoto.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_photo:
                selectImage();
                break;
            case R.id.btn_date:
                showTime(true);
                break;
            case R.id.bt_submit:
                submitLeaveApplication();
                break;
        }
    }

    private void showTime(boolean isStartDate) {
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity() , new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = Integer.toString(year) + "/" + Integer.toString(month+1) + "/" + Integer.toString(dayOfMonth);
                if (isStartDate)
                    startDate = date;
                else
                    endDate = date;
            }
        },mYear,mMonth,mDay);

        datePickerDialog.show();

    }

    public void selectImage()
    {
        Intent intent = new Intent();
        //Select only image types
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //Always show chooser
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
            attachedFile = data.getData();
    }

    public void submitLeaveApplication()
    {
        String certificateNumber = et_cer.getText().toString();
        String additionalComment = et_reason.getText().toString();

        if(certificateNumber=="" || attachedFile==null || startDate=="" || endDate=="" || additionalComment==null)
        {
            Toast.makeText(this.getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }

        else
        {
            lm.postLeaveApplication(userID, certificateNumber, attachedFile, startDate, endDate, additionalComment, new LeaveManager.PostLeaveApplicationListener() {
                @Override
                public void OnStart() {
                    Toast.makeText(view.getContext(), "Uploading files...", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void OnSuccess() {
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

    private void clearFields()
    {
        et_cer.setText("");
        et_reason.setText("");
        attachedFile = null;
        startDate ="";
        endDate ="";
    }
}
