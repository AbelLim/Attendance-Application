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
import android.location.LocationListener;
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

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainScreenFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainScreenFragment extends Fragment implements LocationListener{
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

    CountDownTimer cdt;
    AttendanceManager attendanceManager;
    User u;
    Location userLocation;

    private long mTimeLeftInMillis = 30000;
    private long timeUserTappedIn;
    private boolean userTappedIn;
    private boolean timerIsRunning;
    private int classAttendance;
    private int campusAttendance;
    private String userId = "12345678";
    private HashMap<String, Boolean> campusAttendanceDaysCheck;
    private HashMap<String, HashMap<String, Boolean>> classAttendanceDaysCheck;
    private String currentDateString;
    private String currentTimeString;
    private ArrayList<Class> classes;
    private Class cp3408L;
    private Class cp3408P;

    private LatLng ne = new LatLng(1.316537, 103.876634);
    private LatLng sw = new LatLng(1.315058, 103.875267);
    private LatLngBounds JCU = new LatLngBounds(sw, ne);
    private LocationManager locationManager;


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

        classes = new ArrayList<>();

        cp3408L = new Class("CP3408-Lecture", "14:00", "15:50");
        cp3408P = new Class("CP3408-Practical", "13:00", "14:50");

        classes.add(cp3408L);
        classes.add(cp3408P);

        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 3);
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            userLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        loadHashMap();

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
                    Intent getQrCodeResultIntent = new Intent(getContext(), QRCodeScanner.class);
                    startActivityForResult(getQrCodeResultIntent, 1);
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

        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent getQrCodeResultIntent = new Intent(getContext(), QRCodeScanner.class);
                    startActivityForResult(getQrCodeResultIntent, 1);
                }
                break;
            }
            case 2: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent getQrCodeResultIntent = new Intent(getContext(), QRCodeScanner.class);
                    startActivityForResult(getQrCodeResultIntent, 2);
                }
                break;
            }
            case 3: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                        userLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setTitle("Required Permission");
                    alertDialog.setMessage("The app requires access to user location to run");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    getActivity().finishAndRemoveTask();
                                }
                            });
                    alertDialog.show();
                }
                break;
            }
        }
    }


    public interface OnSomeEventListener {
        public void someEvent();
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
    public void onLocationChanged(Location location) {
        userLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {

        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        currentDateString = currentDateTime.format(dateFormatter);
        currentTimeString = currentDateTime.format(timeFormatter);

        if (JCU.contains(new LatLng(userLocation.getLatitude(), userLocation.getLongitude()))) {
            if (requestCode == 1 && resultCode == 0 && data != null) {
                String scanResult = data.getStringExtra("cc");

                if (scanResult.equals("JCU QR Code Attendance")) {
                    if (timerIsRunning) {
                        alertMessage("Not Yet");
                    } else if (tapOutBtt.getVisibility() == View.VISIBLE) {
                        tapOutForCampus(scanResult);
                    } else if (!campusAttendanceDaysCheck.containsKey(currentDateString)) {
                        alertMessage("Date error");
                    } else if (campusAttendanceDaysCheck.get(currentDateString) != null) {
                        if (campusAttendanceDaysCheck.get(currentDateString)) {
                            alertMessage("Today you have successfully tapped in and tapped out!");
                        } else {
                            alertMessage("Date error");
                        }
                    } else if (campusAttendanceDaysCheck.get(currentDateString) == null) {
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
                } else if (classAttendanceDaysCheck.containsKey(scanResult)) {
                    String classID = scanResult;
                    Class checkingClass = getCheckingClass(classID);
                    HashMap<String, Boolean> attendanceDaysCheck = classAttendanceDaysCheck.get(classID);

                    if (attendanceDaysCheck.containsKey(currentDateString) &&
                            LocalTime.parse(currentTimeString).isAfter(LocalTime.parse(checkingClass.getStartTime())) &&
                            LocalTime.parse(currentTimeString).
                                    isBefore(LocalTime.parse(getClassTapInEndTime(checkingClass.getStartTime())))) {

                        if (attendanceDaysCheck.get(currentDateString) == null) {
                            attendanceDaysCheck.put(currentDateString, true);
                            alertMessage("Successfully tapped in for " + checkingClass.getName());
                        } else if (attendanceDaysCheck.get(currentDateString)) {
                            alertMessage("Today you have successfully tapped in for " + checkingClass.getName());
                        } else {
                            alertMessage("Date error");
                        }
                        saveHashMap();
                        someEventListener.someEvent();
                    } else if (attendanceDaysCheck.containsKey(currentDateString)
                            && attendanceDaysCheck.get(currentDateString)) {
                        alertMessage("Today you have successfully tapped in for " + checkingClass.getName());
                    } else {
                        alertMessage("Date-Time error");
                    }
                } else {
                    alertMessage("QR code error");
                }
            } else if (requestCode == 2 && resultCode == 0 && data != null) {
                String result = data.getStringExtra("cc");
                tapOutForCampus(result);
            }
        }
        else {
            alertMessage("You have to be inside JCU to tap in");
        }
    }

    private void tapOutForCampus(String scanResult){
        if (scanResult.equals("JCU QR Code Attendance")) {
            if (campusAttendanceDaysCheck.get(currentDateString) == null) {
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

                                campusAttendanceDaysCheck.put(currentDateString, true);
                                alertMessage("Successfully Tapped Out!");

                                saveHashMap();

                                someEventListener.someEvent();
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
                alertMessage("Date error");
            }
        }
        else {
            alertMessage("QR code error");
        }
    }

    private Class getCheckingClass(String classID){
        Class checkingClass = new Class("", "", "");
        for(Class c : classes){
            if (c.getName().equals(classID))
            {
                checkingClass = c;
            }
        }
        return checkingClass;
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

    public String getClassTapInEndTime(String startTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime lcStartTime = LocalTime.parse(startTime);
        lcStartTime = lcStartTime.plusMinutes(15);
        return lcStartTime.format(formatter);
    }

    public void loadHashMap(){
        File file = new File(getActivity().getDir("data", MODE_PRIVATE), "map");
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
            campusAttendanceDaysCheck = (HashMap<String, Boolean>) inputStream.readObject();
            classAttendanceDaysCheck = (HashMap<String, HashMap<String, Boolean>>) inputStream.readObject();
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
            outputStream.writeObject(classAttendanceDaysCheck);
            outputStream.flush();
            outputStream.close();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }
}
