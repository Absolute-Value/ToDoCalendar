package com.example.x3033076.finalextodocalendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import java.util.Date;

public class AddToDo extends FragmentActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    TextView toDoTitleTextView, toDoHeaderTextView, toDoMemoTextView;
    Button dateButton, timeButton, cancelButton, addButton;
    static Button setColorButton;
    static int color; // 色番号を保存しおく変数
    Resources addResource; // colors.xml を使えるようにする
    Date now;
    int year, month, day, hour, minute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_add_layout); // レイアウトを関連付け

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

        addResource = getResources(); // colors.xml を使えるようにする
        color = addResource.getColor(R.color.colorLightBlue); // 色の初期値を水色に

        now = new Date();
        year = now.getYear() + 1900; // 年を保存しおく変数
        month = now.getMonth() + 1; // 月を保存しおく変数
        day = now.getDate(); // 日を保存しおく変数
        hour = now.getHours(); // 時を保存しおく変数
        minute = now.getMinutes(); // 分を保存しおく変数
        dateButton.setText(year + "/" + month + "/" + day); // 日付ボタンに日付をセット
        timeButton.setText(hour + ":" + String.format("%02d", minute)); // 時間ボタンに時間をセット
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addBtn: // "追加" ボタンが押されたら
                String title = toDoTitleTextView.getText().toString(); // タイトルの内容を変数に保存
                String header = toDoHeaderTextView.getText().toString(); // 項目名の内容を変数に保存
                String deadline = String.format("%04d", year) + String.format("%02d", month) + String.format("%02d", day)
                        + String.format("%02d", hour) + String.format("%02d", minute); // 締め切りの内容を変数に保存
                String memo = toDoMemoTextView.getText().toString(); // メモの内容を変数に保存
                if (title.equals("")) { // タイトルが入力してなかったら
                    Toast.makeText(AddToDo.this, "タイトルを入力してください", Toast.LENGTH_SHORT).show(); // トーストでタイトルを入力するよう呼びかけ
                } else { // タイトルが入力してあったら
                    DBAdapter dbAdapter = new DBAdapter(this);
                    dbAdapter.openDB(); // DBの読み書き
                    dbAdapter.saveDB(title, header, deadline, memo, color); // 内容をデータベースに保存
                    dbAdapter.closeDB(); // DBを閉じる
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
        Bundle args = new Bundle(); // バンドル生成
        args.putInt("year", year); // 引数 "年"
        args.putInt("month", month); // 引数 "月"
        args.putInt("day", day); // 引数 "日"
        newFragment.setArguments(args);// 引数をフラグメントにセット
        newFragment.show(getSupportFragmentManager(), "datePicker"); // フラグメント起動
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerDialogFragment(); // フラグメントの作成
        Bundle args = new Bundle(); // バンドル生成
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
