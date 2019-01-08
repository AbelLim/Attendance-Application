package com.example.arx8l.attendenceapp;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
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
import android.os.Handler;
import android.os.SystemClock;
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
    LinearLayout countdownTimerLayout;
    TextView countDownTimerText;
    TextView campusPercentageText;
    TextView classPercentageText;
    TextView userNameText;
    TextView userIdText;
    ImageView tapInTapOut;
    CircularProgressBar campusCircleProgressBar;
    CircularProgressBar classCircleProgressBar;

    CountDownTimer cdt;
    AttendanceManager attendanceManager;
    User u;
    Location userLocation;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    PendingIntent pendingIntent;
    PendingIntent pendingIntent1;

    private long mTimeLeftInMillis = 30000;
    private long timeUserTappedIn;
    private boolean userTappedIn;
    private boolean timerIsRunning;
    private boolean userIsStudent;
    private int classAttendance;
    private int campusAttendance;
    private String userId;
    private String userName;
    private HashMap<String, String> campusAttendanceDaysCheck;
    private HashMap<String, HashMap<String, String>> classAttendanceDaysCheck = new HashMap<>();
    private String currentDateString;
    private String currentTimeString;
    private ArrayList<Class> classes;
    private ArrayList<PendingIntent> pendingIntents;
    private Class cp3408L;
    private Class cp3408P;

    private LatLng ne = new LatLng(1.316537, 103.876634);
    private LatLng sw = new LatLng(1.315058, 103.875267);
    private LatLngBounds JCU = new LatLngBounds(sw, ne);
    private LocationManager locationManager;
    private AlarmManager alarmManager;


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

        pendingIntents = new ArrayList<>();
        classes = new ArrayList<>();

        // test outside 1.316671, 103.875865
        // test inside 1.315759, 103.876270


        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 3);
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            userLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        if (getArguments() != null) {
            userName = getArguments().getString("user name");
            userId = getArguments().getString("user id");
            classAttendance = getArguments().getInt("class attendance");
            campusAttendance = getArguments().getInt("campus attendance");
        }

        countdownTimerLayout = myFragmentView.findViewById(R.id.countdown_timer_layout);

        userNameText = myFragmentView.findViewById(R.id.user_name);
        userNameText.setText(userName);
        userIdText = myFragmentView.findViewById(R.id.user_id);
        userIdText.setText("JCU ID: " + userId);

        classPercentageText = myFragmentView.findViewById(R.id.class_percentage);
        classPercentageText.setText(classAttendance + "%");
        classCircleProgressBar = myFragmentView.findViewById(R.id.class_circle_progress_bar);
        classCircleProgressBar.setProgress(classAttendance);
        if (classAttendance < 90){
            classCircleProgressBar.setColor(Color.parseColor("#ff6666"));
        }

        campusPercentageText = myFragmentView.findViewById(R.id.campus_percentage);
        campusPercentageText.setText(campusAttendance + "%");
        campusCircleProgressBar = myFragmentView.findViewById(R.id.campus_circle_progress_bar);
        campusCircleProgressBar.setProgress(campusAttendance);
        if(campusAttendance < 90){
            campusCircleProgressBar.setColor(Color.parseColor("#ff6666"));
        }


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
                campusAttendanceDaysCheck = user.getCampusAttendance();
                classes = user.getClasses();
                for (Class userClass : classes){
                    classAttendanceDaysCheck.put(userClass.getClassID(), userClass.getAttendance());
                }

                userTappedIn = u.getTappedIn();
                userIsStudent = user.getIsStudent();

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


    public interface OnRegisterNotificationListener{
        public void passPendingIntents(ArrayList<PendingIntent> pendingIntents);
    }

    OnRegisterNotificationListener registerNotificationListener;

    public interface OnAttendanceChangeListener {
        public void attendanceChange();
    }

    OnAttendanceChangeListener attendanceChangeListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            attendanceChangeListener = (OnAttendanceChangeListener) context;
            registerNotificationListener = (OnRegisterNotificationListener) context;
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
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
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
                    } else if (!campusAttendanceDaysCheck.get(currentDateString).equals("Null")) {
                        if (campusAttendanceDaysCheck.get(currentDateString).equals("True")) {
                            alertMessage("Today you have successfully tapped in and tapped out!");
                        } else {
                            alertMessage("Date error");
                        }
                    } else if (campusAttendanceDaysCheck.get(currentDateString).equals("Null")) {
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

                                        Intent notifyIntent = new Intent(getActivity(), NotificationReceiver.class);
                                        notifyIntent.putExtra("notification type", "campus att almost");
                                        pendingIntent = PendingIntent.getBroadcast
                                                (getContext(), 0, notifyIntent, 0);
                                        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                                SystemClock.elapsedRealtime() +
                                                        ((mTimeLeftInMillis - (System.currentTimeMillis() - timeUserTappedIn)) - 15000),
                                                pendingIntent);


                                        Intent notifyIntent1 = new Intent(getContext(), NotificationReceiver.class);
                                        notifyIntent1.putExtra("notification type", "campus att tap out");
                                        pendingIntent1 = PendingIntent.getBroadcast
                                                (getContext(), 1, notifyIntent1, PendingIntent.FLAG_UPDATE_CURRENT);
                                        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                                SystemClock.elapsedRealtime() +
                                                        ((mTimeLeftInMillis - (System.currentTimeMillis() - timeUserTappedIn))),
                                                pendingIntent1);

                                        userTappedIn = u.getTappedIn();

                                        pendingIntents.add(pendingIntent);
                                        pendingIntents.add(pendingIntent1);

                                        registerNotificationListener.passPendingIntents(pendingIntents);

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
                    HashMap<String, String> attendanceDaysCheck = classAttendanceDaysCheck.get(classID);

                    if (!userIsStudent){
                        if (attendanceDaysCheck.containsKey(currentDateString)){
                            if (checkingClass.getUserTappedIn()) {
                                if (LocalTime.parse(currentTimeString).
                                        isAfter(LocalTime.parse(getClassTapOutStartTime(checkingClass.getEndTime()))) &&
                                        LocalTime.parse(currentTimeString).
                                                isBefore(LocalTime.parse(getClassTapOutEndTime(checkingClass.getEndTime())))) {
                                    if (attendanceDaysCheck.get(currentDateString).equals("Null")) {
                                        attendanceManager.getUser(userId, new AttendanceManager.onGetUserListener() {
                                            @Override
                                            public void OnStart() {

                                            }

                                            @Override
                                            public void OnSuccess(User user) {
                                                for (Class userClass : user.getClasses()){
                                                    if (userClass.getClassID().equals(checkingClass.getClassID())){
                                                        userClass.putAttendance(currentDateString, "True");
                                                        userClass.setUserTappedIn(false);
                                                    }
                                                }
                                                for (Class userClass : classes){
                                                    if (userClass.getClassID().equals(checkingClass.getClassID())){
                                                        userClass.setUserTappedIn(false);
                                                    }
                                                }
                                                attendanceManager.updateUser(userId, user);
                                                alertMessage("Successfully tapped out for " + checkingClass.getClassID());
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        attendanceChangeListener.attendanceChange();
                                                    }
                                                }, 1000);
                                            }

                                            @Override
                                            public void OnFailure() {

                                            }
                                        });

                                    } else {
                                        alertMessage("Date error");
                                    }
                                } else {
                                    alertMessage("Not the time for tap out yet");
                                }
                            }
                            else {
                                if (LocalTime.parse(currentTimeString).
                                        isAfter(LocalTime.parse(getClassTapInStartTime(checkingClass.getStartTime()))) &&
                                        LocalTime.parse(currentTimeString).
                                                isBefore(LocalTime.parse(getClassTapInEndTime(checkingClass.getStartTime())))) {
                                    if (attendanceDaysCheck.get(currentDateString).equals("Null")) {
                                        attendanceManager.getUser(userId, new AttendanceManager.onGetUserListener() {
                                            @Override
                                            public void OnStart() {

                                            }

                                            @Override
                                            public void OnSuccess(User user) {
                                                for (Class userClass : user.getClasses()){
                                                    if (userClass.getClassID().equals(checkingClass.getClassID())){
                                                        userClass.setUserTappedIn(true);
                                                    }
                                                }
                                                for (Class userClass : classes){
                                                    if (userClass.getClassID().equals(checkingClass.getClassID())){
                                                        userClass.setUserTappedIn(true);
                                                    }
                                                }
                                                attendanceManager.updateUser(userId, user);
                                                alertMessage("Successfully tapped in for " + checkingClass.getClassID());
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
                                    if (attendanceDaysCheck.get(currentDateString).equals("Null")) {
                                        alertMessage("Not the time for tap in yet");
                                    }
                                    else if (attendanceDaysCheck.get(currentDateString).equals("True")) {
                                        alertMessage("Today you have successfully tapped in and tapped out for "
                                                + checkingClass.getClassID());
                                    }
                                    else {
                                        alertMessage("Date error");
                                    }
                                }
                            }
                        }
                        else {
                            alertMessage("Date error");
                        }
                    }
                    else {
                        if (attendanceDaysCheck.containsKey(currentDateString)) {
                            if (LocalTime.parse(currentTimeString).
                                    isAfter(LocalTime.parse(getClassTapInStartTime(checkingClass.getStartTime()))) &&
                                    LocalTime.parse(currentTimeString).
                                            isBefore(LocalTime.parse(getClassTapInEndTime(checkingClass.getStartTime())))) {
                                if (attendanceDaysCheck.get(currentDateString).equals("Null")) {
                                    attendanceManager.getUser(userId, new AttendanceManager.onGetUserListener() {
                                        @Override
                                        public void OnStart() {

                                        }

                                        @Override
                                        public void OnSuccess(User user) {
                                            for (Class userClass : user.getClasses()){
                                                if (userClass.getClassID().equals(checkingClass.getClassID())){
                                                    userClass.putAttendance(currentDateString, "True");
                                                }
                                            }
                                            attendanceManager.updateUser(userId, user);
                                            alertMessage("Successfully tapped in for " + checkingClass.getClassID());
                                            Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    attendanceChangeListener.attendanceChange();
                                                }
                                            }, 1000);
                                        }

                                        @Override
                                        public void OnFailure() {

                                        }
                                    });
                                } else if (attendanceDaysCheck.get(currentDateString).equals("True")) {
                                    alertMessage("Today you have successfully tapped in for " + checkingClass.getClassID());
                                } else {
                                    alertMessage("Date error");
                                }
                            }
                            else {
                                if (attendanceDaysCheck.get(currentDateString).equals("Null")) {
                                    alertMessage("Not the time for tap in yet");
                                } else if (attendanceDaysCheck.get(currentDateString).equals("True")) {
                                    alertMessage("Today you have successfully tapped in for " + checkingClass.getClassID());
                                } else {
                                    alertMessage("Date error");
                                }
                            }
                        }
                        else {
                            alertMessage("Date error");
                        }
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
            if (campusAttendanceDaysCheck.get(currentDateString).equals("Null")) {
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
                                user.putCampusAttendance(currentDateString, "True");
                                alertMessage("Successfully Tapped Out!");
                                attendanceManager.updateUser(userId, user);
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        attendanceChangeListener.attendanceChange();
                                    }
                                }, 1000);
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
        Class checkingClass = new Class("", "", "", false);
        for(Class c : classes){
            if (c.getClassID().equals(classID))
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

    public String getClassTapInStartTime(String classStartTime){
        LocalTime lcStartTime = LocalTime.parse(classStartTime);
        lcStartTime = lcStartTime.minusMinutes(15);
        return lcStartTime.format(formatter);
    }

    public String getClassTapInEndTime(String classStartTime){
        LocalTime lcStartTime = LocalTime.parse(classStartTime);
        lcStartTime = lcStartTime.plusMinutes(15);
        return lcStartTime.format(formatter);
    }

    public String getClassTapOutStartTime(String classEndTime){
        LocalTime lcEndTime = LocalTime.parse(classEndTime);
        lcEndTime = lcEndTime.minusMinutes(30);
        return lcEndTime.format(formatter);
    }

    public String getClassTapOutEndTime(String classEndTime){
        LocalTime lcEndTime = LocalTime.parse(classEndTime);
        lcEndTime = lcEndTime.plusMinutes(30);
        return lcEndTime.format(formatter);
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, this);
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        locationManager.removeUpdates(this);
    }
}
