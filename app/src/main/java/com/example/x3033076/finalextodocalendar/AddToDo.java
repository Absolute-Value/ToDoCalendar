package com.example.x3033076.finalextodocalendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import java.util.Date;

public class AddToDo extends FragmentActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    TextView toDoTitleTextView, memoTextView;
    Button dateButton, timeButton, cancelButton, addButton;
    Date now;
    int year, month, day, hour, minute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_add_layout);

        toDoTitleTextView = findViewById(R.id.toDoTitleTV);
        dateButton = findViewById(R.id.dateBtn);
        timeButton = findViewById(R.id.timeBtn);
        memoTextView = findViewById(R.id.memoTV);
        cancelButton = findViewById(R.id.cancelBtn);
        addButton = findViewById(R.id.addBtn);

        // リスナ登録
        toDoTitleTextView.setOnClickListener(this);
        memoTextView.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        addButton.setOnClickListener(this);

        now = new Date();
        year = now.getYear()+1900;
        month = now.getMonth();
        day = now.getDate();
        hour = now.getHours();
        minute = now.getMinutes();
        dateButton.setText(year+"/"+month+"/"+day);
        timeButton.setText(hour+":"+String.format("%02d",minute));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addBtn:
                ToDoList.adapter.add("["+year+"年"+month+"月"+day+"日"+
                        hour+":"+String.format("%02d",minute)+"] "+String.valueOf(toDoTitleTextView.getText()));
            case R.id.cancelBtn:
                finish(); // このアクティビティを終了させる
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int setYear, int setMonth, int setDay) {
        year = setYear;
        month = setMonth+1;
        day = setDay;
        dateButton.setText(year+"/"+month+"/"+day);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int setHour, int setMinute) {
        hour = setHour;
        minute = setMinute;
        timeButton.setText(hour+":"+String.format("%02d",minute));
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerDialogFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public  void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerDialogFragment();
        newFragment.show(getSupportFragmentManager(), "TimePicker");
    }
}
