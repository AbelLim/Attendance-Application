package com.example.arx8l.attendenceapp;

import android.content.Context;
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
    TextView leavesLeft1;
    TextView absence1;

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

        if (getArguments() != null) {
            classAttendanceDaysCheck = (HashMap<String, HashMap<String,String>>) getArguments().getSerializable("class days check");
        }

        classNames = new ArrayList<>();

        leavesLeft1 = myFragmentView.findViewById(R.id.leaves_left_1);
        absence1 = myFragmentView.findViewById(R.id.absence_1);
        className1 = myFragmentView.findViewById(R.id.class_name_1);

        for (String classId: classAttendanceDaysCheck.keySet()){
            if(!classNames.contains(classId.substring(0,6))){
                classNames.add(classId.substring(0,6));
            }
        }

        className1.setText(classNames.get(0).replaceAll("(?<=\\D)(?=\\d)"," "));
        leavesLeft1.setText(String.valueOf(calculateClassLeavesLeft(classNames.get(0))));
        absence1.setText(String.valueOf(getClassDaysAbsence(classNames.get(0))));

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

    public int calculateClassLeavesLeft(String className){
        int totalLeavesLeft = 0;
        for (String classId : classAttendanceDaysCheck.keySet()){
            if (classId.substring(0, 6).equals(className)){
                totalLeavesLeft += calculateAttendance(classAttendanceDaysCheck.get(classId));
            }
        }
        return totalLeavesLeft/2;
    }

    private int calculateAttendance(HashMap<String, String> daysCheck){
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

    private int getClassDaysAbsence(String className){
        int classDaysAbsence = 0;
        for (String classId : classAttendanceDaysCheck.keySet()){
            if (classId.substring(0, 6).equals(className)){
                classDaysAbsence += getDaysAbsence(classAttendanceDaysCheck.get(classId));
            }
        }
        return classDaysAbsence;
    }

    private int getDaysAbsence(HashMap<String, String> daysCheck){
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
