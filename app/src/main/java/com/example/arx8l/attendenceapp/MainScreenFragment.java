package com.example.arx8l.attendenceapp;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainScreenFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainScreenFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "service";
    private static final long START_TIME_IN_MILLIS = 10800000;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    Button tapOutBtt;
    RelativeLayout classAtt;
    LinearLayout countdownTimerLayout;
    TextView countDownTimerText;
    TextView campusPercentageText;
    TextView classPercentageText;
    ImageView tapInTapOut;
    CircularProgressBar campusCircleProgressBar;
    CircularProgressBar classCircleProgressBar;
    LocalDate currentDate;

    CountDownTimer cdt;
    AttendanceManager attendanceManager;
    User u;

    private long mTimeLeftInMillis = 30000;
    private long timeUserTappedIn;
    private boolean userTappedIn;
    private boolean timerIsRunning;
    private int classAttendance;
    private int campusAttendance;
    private String userId = "12345678";
    private HashMap<String, Boolean> campusAttendanceDaysCheck;
    private String currentDateString;


    public MainScreenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainScreenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainScreenFragment newInstance(String param1, String param2) {
        MainScreenFragment fragment = new MainScreenFragment();
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
        View myFragmentView = inflater.inflate(R.layout.fragment_main_screen, container, false);

//        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", MODE_PRIVATE);

//        userTappedIn = prefs.getBoolean("userTappedIn", false);
//        timeUserTappedIn = prefs.getLong("timeUserTappedIn", 0);

        currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        currentDateString = currentDate.format(formatter);

        loadHashMap();

        System.out.println("MainScreenFragment >>> ");
//        for (String name: campusAttendanceDaysCheck.keySet()){
//            String key = name;
//            Boolean value = campusAttendanceDaysCheck.get(name);
//            System.out.println(key + ": " + value);
//        }

        ArrayList<String> sortedKeys =
                new ArrayList<String>(campusAttendanceDaysCheck.keySet());
        Collections.sort(sortedKeys, new Comparator<String>() {
            DateFormat f = new SimpleDateFormat("dd/MM/yyyy");
            @Override
            public int compare(String o1, String o2) {
                try {
                    return f.parse(o1).compareTo(f.parse(o2));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });

        for (String x : sortedKeys)
            System.out.println(x + ": " + campusAttendanceDaysCheck.get(x));


        if (getArguments() != null) {
            classAttendance = getArguments().getInt("class attendance");
            campusAttendance = getArguments().getInt("campus attendance");
        }

        countdownTimerLayout = myFragmentView.findViewById(R.id.countdown_timer_layout);

        classPercentageText = myFragmentView.findViewById(R.id.class_percentage);
        classPercentageText.setText(classAttendance + "%");
        classCircleProgressBar = myFragmentView.findViewById(R.id.class_circle_progress_bar);
        classCircleProgressBar.setProgress(classAttendance);

        campusPercentageText = myFragmentView.findViewById(R.id.campus_percentage);
        campusPercentageText.setText(campusAttendance + "%");
        campusCircleProgressBar = myFragmentView.findViewById(R.id.campus_circle_progress_bar);
        campusCircleProgressBar.setProgress(campusAttendance);


        tapInTapOut = getActivity().findViewById(R.id.tap_in_tap_out);

        tapInTapOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions( new String[]{Manifest.permission.CAMERA}, 1);
                }
                else {
                    if (tapOutBtt.getVisibility() == View.VISIBLE){
                        Intent getQrCodeResultIntent = new Intent(getContext(), QRCodeScanner.class);
                        startActivityForResult(getQrCodeResultIntent, 2);
                    }
                    else {
                        Intent getQrCodeResultIntent = new Intent(getContext(), QRCodeScanner.class);
                        startActivityForResult(getQrCodeResultIntent, 1);
                    }
                }
            }
        });

        tapOutBtt = myFragmentView.findViewById(R.id.tap_out);
        tapOutBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions( new String[]{Manifest.permission.CAMERA}, 2);
                }
                else {
                    Intent getQrCodeResultIntent = new Intent(getContext(), QRCodeScanner.class);
                    startActivityForResult(getQrCodeResultIntent, 2);
                }
            }
        });

        countDownTimerText = new TextView(getContext());
        countDownTimerText.setTextSize(24);

//        classAtt = myFragmentView.findViewById(R.id.class_att);
//        classAtt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ClassAttendanceFragment classAttendanceFragment = new ClassAttendanceFragment();
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
//                fragmentTransaction.replace(R.id.main_frag, classAttendanceFragment, "");
//                fragmentTransaction.commit();
//            }
//        });

