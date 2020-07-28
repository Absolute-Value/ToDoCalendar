package com.example.x3033076.finalextodocalendar;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

public class AlarmReceiver extends BroadcastReceiver {
    public static String NOTIFICATION_ID = "notificationId";
    public static String NOTIFICATION_HEADER = "header";
    public static String NOTIFICATION_TITLE = "title";

    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra(NOTIFICATION_ID, 0); // 引き渡されたidを変数に保存
        String header = intent.getStringExtra(NOTIFICATION_HEADER); // 引き渡されたヘッダーを変数に保存
        String title = intent.getStringExtra(NOTIFICATION_TITLE); // 引き渡されたタイトルを変数に保存
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // SDkがOreo以上だったら
            NotificationChannel mChannel = new NotificationChannel("id_"+id, "name_"+id, NotificationManager.IMPORTANCE_HIGH);
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.createNotificationChannel(mChannel);
            mNotificationManager.notify(id , buildNotification(context, id, header, title));
        } else { // SDkがOreo未満だったら
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(id, buildNotification(context, header, title));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O) // Oreo以上
    private Notification buildNotification(Context context, int id, String header, String title) {
        Notification.Builder builder = new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_action_name) // アイコンセット
                .setContentTitle(header) // ヘッダーセット
                .setContentText(title) // タイトルセット
                .setChannelId("id_"+id); // id設定
        return builder.build();
    }

    private Notification buildNotification(Context context, String header, String title) {
        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle(header) // ヘッダーセット
                .setContentText(title) // タイトルセット
                .setSmallIcon(R.drawable.ic_action_name); // アイコンセット
        return builder.build();
    }
}