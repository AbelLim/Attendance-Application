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
import android.widget.Button;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CampusAttendanceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CampusAttendanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CampusAttendanceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private int campusAttendance;

    CircularProgressBar campusCheckCircleBar;
    TextView campusCheckPercentage;
    TextView campusStatus;

    public CampusAttendanceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CampusAttendanceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CampusAttendanceFragment newInstance(String param1, String param2) {
        CampusAttendanceFragment fragment = new CampusAttendanceFragment();
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
        View myFragmentView = inflater.inflate(R.layout.fragment_campus_attendance, container, false);

        myFragmentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassAttendanceFragment classAttendanceFragment = new ClassAttendanceFragment();
                classAttendanceFragment.setArguments(getArguments());
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_frag_att, classAttendanceFragment, "classTag");
                fragmentTransaction.commit();
            }
        });

        campusCheckCircleBar = myFragmentView.findViewById(R.id.campus_check_circle_bar);
        campusCheckPercentage = myFragmentView.findViewById(R.id.campus_check_percentage);
        campusStatus = myFragmentView.findViewById(R.id.campus_status);

        campusAttendance = getArguments().getInt("campus attendance");

        campusCheckCircleBar.setProgress(campusAttendance);
        campusCheckPercentage.setText(campusAttendance + "%");

        if (campusAttendance >= 90){
            campusStatus.setText("Your attendance meets the ICA Requirements");
        }
        else {
            campusStatus.setText("Your attendance must reach 90% to meet ICA's requirement");
        }

        return myFragmentView;
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
