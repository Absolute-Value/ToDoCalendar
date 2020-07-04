package com.example.x3033076.finalextodocalendar;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    public static String NOTIFICATION_ID = "notificationId";
    public static String NOTIFICATION_HEADER = "header";
    public static String NOTIFICATION_TITLE = "title";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        Log.d("id",""+id);
        String header = intent.getStringExtra(NOTIFICATION_HEADER);
        String title = intent.getStringExtra(NOTIFICATION_TITLE);
        notificationManager.notify(id, buildNotification(context, header, title));
    }

    private Notification buildNotification(Context context, String header, String title) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(header)
                .setContentText(title)
                .setSmallIcon(android.R.drawable.star_on);

        return builder.build();
    }
}