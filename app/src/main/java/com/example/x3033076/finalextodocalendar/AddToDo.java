package com.example.x3033076.finalextodocalendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddToDo extends FragmentActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    TextView toDoTitleTextView, toDoHeaderTextView, toDoMemoTextView;
    Button dateButton, timeButton, cancelButton, addButton;
    static Button setColorButton;
    static int color;
    Resources addResource;
    Date now;
    int year, month, day, hour, minute;

    private ArrayList<Map<String, Object>> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_add_layout);

        toDoTitleTextView = findViewById(R.id.toDoTitleTV);
        toDoHeaderTextView = findViewById(R.id.toDoHeaderTV);
        setColorButton = findViewById(R.id.toDoColorBtn);
        dateButton = findViewById(R.id.dateBtn);
        timeButton = findViewById(R.id.timeBtn);
        toDoMemoTextView = findViewById(R.id.toDoMemoTV);
        cancelButton = findViewById(R.id.cancelBtn);
        addButton = findViewById(R.id.addBtn);

        // リスナ登録
        cancelButton.setOnClickListener(this);
        addButton.setOnClickListener(this);

        addResource = getResources();
        color = addResource.getColor(R.color.colorLightBlue);

        now = new Date();
        year = now.getYear() + 1900;
        month = now.getMonth() + 1;
        day = now.getDate();
        hour = now.getHours();
        minute = now.getMinutes();
        dateButton.setText(year + "/" + month + "/" + day);
        timeButton.setText(hour + ":" + String.format("%02d", minute));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addBtn: // "追加" ボタンが押されたら
                String title = toDoTitleTextView.getText().toString();
                String header = toDoHeaderTextView.getText().toString();
                String deadline = String.format("%04d", year) + String.format("%02d", month) + String.format("%02d", day)
                        + String.format("%02d", hour) + String.format("%02d", minute);
                String memo = toDoMemoTextView.getText().toString();
                if (title.equals("")) {
                    Toast.makeText(AddToDo.this, "タイトルを入力してください", Toast.LENGTH_SHORT).show();
                } else {
                    DBAdapter dbAdapter = new DBAdapter(this);
                    dbAdapter.openDB();
                    dbAdapter.saveDB(title, header, deadline, memo, color);
                    dbAdapter.closeDB();
                    finish(); // このアクティビティを終了させる
                }
                break;
            case R.id.cancelBtn: // "キャンセル" ボタンが押されたら
                finish(); // このアクティビティを終了させる
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int setYear, int setMonth, int setDay) {
        year = setYear;
        month = setMonth + 1;
        day = setDay;
        dateButton.setText(year + "/" + month + "/" + day);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int setHour, int setMinute) {
        hour = setHour;
        minute = setMinute;
        timeButton.setText(hour + ":" + String.format("%02d", minute));
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerDialogFragment(); // フラグメントの作成
        Bundle args = new Bundle();
        args.putInt("year", year); // 引数 "年"
        args.putInt("month", month); // 引数 "月"
        args.putInt("day", day); // 引数 "日"
        newFragment.setArguments(args);// 引数をフラグメントにセット
        newFragment.show(getSupportFragmentManager(), "datePicker"); // フラグメント起動
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerDialogFragment(); // フラグメントの作成
        Bundle args = new Bundle();
        args.putInt("hour", hour); // 引数 "時"
        args.putInt("minute", minute); // 引数 "分"
        newFragment.setArguments(args); // 引数をフラグメントにセット
        newFragment.show(getSupportFragmentManager(), "TimePicker"); // フラグメント起動
    }

    public void showColorSetDialog(View v) {
        DialogFragment newFragment = new ColorSetDialogFragment(); // フラグメントの作成
        newFragment.show(getSupportFragmentManager(), "ColorSet"); // フラグメント起動
    }
}
