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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ClassAttendanceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ClassAttendanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassAttendanceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private int classAttendance;

    CircularProgressBar classCheckCircleBar;
    TextView classCheckPercentage;
    TextView classStatus;
    ImageView info1;
    ImageView attentionIcon;


    public ClassAttendanceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClassAttendanceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClassAttendanceFragment newInstance(String param1, String param2) {
        ClassAttendanceFragment fragment = new ClassAttendanceFragment();
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
        View myFragmentView = inflater.inflate(R.layout.fragment_class_attendance, container, false);
        myFragmentView.setCameraDistance(12000);
        info1 = myFragmentView.findViewById(R.id.info_1);
        info1.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                //flip from class to detailed attendance
                    DetailClassAttendanceFragment detailClassAttendanceFragment = new DetailClassAttendanceFragment();
                    detailClassAttendanceFragment.setArguments(getArguments()); //flip to detailed attendance
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.animator.card_flip_right_in,
                            R.animator.card_flip_right_out,
                            R.animator.card_flip_left_in,
                            R.animator.card_flip_left_out);
                    fragmentTransaction.replace(R.id.container_frag_att, detailClassAttendanceFragment, "");
                    fragmentTransaction.commit();
                    ///
                }
            });

//        myFragmentView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CampusAttendanceFragment campusAttendanceFragment = new CampusAttendanceFragment();
//                campusAttendanceFragment.setArguments(getArguments());
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.container_frag_att, campusAttendanceFragment, "campusTag");
//                fragmentTransaction.commit();
//            }
//        });

        classCheckCircleBar = myFragmentView.findViewById(R.id.class_check_circle_bar);
        classCheckPercentage = myFragmentView.findViewById(R.id.class_check_percentage);
        classStatus = myFragmentView.findViewById(R.id.class_status);
        attentionIcon = myFragmentView.findViewById(R.id.attention_icon_class);
        attentionIcon.setVisibility(View.INVISIBLE);

        classAttendance = getArguments().getInt("class attendance");

        classCheckCircleBar.setProgress(classAttendance);
        classCheckPercentage.setText(classAttendance + "%");

        if (classAttendance >= 90){
            classStatus.setText("Your attendance meets the ICA Requirements");
        }
        else {
            classStatus.setText("Your attendance must reach 90% to meet ICA's requirement");
            classCheckCircleBar.setColor(Color.parseColor("#ff6666"));
            attentionIcon.setVisibility(View.VISIBLE);
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
