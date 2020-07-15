package com.example.x3033076.finalextodocalendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class SettingsFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Switch day2beforeSwitch, day1beforeSwitch, hour3beforeSwitch, hour1beforeSwitch, justSwitch;
    private Button notificationButton;
    private View setRootView;

    private String DB_TABLE = "switchBool";   // DBのテーブル名
    private String COL_ID = "_id";            // id
    private String COL_BOOL = "bool";       // タイトル

    private SQLiteDatabase db = null;           // SQLiteDatabase
    private DBHelper dbHelper = null;           // DBHepler

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setRootView = inflater.inflate(R.layout.setting_layout, container, false);

        day2beforeSwitch = setRootView.findViewById(R.id.day2beforeSW);
        day1beforeSwitch = setRootView.findViewById(R.id.day1beforeSW);
        hour3beforeSwitch = setRootView.findViewById(R.id.hour3beforeSW);
        hour1beforeSwitch = setRootView.findViewById(R.id.hour1beforeSW);
        justSwitch = setRootView.findViewById(R.id.justTimeSW);
        notificationButton = setRootView.findViewById(R.id.notificationBtn);

        day2beforeSwitch.setOnCheckedChangeListener(this);
        day1beforeSwitch.setOnCheckedChangeListener(this);
        hour3beforeSwitch.setOnCheckedChangeListener(this);
        hour1beforeSwitch.setOnCheckedChangeListener(this);
        justSwitch.setOnCheckedChangeListener(this);
        notificationButton.setOnClickListener(this);

        dbHelper = new DBHelper(this.getContext());
        db = dbHelper.getReadableDatabase();

        String[] columns = {COL_BOOL}; // DBのカラム：ToDo名
        Cursor c = db.query(DB_TABLE, columns, null, null, null, null, null);
        int count = 0;
        if (c.moveToFirst()) {
            do {
                switch (count) {
                    case 0:
                        day2beforeSwitch.setChecked(loadBool(c.getInt(0)));
                        ToDoListFragment.day2SwitchBool = loadBool(c.getInt(0));
                        break;
                    case 1:
                        day1beforeSwitch.setChecked(loadBool(c.getInt(0)));
                        ToDoListFragment.day1SwitchBool = loadBool(c.getInt(0));
                        break;
                    case 2:
                        hour3beforeSwitch.setChecked(loadBool(c.getInt(0)));
                        ToDoListFragment.hour3SwitchBool = loadBool(c.getInt(0));
                    case 3:
                        hour1beforeSwitch.setChecked(loadBool(c.getInt(0)));
                        ToDoListFragment.hour1SwitchBool = loadBool(c.getInt(0));
                    default:
                        justSwitch.setChecked(loadBool(c.getInt(0)));
                        ToDoListFragment.justSwitchBool = loadBool(c.getInt(0));
                        break;
                }
                count++;
            } while (c.moveToNext());
        }
        c.close();
        db.close();

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
                Log.d("time", "" + calendar.getTime());
        }
    }

    private void scheduleNotification(String header, String title, Calendar calendar){
        Intent notificationIntent = new Intent(getActivity(), AlarmReceiver.class)
                .putExtra(AlarmReceiver.NOTIFICATION_ID, 0)
                .putExtra(AlarmReceiver.NOTIFICATION_HEADER, header)
                .putExtra(AlarmReceiver.NOTIFICATION_TITLE, title);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= 23) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= 19) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        db = dbHelper.getWritableDatabase();
        db.delete(DB_TABLE, null, null);
        saveDB(day2beforeSwitch.isChecked());
        ToDoListFragment.day2SwitchBool = day2beforeSwitch.isChecked();
        saveDB(day1beforeSwitch.isChecked());
        ToDoListFragment.day1SwitchBool = day1beforeSwitch.isChecked();
        saveDB(hour3beforeSwitch.isChecked());
        ToDoListFragment.hour3SwitchBool = hour3beforeSwitch.isChecked();
        saveDB(hour1beforeSwitch.isChecked());
        ToDoListFragment.hour1SwitchBool = hour1beforeSwitch.isChecked();
        saveDB(justSwitch.isChecked());
        ToDoListFragment.justSwitchBool = justSwitch.isChecked();
        db.close();
    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, "switchBool.db", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String createTbl = "CREATE TABLE " + DB_TABLE + " ("
                    + COL_ID + " INTEGER PRIMARY KEY,"
                    + COL_BOOL + " INT NOT NULL default 0"
                    + ");";

            db.execSQL(createTbl); //SQL文の実行
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public void saveDB(boolean bool) {
        int bol;
        if (bool) bol = 1;
        else bol = 0;
        db.beginTransaction(); // トランザクション開始

        try {
            ContentValues values = new ContentValues(); // ContentValuesでデータを設定していく
            values.put(COL_BOOL, bol);

            db.insert(DB_TABLE, null, values); // レコードへ登録

            db.setTransactionSuccessful();      // トランザクションへコミット
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction(); // トランザクションの終了
        }
    }

    private boolean loadBool(int getBool) {
        boolean getBol = false;
        if (getBool==1) getBol = true;
        return getBol;
    }
}
