package com.example.x3033076.finalextodocalendar;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToDoListFragment extends Fragment implements View.OnClickListener {

    ListView listV;
    Button finishButton, editButton, deleteButton;
    TextView toDoDate, toDoHeader, toDoTitle, toDoTime, toDoMemo;

    int getPosition;
    String getId, getDate, getTime;
    static String[] week_name = {"日", "月", "火", "水", "木", "金", "土"};
    static Boolean editMode = false;

    private Resources toDoFresource;
    private DBAdapter dbAdapter; // DBAdapter
    private List<Map<String,Object>> list = new ArrayList<>();

    private View toDoColorView, tdRootView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        tdRootView = inflater.inflate(R.layout.todo_list_layout, container, false);

        listV = (ListView) tdRootView.findViewById(R.id.toDoLV);
        toDoColorView = (View) tdRootView.findViewById(R.id.toDoColorV);
        toDoDate = (TextView) tdRootView.findViewById(R.id.dateTV);
        toDoHeader = (TextView) tdRootView.findViewById(R.id.headerTV);
        toDoTitle = (TextView) tdRootView.findViewById(R.id.titleTV);
        toDoTime = (TextView) tdRootView.findViewById(R.id.timeTV);
        toDoMemo = (TextView) tdRootView.findViewById(R.id.memoTV);
        finishButton = (Button) tdRootView.findViewById(R.id.finBtn);
        editButton = (Button) tdRootView.findViewById(R.id.editBtn);
        deleteButton = (Button) tdRootView.findViewById(R.id.delBtn);

        listV.setOnItemClickListener(new ListItemClickListener());
        finishButton.setOnClickListener(this);
        editButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);

        toDoFresource = tdRootView.getResources();
        listUpdate();

        return tdRootView;
    }

    void listUpdate() {
        dbAdapter = new DBAdapter(getActivity());
        dbAdapter.openDB(); // DBの読み込み(読み書きの方)

        // DBのデータを取得
        String[] columns = {DBAdapter.COL_TITLE, DBAdapter.COL_HEADER, DBAdapter.COL_DEADLINE, DBAdapter.COL_COLOR, DBAdapter.COL_ID}; // DBのカラム：ToDo名
        Cursor c = dbAdapter.getDB(columns);

        int listYear, listMonth, listDay, listHour, listMinute;
        Calendar calendar = Calendar.getInstance();

        list.clear(); // これがないとリスト増殖バグ発生

        if (c.moveToFirst()) {
            do {
                listYear  = Integer.parseInt(c.getString(2).substring(0,4));
                listMonth = Integer.parseInt(c.getString(2).substring(4,6));
                listDay   = Integer.parseInt(c.getString(2).substring(6,8));
                calendar.set(listYear, listMonth-1, listDay);
                Map<String,Object> map = new HashMap<>();
                map.put("title", c.getString(0));
                map.put("header", c.getString(1));
                map.put("year", listYear);
                map.put("date", c.getString(2).substring(0,8));
                map.put("time", c.getString(2).substring(8,10) + ":" + c.getString(2).substring(10,12));
                map.put("color", c.getString(3));
                list.add(map);
            } while (c.moveToNext());
        }
        c.close();
        dbAdapter.closeDB(); // DBを閉じる

        SimpleAdapter adapter = new MyAdapter(getActivity(), list, R.layout.list_layout, new String[]{"title", "header", "date", "time", "color", "year"},
                new int[]{R.id.listTitleTV, R.id.listHeaderTV, R.id.listDateTV, R.id.listTimeTV, R.id.listColorTV});
        listV.setAdapter(adapter); //ListViewにアダプターをセット(=表示)
    }

    private class ListItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            dbAdapter.openDB();
            String[] columns = {DBAdapter.COL_ID, DBAdapter.COL_TITLE, DBAdapter.COL_HEADER, DBAdapter.COL_DEADLINE, DBAdapter.COL_MEMO, DBAdapter.COL_COLOR}; // DBのカラム：ID, ToDo名, 期限, メモ
            Cursor c = dbAdapter.getDB(columns);
            getPosition = position + 1;
            c.move(getPosition);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Integer.valueOf(c.getString(3).substring(0,4)),
                    Integer.valueOf(c.getString(3).substring(4,6))-1, Integer.valueOf(c.getString(3).substring(6,8)));
            getId = c.getString(0);
            getDate = c.getString(3).substring(0,4) + "年" + c.getString(3).substring(4,6) + "月" + c.getString(3).substring(6,8) +"日("+week_name[calendar.get(Calendar.DAY_OF_WEEK)-1]+")";
            getTime = c.getString(3).substring(8,10) + ":" + c.getString(3).substring(10,12);
            init(getDate, c.getString(2), c.getString(1), getTime, c.getString(4), Integer.valueOf(c.getString(5)),true);
            c.close();
            dbAdapter.closeDB();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.finBtn:
            case R.id.delBtn:
                dbAdapter.openDB();
                dbAdapter.selectDelete(getId);
                dbAdapter.closeDB();
                init("", "", "", "", "", toDoFresource.getColor(R.color.colorWhite),false);
                listUpdate();
                break;
            case R.id.editBtn:
                Intent intent = new Intent(getActivity(), EditToDo.class);
                dbAdapter.openDB();
                String[] columns = {DBAdapter.COL_ID, DBAdapter.COL_TITLE, DBAdapter.COL_HEADER, DBAdapter.COL_DEADLINE, DBAdapter.COL_MEMO, DBAdapter.COL_COLOR}; // DBのカラム：ID, ToDo名, 期限, メモ
                Cursor c = dbAdapter.getDB(columns);
                c.move(getPosition);
                intent.putExtra("id", c.getString(0));
                intent.putExtra("title", c.getString(1));
                intent.putExtra("header", c.getString(2));
                intent.putExtra("year", Integer.valueOf((c.getString(3).substring(0,4))));
                intent.putExtra("month", Integer.valueOf(c.getString(3).substring(4,6)));
                intent.putExtra("day", Integer.valueOf(c.getString(3).substring(6,8)));
                intent.putExtra("hour", Integer.valueOf(c.getString(3).substring(8,10)));
                intent.putExtra("minute", Integer.valueOf(c.getString(3).substring(10,12)));
                intent.putExtra("memo", c.getString(4));
                intent.putExtra("color", Integer.valueOf(c.getString(5)));
                c.close();
                dbAdapter.closeDB();
                editMode = true;
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        init("", "", "", "", "", toDoFresource.getColor(R.color.colorWhite), false);
    }

    @Override
    public void onResume() {
        super.onResume();
        listUpdate();
    }

    void init(String dateText, String headerText, String titleText, String timeText, String memoText, int colorId, boolean bool) {
        toDoDate.setText(dateText);
        toDoHeader.setText(headerText);
        toDoTitle.setText(titleText);
        toDoTime.setText(timeText);
        toDoMemo.setText(memoText);
        toDoColorView.setBackgroundColor(colorId);
        finishButton.setEnabled(bool);
        editButton.setEnabled(bool);
        deleteButton.setEnabled(bool);
    }
}
