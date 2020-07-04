package com.example.x3033076.finalextodocalendar;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
                NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.cancelAll();

        }
    }
}
