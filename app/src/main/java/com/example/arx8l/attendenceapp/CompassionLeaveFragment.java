package com.example.arx8l.attendenceapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.Calendar;

public class CompassionLeaveFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private int mYear;
    private int mMonth;
    private int mDay;
    private Spinner spinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_compassion, null);
        initView(view);

        spinner = (Spinner) getView().findViewById(R.id.spinner);
        String[] leave = new String[]{"Issues at home","Pyschological issues", "Other"};
        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item, leave);

        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adp1);
        spinner.setOnItemSelectedListener(this);
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

    //Here is item selection of spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.v("item",(String) parent.getItemAtPosition(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
