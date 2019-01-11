/*This fragment is the check my attendance screen. it contains the container that can be switched to campus, class, detailed class attendance board
* Code by Celine and Tung*/
package com.example.arx8l.attendenceapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CheckMyAttendanceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CheckMyAttendanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckMyAttendanceFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    boolean currentScreenIsCampus = true;

    private OnFragmentInteractionListener mListener;

    ImageView tapInTapOut;
//    SharedPreferences preferences;


    public CheckMyAttendanceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CheckMyAttendanceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CheckMyAttendanceFragment newInstance(String param1, String param2) {
        CheckMyAttendanceFragment fragment = new CheckMyAttendanceFragment();
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
        View myFragmentView = inflater.inflate(R.layout.fragment_check_my_attendance, container, false);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ClassAttendanceFragment classAttendanceFragment = new ClassAttendanceFragment();
        classAttendanceFragment.setArguments(getArguments());
        CampusAttendanceFragment campusAttendanceFragment = new CampusAttendanceFragment();
        campusAttendanceFragment.setArguments(getArguments());
        fragmentTransaction.add(R.id.container_frag_att, campusAttendanceFragment, "campusTag");
        fragmentTransaction.commit();

        // swipe left-right gesture for the campus and class attendance:

        myFragmentView.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            public void onSwipeLeft() {
                if(currentScreenIsCampus){
                    ClassAttendanceFragment classAttendanceFragment = new ClassAttendanceFragment();
                    classAttendanceFragment.setArguments(getArguments());
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                    fragmentTransaction.replace(R.id.container_frag_att, classAttendanceFragment, "classTag");
                    fragmentTransaction.commit();
                    currentScreenIsCampus = false;
                }
                else {
                    CampusAttendanceFragment campusAttendanceFragment = new CampusAttendanceFragment();
                    campusAttendanceFragment.setArguments(getArguments());
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                    fragmentTransaction.replace(R.id.container_frag_att, campusAttendanceFragment, "campusTag");
                    fragmentTransaction.commit();
                    currentScreenIsCampus = true;
                }
            }


            public void onSwipeRight() {
                if(currentScreenIsCampus){
                    ClassAttendanceFragment classAttendanceFragment = new ClassAttendanceFragment();
                    classAttendanceFragment.setArguments(getArguments());
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                    fragmentTransaction.replace(R.id.container_frag_att, classAttendanceFragment, "classTag");
                    fragmentTransaction.commit();
                    currentScreenIsCampus = false;
                }
                else {
                    CampusAttendanceFragment campusAttendanceFragment = new CampusAttendanceFragment();
                    campusAttendanceFragment.setArguments(getArguments());
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                    fragmentTransaction.replace(R.id.container_frag_att, campusAttendanceFragment, "campusTag");
                    fragmentTransaction.commit();
                    currentScreenIsCampus = true;
                }

            }

        });

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

    @Override
    public void onStop() {
        super.onStop();
    }
}
