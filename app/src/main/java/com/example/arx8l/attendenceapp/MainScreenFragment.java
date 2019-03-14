/*This fragment represents the app main screen
* Code by Tung*/
package com.example.arx8l.attendenceapp;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.support.v4.app.NotificationCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
    TextView hourMinuteSecondText;
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
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private long mTimeLeftInMillis = 30000;
    private long timeUserTappedIn;
    private boolean outsideCampusNotificationFiredOff = false;
    private boolean userTappedIn;
    private boolean timerIsRunning;
    private boolean userIsStudent;
    private int classAttendancePercentage;
    private int campusAttendancePercentage;
    private String userId;
    private String userName;
    private HashMap<String, String> campusAttendance;
    private HashMap<String, HashMap<String, String>> classAttendance;
    private String currentDateString;
    private String currentTimeString;
    private ArrayList<Class> classes;
    private HashMap<String, PendingIntent> notificationIntents;

    private LatLng ne = new LatLng(1.316537, 103.876634);
    private LatLng sw = new LatLng(1.315058, 103.875267);

    //Draw the rectangle bound around JCU in google map using the south-west position and the north-east position
    private LatLngBounds jcuBounds = new LatLngBounds(sw, ne);
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

        preferences = getActivity().getSharedPreferences("notification", MODE_PRIVATE);
        editor  =  getActivity().getSharedPreferences("notification", MODE_PRIVATE).edit();

        outsideCampusNotificationFiredOff = preferences.getBoolean("outsideCampusNotificationFiredOff", false);

        classes = new ArrayList<>();
        classAttendance = new HashMap<>();

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
            classAttendancePercentage = getArguments().getInt("class attendance");
            campusAttendancePercentage = getArguments().getInt("campus attendance");
            notificationIntents = (HashMap<String, PendingIntent>) getArguments().getSerializable("notification intents");
        }

        countdownTimerLayout = myFragmentView.findViewById(R.id.countdown_timer_layout);

        userNameText = myFragmentView.findViewById(R.id.user_name);
        userNameText.setText(userName);
        userIdText = myFragmentView.findViewById(R.id.user_id);
        userIdText.setText("JCU ID: " + userId);

        classPercentageText = myFragmentView.findViewById(R.id.class_percentage);
        classPercentageText.setText(classAttendancePercentage + "%");
        classCircleProgressBar = myFragmentView.findViewById(R.id.class_circle_progress_bar);
        classCircleProgressBar.setProgress(classAttendancePercentage);
        if (classAttendancePercentage < 90){
            classCircleProgressBar.setColor(Color.parseColor("#ff6666"));
        }

        campusPercentageText = myFragmentView.findViewById(R.id.campus_percentage);
        campusPercentageText.setText(campusAttendancePercentage + "%");
        campusCircleProgressBar = myFragmentView.findViewById(R.id.campus_circle_progress_bar);
        campusCircleProgressBar.setProgress(campusAttendancePercentage);
        if(campusAttendancePercentage < 90){
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
        hourMinuteSecondText = new TextView(getContext());

        countDownTimerText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,1f));
        hourMinuteSecondText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,1f));

        countDownTimerText.setTextSize(20);
        countDownTimerText.setTextColor(Color.BLACK);
        countDownTimerText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        countDownTimerText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
        countDownTimerText.setPadding(0,0,0,5);

        hourMinuteSecondText.setTextSize(24);
        hourMinuteSecondText.setTextColor(Color.BLACK);
        hourMinuteSecondText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        hourMinuteSecondText.setPadding(0,0,0,5);

        String cdtText = "Tapping Out in: ";
        String hmsText = "          ";

        SpannableString ss = new SpannableString(cdtText);
        ForegroundColorSpan fcsRed = new ForegroundColorSpan(Color.RED);
        ss.setSpan(fcsRed, 8, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        countDownTimerText.setText(ss);
        hourMinuteSecondText.setText(hmsText);

        tapOutBtt.setVisibility(View.GONE);

        attendanceManager = new AttendanceManager();

        attendanceManager.getUser(userId, new AttendanceManager.onGetUserListener() {
            @Override
            public void OnStart() {

            }

            @Override
            public void OnSuccess(User user) {
                u = user;
                campusAttendance = user.getCampusAttendance();
                classes = user.getClasses();
                for (Class userClass : classes){
                    classAttendance.put(userClass.getClassID(), userClass.getAttendance());
                    if (userClass.getUserTappedIn()){
                        setTapOutClassNotification(userClass);
                    }
                }

                userTappedIn = u.getTappedIn();
                userIsStudent = user.getIsStudent();

                if(userTappedIn && cdt == null){
                    countdownTimerLayout.addView(countDownTimerText);
                    countdownTimerLayout.addView(hourMinuteSecondText);
                    Date date = new Date();
                    try {
                        date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(u.getTapInTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    timeUserTappedIn = date.getTime();
                    setTapOutCampusNotification();

                    startTimer();
                }
                else if (cdt != null && !timerIsRunning){
                    tapOutBtt.setVisibility(View.VISIBLE);
                }
                else if (userTappedIn){
                    countdownTimerLayout.addView(countDownTimerText);
                    countdownTimerLayout.addView(hourMinuteSecondText);
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
        public void passNotificationPendingIntents(HashMap<String, PendingIntent> notificationPendingIntents);
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
        if (!timerIsRunning && cdt != null && userTappedIn) {
            if (!outsideCampusNotificationFiredOff) {
                if (!jcuBounds.contains(new LatLng(userLocation.getLatitude(), userLocation.getLongitude()))) {
                    Intent intent1 = new Intent(EasyAtt.getContext(), MainActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PendingIntent pendingIntent = PendingIntent.getActivity(EasyAtt.getContext(), 0, intent1, 0);
                    NotificationManager notif = (NotificationManager) EasyAtt.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(EasyAtt.getContext().getApplicationContext(), "testChannel")
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle("JCU Attendance Notification")
                            .setContentText("Oh no! You forgot to tap out for your attendance!")
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true);
                    notif.notify(10, mBuilder.build());
                    outsideCampusNotificationFiredOff = true;
                    editor.putBoolean("outsideCampusNotificationFiredOff", outsideCampusNotificationFiredOff);
                    editor.apply();
                }
            }
        }
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

        if (jcuBounds.contains(new LatLng(userLocation.getLatitude(), userLocation.getLongitude()))) {
            if (requestCode == 1 && resultCode == 0 && data != null) {
                String scanResult = data.getStringExtra("scan result");

                if (scanResult.equals("JCU QR Code Attendance")) {
                    if (timerIsRunning) {
                        alertMessage("Not Yet");
                    } else if (tapOutBtt.getVisibility() == View.VISIBLE) {
                        tapOutForCampus(scanResult);
                    } else if (!campusAttendance.containsKey(currentDateString)) {
                        alertMessage("Date error");
                    } else if (!campusAttendance.get(currentDateString).equals("Null")) {
                        if (campusAttendance.get(currentDateString).equals("True")) {
                            alertMessage("Today you have successfully tapped in and tapped out!");
                        } else {
                            alertMessage("Date error");
                        }
                    } else if (campusAttendance.get(currentDateString).equals("Null")) {
                        Intent successIntent = new Intent(getContext(), SuccessPage.class);
                        successIntent.putExtra("message", "You have successfully tapped in!");
                        getActivity().startActivity(successIntent);
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

                                        countdownTimerLayout.addView(countDownTimerText);
                                        countdownTimerLayout.addView(hourMinuteSecondText);
                                        Date date = new Date();
                                        try {
                                            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(u.getTapInTime());
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        timeUserTappedIn = date.getTime();

                                        setTapOutCampusNotification();

                                        userTappedIn = u.getTappedIn();

                                        registerNotificationListener.passNotificationPendingIntents(notificationIntents);

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
                } else if (classAttendance.containsKey(scanResult)) {
                    String classID = scanResult;
                    Class checkingClass = getCheckingClass(classID);
                    HashMap<String, String> attendanceDaysCheck = classAttendance.get(classID);

                    if (!userIsStudent){
                        if (attendanceDaysCheck.containsKey(currentDateString)){
                            if (checkingClass.getUserTappedIn()) {
                                if (LocalTime.parse(currentTimeString).
                                        isAfter(LocalTime.parse(getClassTapOutStartTime(checkingClass.getEndTime()))) &&
                                        LocalTime.parse(currentTimeString).
                                                isBefore(LocalTime.parse(getClassTapOutEndTime(checkingClass.getEndTime())))) {
                                    if (attendanceDaysCheck.get(currentDateString).equals("Null")) {
                                        Intent successIntent = new Intent(getContext(), SuccessPage.class);
                                        successIntent.putExtra("message",
                                                "You have successfully tapped out for " + checkingClass.getClassID() + "!");
                                        getActivity().startActivity(successIntent);

                                        notificationIntents.get(checkingClass.getClassID() + " tap out").cancel();
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
                                        Intent successIntent = new Intent(getContext(), SuccessPage.class);
                                        successIntent.putExtra("message",
                                                "You have successfully tapped in for " + checkingClass.getClassID() + "!");
                                        getActivity().startActivity(successIntent);

                                        notificationIntents.get(checkingClass.getClassID() + " tap in").cancel();

                                        setTapOutClassNotification(checkingClass);

                                        registerNotificationListener.passNotificationPendingIntents(notificationIntents);

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
                                    Intent successIntent = new Intent(getContext(), SuccessPage.class);
                                    successIntent.putExtra("message",
                                            "You have successfully tapped in for " + checkingClass.getClassID() + "!");
                                    getActivity().startActivity(successIntent);

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
                String result = data.getStringExtra("scan result");
                tapOutForCampus(result);
            }
        }
        else {
            alertMessage("You have to be inside JCU to tap in");
        }
    }

    private void tapOutForCampus(String scanResult){
        if (scanResult.equals("JCU QR Code Attendance")) {
            if (campusAttendance.get(currentDateString).equals("Null")) {
                Intent successIntent = new Intent(getContext(), SuccessPage.class);
                successIntent.putExtra("message", "You have successfully tapped out!");
                getActivity().startActivity(successIntent);
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

    private void setTapOutCampusNotification(){
        Intent notifyIntent = new Intent(getActivity(), NotificationReceiver.class);
        notifyIntent.putExtra("notification type", "campus att almost");
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent notificationPendingIntent = PendingIntent.getBroadcast
                (getContext(), 0, notifyIntent,0);
        if ((mTimeLeftInMillis - (System.currentTimeMillis() - timeUserTappedIn)) > 15000) {
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() +
                            ((mTimeLeftInMillis - (System.currentTimeMillis() - timeUserTappedIn)) - 15000),
                    notificationPendingIntent);
        }

        Intent notifyIntent1 = new Intent(getContext(), NotificationReceiver.class);
        notifyIntent1.putExtra("notification type", "campus att tap out");
        notifyIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent notificationPendingIntent1 = PendingIntent.getBroadcast
                (getContext(), 1, notifyIntent1, 0);
        if ((mTimeLeftInMillis - (System.currentTimeMillis() - timeUserTappedIn)) > 0) {
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() +
                            ((mTimeLeftInMillis - (System.currentTimeMillis() - timeUserTappedIn))),
                    notificationPendingIntent1);
        }

        notificationIntents.put("campus tap out almost" ,notificationPendingIntent);
        notificationIntents.put("campus tap out" ,notificationPendingIntent1);
    }

    private void setTapOutClassNotification(Class checkingClass){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int classEndTimeHour = Integer.parseInt(checkingClass.getEndTime().substring(0,2));
        int classEndTimeMinute = Integer.parseInt(checkingClass.getEndTime().substring(3));

        if (LocalTime.now().isBefore(LocalTime.parse(checkingClass.getEndTime()))) {
            calendar.set(Calendar.HOUR_OF_DAY, classEndTimeHour);
            calendar.set(Calendar.MINUTE, classEndTimeMinute);
            calendar.set(Calendar.SECOND, 00);
            Intent notifyIntent = new Intent(getActivity(), NotificationReceiver.class);
            notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            notifyIntent.putExtra("notification type", "class tap out");
            notifyIntent.putExtra("class id", checkingClass.getClassID());
            PendingIntent notificationPendingIntent = PendingIntent.getBroadcast
                    (getContext(), 2, notifyIntent, 0);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    notificationPendingIntent);
            notificationIntents.put(checkingClass.getClassID() + " tap out" ,notificationPendingIntent);
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

        cdt = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerIsRunning = true;
                mTimeLeftInMillis = millisUntilFinished;
                Log.i(TAG, "Countdown seconds remaining: " + millisUntilFinished / 1000);
                long hour = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                long minute = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished));
                long second = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));

                String hms = String.format("  %02d:%02d:%02d", hour, minute, second);
                hourMinuteSecondText.setText(hms);
            }

            @Override
            public void onFinish() {
                timerIsRunning = false;
                countdownTimerLayout.removeView(countDownTimerText);
                countdownTimerLayout.removeView(hourMinuteSecondText);
                tapOutBtt.setVisibility(View.VISIBLE);
            }
        };
        cdt.start();
    }

    public String getClassTapInStartTime(String classStartTime){
        LocalTime lcStartTime = LocalTime.parse(classStartTime);
        lcStartTime = lcStartTime.minusMinutes(15);
        return lcStartTime.format(timeFormatter);
    }

    public String getClassTapInEndTime(String classStartTime){
        LocalTime lcStartTime = LocalTime.parse(classStartTime);
        lcStartTime = lcStartTime.plusMinutes(15);
        return lcStartTime.format(timeFormatter);
    }

    public String getClassTapOutStartTime(String classEndTime){
        LocalTime lcEndTime = LocalTime.parse(classEndTime);
        lcEndTime = lcEndTime.minusMinutes(30);
        return lcEndTime.format(timeFormatter);
    }

    public String getClassTapOutEndTime(String classEndTime){
        LocalTime lcEndTime = LocalTime.parse(classEndTime);
        lcEndTime = lcEndTime.plusMinutes(30);
        return lcEndTime.format(timeFormatter);
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
