package com.example.x3033076.finalextodocalendar;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    private Button notificationButton;
    private View setRootView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setRootView = inflater.inflate(R.layout.setting_layout, container, false);

        notificationButton = setRootView.findViewById(R.id.notificationBtn);

        notificationButton.setOnClickListener(this);

        return setRootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.notificationBtn:
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.add(Calendar.SECOND, 5);
                scheduleNotification("ToDoリスト付きカレンダー", "5秒後に届く通知です", calendar);
                Log.d("time",""+calendar.getTime());
        }
    }

    private void scheduleNotification(String header, String title, Calendar calendar){
        Intent notificationIntent = new Intent(getActivity(), AlarmReceiver.class);
        notificationIntent.putExtra(AlarmReceiver.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(AlarmReceiver.NOTIFICATION_HEADER, header);
        notificationIntent.putExtra(AlarmReceiver.NOTIFICATION_TITLE, title);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}