//        String text = "Tapping Out in:";
//        SpannableString ss = new SpannableString(text);
//        ForegroundColorSpan fcsRed = new ForegroundColorSpan(Color.RED);
//        ss.setSpan(fcsRed, 8, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        tappingOut.setText(ss);
        tapOutBtt.setVisibility(View.GONE);

        attendanceManager = new AttendanceManager();

        attendanceManager.getUser(userId, new AttendanceManager.onGetUserListener() {
            @Override
            public void OnStart() {

            }

            @Override
            public void OnSuccess(User user) {
                u = user;

                userTappedIn = u.getTappedIn();

                if(userTappedIn && cdt == null){
                    countdownTimerLayout.addView(countDownTimerText);
                    Date date = new Date();
                    try {
                        date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(u.getTapInTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    timeUserTappedIn = date.getTime();
                    startTimer();
                }
                else if (cdt != null && !timerIsRunning){
                    tapOutBtt.setVisibility(View.VISIBLE);
                }
                else if (userTappedIn){
                    countdownTimerLayout.addView(countDownTimerText);
                }
            }

            @Override
            public void OnFailure() {

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        switch (requestCode) {
            case 1: {
                Intent getQrCodeResultIntent = new Intent(getContext(), QRCodeScanner.class);
                startActivityForResult(getQrCodeResultIntent, 1);
            }
            case 2:
                Intent getQrCodeResultIntent = new Intent(getContext(), QRCodeScanner.class);
                startActivityForResult(getQrCodeResultIntent, 2);
        }
    }


    public interface OnSomeEventListener {
        public void someEvent(HashMap<String, Boolean> campusDaysCheck);
    }

    OnSomeEventListener someEventListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            someEventListener = (OnSomeEventListener) context;
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
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == 1 && resultCode == 0 && data != null) {
            String result = data.getStringExtra("cc");

            if (result.equals("JCU QR Code Attendance") && timerIsRunning){
                alertMessage("Not Yet");
            }
            else if (result.equals("JCU QR Code Attendance") && !campusAttendanceDaysCheck.containsKey(currentDateString)){
                alertMessage("Error");
            }
            else if (result.equals("JCU QR Code Attendance") && campusAttendanceDaysCheck.get(currentDateString) != null){
                if (campusAttendanceDaysCheck.get(currentDateString)){
                    alertMessage("Today you have successfully tapped in and tapped out!");
                }
            }
            else if (result.equals("JCU QR Code Attendance") && campusAttendanceDaysCheck.get(currentDateString) == null) {
                attendanceManager.tapIn(userId, new AttendanceManager.OnTapInListener() {
                    @Override
                    public void OnStart() {

                    }

                    @Override
                    public void OnSuccess() {
                        attendanceManager.getUser(userId, new AttendanceManager.onGetUserListener() {
                            @Override
                            public void OnStart() {

                            }

                            @Override
                            public void OnSuccess(User user) {
                                u = user;
                                alertMessage("Successfully Tapped In!");
                                countdownTimerLayout.addView(countDownTimerText);
                                Date date = new Date();
                                try {
                                    date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(u.getTapInTime());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                timeUserTappedIn = date.getTime();
                                userTappedIn = u.getTappedIn();
                                startTimer();

                            }

                            @Override
                            public void OnFailure() {

                            }
                        });
                    }

                    @Override
                    public void OnFailure() {

                    }
                });

            } else {
                alertMessage("Error");
            }
        }
        else if(requestCode == 2 && resultCode == 0 && data != null){
            String result = data.getStringExtra("cc");
            if (result.equals("JCU QR Code Attendance")) {
                attendanceManager.tapOut(userId, new AttendanceManager.OnTapOutListener() {
                    @Override
                    public void OnStart() {

                    }

                    @Override
                    public void OnSuccess() {
                        attendanceManager.getUser(userId, new AttendanceManager.onGetUserListener() {
                            @Override
                            public void OnStart() {

                            }

                            @Override
                            public void OnSuccess(User user) {
                                u = user;
                                alertMessage("Successfully Tapped Out!");

                                if (campusAttendanceDaysCheck.get(currentDateString) == null){
                                    campusAttendanceDaysCheck.put(currentDateString, true);
                                }

                                saveHashMap();

                                someEventListener.someEvent(campusAttendanceDaysCheck);
                                userTappedIn = u.getTappedIn();
                                tapOutBtt.setVisibility(View.GONE);
                            }

                            @Override
                            public void OnFailure() {

                            }
                        });
                    }

                    @Override
                    public void OnFailure() {

                    }
                });
            }
            else {
                alertMessage("Error");
            }
        }
    }

    private void alertMessage(String message){
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Scan result");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void startTimer(){
        mTimeLeftInMillis = mTimeLeftInMillis - (System.currentTimeMillis() - timeUserTappedIn);
        System.out.println(mTimeLeftInMillis);

        cdt = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerIsRunning = true;
                mTimeLeftInMillis = millisUntilFinished;
                Log.i(TAG, "Countdown seconds remaining: " + millisUntilFinished / 1000);
                long hour = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                long minute = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished));
                long second = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));

                String hms = String.format("Tapping Out in: " + "%02d:%02d:%02d", hour, minute, second);
                countDownTimerText.setText(hms);//set text
            }

            @Override
            public void onFinish() {
                timerIsRunning = false;
                countdownTimerLayout.removeView(countDownTimerText);
                tapOutBtt.setVisibility(View.VISIBLE);
            }
        };
        cdt.start();
    }

    public void loadHashMap(){
        File file = new File(getActivity().getDir("data", MODE_PRIVATE), "map");
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
            campusAttendanceDaysCheck = (HashMap<String, Boolean>) inputStream.readObject();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void saveHashMap(){
        File file = new File(getActivity().getDir("data", MODE_PRIVATE), "map");
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(campusAttendanceDaysCheck);
            outputStream.flush();
            outputStream.close();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
