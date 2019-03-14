/*This class handles pushing out notification
* Code by Tung*/
package com.example.arx8l.attendenceapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;

/**
 * Created by User on 8/1/2019.
 */

public class NotificationReceiver extends BroadcastReceiver {

    public NotificationReceiver(){

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String notificationType = intent.getExtras().getString("notification type");
        if (notificationType.equals("campus att almost")){
            Intent intent1 = new Intent(context, MainActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, 0);
            NotificationManager notif =(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context.getApplicationContext(), "testChannel")
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle("JCU Attendance Notification")
                    .setContentText("Hey! It's almost time to tap out.")
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            notif.notify(1, mBuilder.build());
        }
        else if (notificationType.equals("campus att tap out")){
            Intent intent1 = new Intent(context, MainActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, 0);
            NotificationManager notif =(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context.getApplicationContext(), "testChannel")
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle("JCU Attendance Notification")
                    .setContentText("Yay! You can now tap out!")
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            notif.notify(2, mBuilder.build());
        }
        else if (notificationType.equals("class tap out")){
            String classID = intent.getStringExtra("class id");
            Intent intent1 = new Intent(context, MainActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, 0);
            NotificationManager notif =(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context.getApplicationContext(), "testChannel")
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle("JCU Attendance Notification")
                    .setContentText("Hey! You can now tap out for " + classID + "!")
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            notif.notify(3, mBuilder.build());
        }
        else if (notificationType.equals("class tap in")){
            String classID = intent.getStringExtra("class id");
            Intent intent1 = new Intent(context, MainActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, 0);
            NotificationManager notif =(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context.getApplicationContext(), "testChannel")
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle("JCU Attendance Notification")
                    .setContentText("Oh no! You forgot to tap in for " + classID + "!")
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            notif.notify(4, mBuilder.build());
        }
        else if (notificationType.equals("location outside")){
            Intent intent1 = new Intent(context, MainActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, 0);
            NotificationManager notif = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context.getApplicationContext(), "testChannel")
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle("JCU Attendance Notification")
                    .setContentText("Oh no! You forgot to tap out for your attendance!")
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            notif.notify(10, mBuilder.build());
        }
    }
}

