package com.example.x3033076.finalextodocalendar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MyAdapter extends SimpleAdapter {

    LayoutInflater mLayoutInflater;
    String[] colm;
    int[] Rid;
    int res;
    Context context;

    public MyAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        colm = from;
        Rid = to;
        res = resource;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = mLayoutInflater.inflate(res, parent,false);
        ListView listView = (ListView) parent;

        Map<String,Object> data = (Map<String,Object>)listView.getItemAtPosition(position);

        Date now = new Date();
        int nowDate = 10000*(now.getYear()+1900) + 100*(now.getMonth()+1) + now.getDate();
        String date = (String)data.get(colm[2]);
        int setDate = Integer.valueOf(date);
        int nowTime = 60 * now.getHours() + now.getMinutes();
        String time = (String)data.get(colm[3]);
        int setTime = 60 * Integer.valueOf(time.substring(0,2)) + Integer.valueOf(time.substring(3,5));

        TextView textA = convertView.findViewById(Rid[0]);
        textA.setText((String)data.get(colm[0]));
        if(setDate<nowDate | (setDate==nowDate && setTime<nowTime)) textA.setTextColor(Color.RED);

        TextView textB = convertView.findViewById(Rid[1]);
        textB.setText((String)data.get(colm[1]));
        if(setDate<nowDate | (setDate==nowDate && setTime<nowTime)) textB.setTextColor(Color.RED);

        TextView textC = convertView.findViewById(Rid[2]);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.valueOf(date.substring(0,4)), Integer.valueOf(date.substring(4,6))-1, Integer.valueOf(date.substring(6,8)));
        String showDate = date.substring(4,6) + "月" + date.substring(6,8) + "日(" +ToDoListFragment.week_name[calendar.get(Calendar.DAY_OF_WEEK)-1] + ")";
        textC.setText(showDate);

        TextView textD = convertView.findViewById(Rid[3]);
        textD.setText((String)data.get(colm[3]));
        if(setDate<nowDate | (setDate==nowDate && setTime<nowTime)) textD.setTextColor(Color.RED);

        View viewA = convertView.findViewById(Rid[4]);
        String colId = (String) data.get(colm[4]);
        viewA.setBackgroundColor(Integer.valueOf(colId));

        return convertView;
    }
}
