package com.example.arx8l.attendenceapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import java.util.Calendar;

public class MedicalLeaveFragment extends Fragment implements View.OnClickListener {
    private int mYear;
    private int mMonth;
    private int mDay;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_medical, null);
        initView(view);

        return view;
    }

    private void initView(View view) {
        LinearLayout llDate = view.findViewById(R.id.ll_date);
        Button btSubmit = view.findViewById(R.id.bt_submit);
        llDate.setOnClickListener(this);
        btSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_date:
                showTime();
                break;
            case R.id.bt_submit:
                startActivity(new Intent(getActivity(),SuccessActivity.class));
                break;
        }
    }

    private void showTime() {
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            }
        },mYear,mMonth,mDay);

        datePickerDialog.show();

    }
}
