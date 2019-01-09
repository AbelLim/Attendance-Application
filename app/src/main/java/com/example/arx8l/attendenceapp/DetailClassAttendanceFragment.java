package com.example.arx8l.attendenceapp;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailClassAttendanceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailClassAttendanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailClassAttendanceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private HashMap<String, HashMap<String, String>> classAttendanceDaysCheck;
    private ArrayList<String> classNames;

    private OnFragmentInteractionListener mListener;

    TextView className1;
    TextView attendance1;
    TextView absence1;
    RoundCornerProgressBar progressBar1;
    ImageView warning1;

    TextView className2;
    TextView attendance2;
    TextView absence2;
    RoundCornerProgressBar progressBar2;
    ImageView warning2;

    TextView className3;
    TextView attendance3;
    TextView absence3;
    RoundCornerProgressBar progressBar3;
    ImageView warning3;

    TextView className4;
    TextView attendance4;
    TextView absence4;
    RoundCornerProgressBar progressBar4;
    ImageView warning4;

    public DetailClassAttendanceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailClassAttendanceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailClassAttendanceFragment newInstance(String param1, String param2) {
        DetailClassAttendanceFragment fragment = new DetailClassAttendanceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


      // set icon 2 image animation for detailed attendance so it flips back to class Attendance.
        View myFragmentView = inflater.inflate(R.layout.fragment_detailed_class_attendance,
                container, false);

        myFragmentView.setCameraDistance(12000);

        if (getArguments() != null) {
            classAttendanceDaysCheck = (HashMap<String, HashMap<String,String>>) getArguments().getSerializable("class days check");
        }

        classNames = new ArrayList<>();

        attendance1 = myFragmentView.findViewById(R.id.attendance_1);
        absence1 = myFragmentView.findViewById(R.id.absence_1);
        className1 = myFragmentView.findViewById(R.id.class_name_1);
        progressBar1 = myFragmentView.findViewById(R.id.progress_1);
        warning1 = myFragmentView.findViewById(R.id.warning_1);
        warning1.setVisibility(View.INVISIBLE);

        attendance2 = myFragmentView.findViewById(R.id.attendance_2);
        absence2 = myFragmentView.findViewById(R.id.absence_2);
        className2 = myFragmentView.findViewById(R.id.class_name_2);
        progressBar2 = myFragmentView.findViewById(R.id.progress_2);
        warning2 = myFragmentView.findViewById(R.id.warning_2);
        warning2.setVisibility(View.INVISIBLE);

        attendance3 = myFragmentView.findViewById(R.id.attendance_3);
        absence3 = myFragmentView.findViewById(R.id.absence_3);
        className3 = myFragmentView.findViewById(R.id.class_name_3);
        progressBar3 = myFragmentView.findViewById(R.id.progress_3);
        warning3 = myFragmentView.findViewById(R.id.warning_3);
        warning3.setVisibility(View.INVISIBLE);

        attendance4 = myFragmentView.findViewById(R.id.attendance_4);
        absence4 = myFragmentView.findViewById(R.id.absence_4);
        className4 = myFragmentView.findViewById(R.id.class_name_4);
        progressBar4 = myFragmentView.findViewById(R.id.progress_4);
        warning4 = myFragmentView.findViewById(R.id.warning_4);
        warning4.setVisibility(View.INVISIBLE);

        for (String classId: classAttendanceDaysCheck.keySet()){
            if(!classNames.contains(classId.substring(0,6))){
                classNames.add(classId.substring(0,6));
            }
        }

        int subjectAttendance1 = calculateSubjectAttendance(classNames.get(0));
        className1.setText(classNames.get(0).replaceAll("(?<=\\D)(?=\\d)"," "));
        attendance1.setText("Attendance: " + String.valueOf(subjectAttendance1) + "%");
        absence1.setText("Absence: " + String.valueOf(getSubjectDaysAbsence(classNames.get(0))));
        progressBar1.setProgress(subjectAttendance1);
        if (subjectAttendance1 < 100 && subjectAttendance1 > 70){
            progressBar1.setProgressColor(Color.parseColor("#ffcc00"));
        }
        else if (subjectAttendance1 <= 70){
            progressBar1.setProgressColor(Color.parseColor("#ff0000"));
            warning1.setVisibility(View.VISIBLE);
        }

        int subjectAttendance2 = calculateSubjectAttendance(classNames.get(1));
        className2.setText(classNames.get(1).replaceAll("(?<=\\D)(?=\\d)"," "));
        attendance2.setText("Attendance: " + String.valueOf(subjectAttendance2) + "%");
        absence2.setText("Absence: " + String.valueOf(getSubjectDaysAbsence(classNames.get(1))));
        progressBar2.setProgress(subjectAttendance2);
        if (subjectAttendance2 < 100 && subjectAttendance2 > 70){
            progressBar2.setProgressColor(Color.parseColor("#ffcc00"));
        }
        else if (subjectAttendance2 <= 70){
            progressBar2.setProgressColor(Color.parseColor("#ff0000"));
            warning2.setVisibility(View.VISIBLE);
        }

        int subjectAttendance3 = calculateSubjectAttendance(classNames.get(2));
        className3.setText(classNames.get(2).replaceAll("(?<=\\D)(?=\\d)"," "));
        attendance3.setText("Attendance: " + String.valueOf(subjectAttendance3) + "%");
        absence3.setText("Absence: " + String.valueOf(getSubjectDaysAbsence(classNames.get(2))));
        progressBar3.setProgress(subjectAttendance3);
        if (subjectAttendance3 < 100 && subjectAttendance3 > 70){
            progressBar3.setProgressColor(Color.parseColor("#ffcc00"));
        }
        else if (subjectAttendance3 <= 70){
            progressBar3.setProgressColor(Color.parseColor("#ff0000"));
            warning3.setVisibility(View.VISIBLE);
        }

        int subjectAttendance4 = calculateSubjectAttendance(classNames.get(3));
        className4.setText(classNames.get(3).replaceAll("(?<=\\D)(?=\\d)"," "));
        attendance4.setText("Attendance: " + String.valueOf(subjectAttendance4) + "%");
        absence4.setText("Absence: " + String.valueOf(getSubjectDaysAbsence(classNames.get(3))));
        progressBar4.setProgress(subjectAttendance4);
        if (subjectAttendance4 < 100 && subjectAttendance4 > 70){
            progressBar4.setProgressColor(Color.parseColor("#ffcc00"));
        }
        else if (subjectAttendance4 <= 70){
            progressBar4.setProgressColor(Color.parseColor("#ff0000"));
            warning4.setVisibility(View.VISIBLE);
        }

        ImageView info2;
        info2 = myFragmentView.findViewById(R.id.info_2);

        info2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // flip from detailed to class attendance
                ClassAttendanceFragment classAttendanceFragment = new ClassAttendanceFragment();
                classAttendanceFragment.setArguments(getArguments()); //flip from detailed to class attendance
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.animator.card_flip_right_in,
                        R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in,
                        R.animator.card_flip_left_out);
                fragmentTransaction.replace(R.id.container_frag_att,
                        classAttendanceFragment, "");
                fragmentTransaction.commit();
                ///
            }
        });

        return myFragmentView;
    }

    public int calculateSubjectAttendance(String className){
        int totalLeavesLeft = 0;
        for (String classId : classAttendanceDaysCheck.keySet()){
            if (classId.substring(0, 6).equals(className)){
                totalLeavesLeft += calculateClassAttendance(classAttendanceDaysCheck.get(classId));
            }
        }
        return totalLeavesLeft/2;
    }

    private int calculateClassAttendance(HashMap<String, String> daysCheck){
        int daysTappedIn = 0;
        int daysTappedInRequired = 0;

        for (String name: daysCheck.keySet()){
            if (!daysCheck.get(name).equals("Null")) {
                daysTappedInRequired += 1;
                if (daysCheck.get(name).equals("True")) {
                    daysTappedIn += 1;
                }
            }
        }
        return (int) (((float) daysTappedIn/ (float) daysTappedInRequired) * 100);
    }

    private int getSubjectDaysAbsence(String className){
        int classDaysAbsence = 0;
        for (String classId : classAttendanceDaysCheck.keySet()){
            if (classId.substring(0, 6).equals(className)){
                classDaysAbsence += getClassDaysAbsence(classAttendanceDaysCheck.get(classId));
            }
        }
        return classDaysAbsence;
    }

    private int getClassDaysAbsence(HashMap<String, String> daysCheck){
        int daysAbsence = 0;
        for (String date: daysCheck.keySet()){
            if (daysCheck.get(date).equals("False")){
                daysAbsence += 1;
            }
        }
        return daysAbsence;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
