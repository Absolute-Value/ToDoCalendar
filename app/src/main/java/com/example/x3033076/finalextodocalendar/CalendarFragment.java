package com.example.x3033076.finalextodocalendar;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
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

public class CalendarFragment extends Fragment {

    private ListView calList;
    private TextView titleText, calDate;
    private Button prevButton, nextButton;
    private CalendarAdapter mCalendarAdapter;
    private GridView calendarGridView;

    private View clRootView;
    private List<Map<String,Object>> list = new ArrayList<>();
    private DBAdapter dbAdapter; // DBAdapter
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        clRootView = inflater.inflate(R.layout.calendar_layout, container, false);

        calList = clRootView.findViewById(R.id.calDateLV);
        titleText = clRootView.findViewById(R.id.calMonthTV);
        calDate = clRootView.findViewById(R.id.calDateTV);
        prevButton = clRootView.findViewById(R.id.calPrevBtn);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarAdapter.prevMonth();
                titleText.setText(mCalendarAdapter.getTitle());
            }
        });
        nextButton = clRootView.findViewById(R.id.calNextBtn);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarAdapter.nextMonth();
                titleText.setText(mCalendarAdapter.getTitle());
            }
        });
        calendarGridView = clRootView.findViewById(R.id.calendarGV);
        mCalendarAdapter = new CalendarAdapter(getContext());
        calendarGridView.setAdapter(mCalendarAdapter);
        titleText.setText(mCalendarAdapter.getTitle());

        calendarGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String nowYear = mCalendarAdapter.getTitle().substring(0,4);
                int nowMonth = Integer.valueOf(mCalendarAdapter.getMonth());
                TextView cellDateText = v.findViewById(R.id.cellTV);
                String nowDay = (String) cellDateText.getText();
                if(Integer.valueOf(nowDay)-7>position) nowMonth--; // 先月末
                if(position-7>Integer.valueOf(nowDay)) nowMonth++; // 来月頭
                Calendar calendar = Calendar.getInstance();
                calendar.set(Integer.valueOf(nowYear), nowMonth-1, Integer.valueOf(nowDay));
                calDate.setText(nowMonth+"月"+nowDay+"日("+ToDoListFragment.week_name[calendar.get(Calendar.DAY_OF_WEEK)-1]+")");

                dbAdapter = new DBAdapter(getActivity());
                dbAdapter.openDB(); // DBの読み込み(読み書きの方)

                list.clear();

                // DBのデータを取得
                String[] columns = {DBAdapter.COL_TITLE, DBAdapter.COL_HEADER, DBAdapter.COL_DEADLINE, DBAdapter.COL_COLOR}; // DBのカラム：ToDo名
                Cursor c = dbAdapter.getDB(columns);
                if (c.moveToFirst()) {
                    do {
                        if(c.getString(2).substring(0,8).equals(nowYear+String.format("%02d",nowMonth)+String.format("%02d",Integer.parseInt(nowDay)))) {
                            Map<String,Object> map = new HashMap<>();
                            map.put("title", c.getString(0));
                            map.put("header", c.getString(1));
                            map.put("date", c.getString(2).substring(0,8));
                            map.put("time", c.getString(2).substring(8,10) + ":" + c.getString(2).substring(10,12));
                            map.put("color", c.getString(3));
                            list.add(map);
                        }
                    } while (c.moveToNext());
                }
                c.close();
                dbAdapter.closeDB(); // DBを閉じる

                SimpleAdapter adapter = new MyAdapter(getActivity(), list, R.layout.calendar_list_layout, new String[]{"title", "header", "date", "time", "color"},
                        new int[]{R.id.calListTitleTV, R.id.calListHeaderTV, R.id.calListTimeTV, R.id.calListTimeTV, R.id.calListColorTV});
                calList.setAdapter(adapter); //ListViewにアダプターをセット(=表示)
            }
        });
        return clRootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        calDate.setText("");
        list.clear();
        SimpleAdapter adapter = new MyAdapter(getActivity(), list, R.layout.calendar_list_layout, new String[]{"title", "header", "date", "time", "color"},
                new int[]{R.id.calListTitleTV, R.id.calListHeaderTV, R.id.calListTimeTV, R.id.calListTimeTV, R.id.calListColorTV});
        calList.setAdapter(adapter); //ListViewにアダプターをセット(=表示)
    }

    @Override
    public void onResume() {
        super.onResume();
        mCalendarAdapter = new CalendarAdapter(getContext());
        calendarGridView.setAdapter(mCalendarAdapter);
    }
}
