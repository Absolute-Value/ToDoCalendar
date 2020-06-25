package com.example.x3033076.finalextodocalendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditToDo extends FragmentActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    TextView toDoTitleTextView, toDoHeaderTextView, toDoMemoTextView;
    Button dateButton, timeButton, cancelButton, addButton;
    static Button setColorButton;

    String id, title, header, memo;
    int year, month, day, hour, minute;
    static int color;

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
        addButton.setText("変更"); // "追加"ボタンを"変更"ボタンに変更

        // リスナ登録
        addButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        Intent intent = getIntent();

        id = intent.getStringExtra("id");
        title = intent.getStringExtra("title");
        header = intent.getStringExtra("header");
        color = intent.getIntExtra("color", Color.WHITE);
        year = intent.getIntExtra("year", 1999);
        month = intent.getIntExtra("month", 12);
        day = intent.getIntExtra("day", 20);
        hour = intent.getIntExtra("hour", 12);
        minute = intent.getIntExtra("minute", 20);
        memo = intent.getStringExtra("memo");
        toDoTitleTextView.setText(title);
        toDoHeaderTextView.setText(header);
        setColorButton.setBackgroundColor(color);
        dateButton.setText(year + "/" + month + "/" + day);
        timeButton.setText(hour + ":" + String.format("%02d", minute));
        toDoMemoTextView.setText(memo);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addBtn: // "変更" ボタンが押されたら
                title = toDoTitleTextView.getText().toString();
                header = toDoHeaderTextView.getText().toString();
                String deadline = String.format("%04d", year) + String.format("%02d", month) + String.format("%02d", day)
                        + String.format("%02d", hour) + String.format("%02d", minute);
                memo = toDoMemoTextView.getText().toString();
                if (title.equals("")) {
                    Toast.makeText(this, "タイトルを入力してください", Toast.LENGTH_SHORT).show();
                } else {
                    DBAdapter dbAdapter = new DBAdapter(this);
                    dbAdapter.openDB();
                    dbAdapter.selectDelete(id);
                    dbAdapter.saveDB(title, header, deadline, memo, color);

                    // DBのデータを取得
                    String[] columns = {DBAdapter.COL_TITLE, DBAdapter.COL_HEADER, DBAdapter.COL_DEADLINE, DBAdapter.COL_COLOR}; // DBのカラム：ToDo名
                    Cursor c = dbAdapter.getDB(columns);

                    int listYear, listMonth, listDay;
                    Calendar calendar = Calendar.getInstance();

                    if (c.moveToFirst()) {
                        do {
                            listYear = Integer.parseInt(c.getString(2).substring(0, 4));
                            listMonth = Integer.parseInt(c.getString(2).substring(4, 6));
                            listDay = Integer.parseInt(c.getString(2).substring(6, 8));
                            calendar.set(listYear, listMonth - 1, listDay);
                            Map<String, Object> map = new HashMap<>();
                            map.put("title", c.getString(0));
                            map.put("header", c.getString(1));
                            map.put("date", c.getString(2).substring(0,8));
                            map.put("time", c.getString(2).substring(8, 10) + ":" + c.getString(2).substring(10, 12));
                            map.put("color", c.getString(3));
                            list.add(map);
                        } while (c.moveToNext());
                    }
                    c.close();
                    dbAdapter.closeDB(); // DBを閉じる

                    SimpleAdapter adapter = new MyAdapter(this, list, R.layout.list_layout, new String[]{"title", "header", "date", "time", "color"},
                            new int[]{R.id.listTitleTV, R.id.listHeaderTV, R.id.listDateTV, R.id.listTimeTV, R.id.listColorTV});
                    ToDoListFragment.listV.setAdapter(adapter); //ListViewにアダプターをセット(=表示)

                    ToDoListFragment.editMode = false;
                    finish(); // このアクティビティを終了させる
                }
                break;
            case R.id.cancelBtn: // "キャンセル" ボタンが押されたら
                finish();
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

    public void showDatePickerDialog(View v) { // dateBtnが押されたとき
        DialogFragment newFragment = new DatePickerDialogFragment(); // フラグメントの作成
        Bundle args = new Bundle();
        args.putInt("year", year); // 引数 "年"
        args.putInt("month", month); // 引数 "月"
        args.putInt("day", day); // 引数 "日"
        newFragment.setArguments(args);// 引数をフラグメントにセット
        newFragment.show(getSupportFragmentManager(), "datePicker"); // フラグメント起動
    }

    public void showTimePickerDialog(View v) { // timeBtnが押されたとき
        DialogFragment newFragment = new TimePickerDialogFragment(); // フラグメントの作成
        Bundle args = new Bundle();
        args.putInt("hour", hour); // 引数 "時"
        args.putInt("minute", minute); // 引数 "分"
        newFragment.setArguments(args);// 引数をフラグメントにセット
        newFragment.show(getSupportFragmentManager(), "TimePicker"); // フラグメント起動
    }

    public void showColorSetDialog(View v) { // toDoColorBtnが押されたとき
        DialogFragment newFragment = new ColorSetDialogFragment(); // フラグメントの作成
        newFragment.show(getSupportFragmentManager(), "ColorSet"); // フラグメント起動
    }
}
