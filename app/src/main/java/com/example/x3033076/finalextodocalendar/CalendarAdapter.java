package com.example.x3033076.finalextodocalendar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarAdapter extends BaseAdapter {
    static boolean doneCheck = false;

    private List<Date> dateArray = new ArrayList();
    private Context mContext;
    private DateManager mDateManager;
    private LayoutInflater mLayoutInflater;
    static List<String> dayArray = new ArrayList();

    private static class ViewHolder { //カスタムセルを拡張したらここでWigetを定義
        public TextView dateText;
    }

    public CalendarAdapter(Context context){
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mDateManager = new DateManager();
        dateArray = mDateManager.getDays();
    }

    @Override
    public int getCount() {
        return dateArray.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.calendar_cell_layout, null);
            holder = new ViewHolder();
            holder.dateText = convertView.findViewById(R.id.cellTV);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        //セルのサイズを指定
        float dp = mContext.getResources().getDisplayMetrics().density;
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(parent.getWidth()/7 - (int)dp,
                (parent.getHeight() - (int)dp * mDateManager.getWeeks() ) / mDateManager.getWeeks());
        convertView.setLayoutParams(params);

        //日付のみ表示させる
        SimpleDateFormat dateFormat = new SimpleDateFormat("d", Locale.US);
        holder.dateText.setText(dateFormat.format(dateArray.get(position)));
        dayArray.add(dateFormat.format(dateArray.get(position)));

        convertView.setBackgroundColor(Color.WHITE);

        int colorId;
        switch (mDateManager.getDayOfWeek(dateArray.get(position))){ //日曜日を赤、土曜日を青に
            case 1:
                if (mDateManager.isCurrentMonth(dateArray.get(position))) colorId = Color.RED; // 当月のセル
                else colorId = Color.argb(80, 255,0,0);               // 当月以外のセル
                break;
            case 7:
                if (mDateManager.isCurrentMonth(dateArray.get(position))) colorId = Color.BLUE; // 当月のセル
                else colorId = Color.argb(80, 0,0,255);               // 当月以外のセル
                break;
            default:
                if (mDateManager.isCurrentMonth(dateArray.get(position))) colorId = Color.BLACK; // 当月のセル
                else colorId = Color.argb(80, 0,0,0);                  // 当月以外のセル
                break;
        }
        holder.dateText.setTextColor(colorId);

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    public String getTitle(){ //表示月を取得
        SimpleDateFormat format = new SimpleDateFormat("yyyy"+"年"+"MM"+"月", Locale.US);
        return format.format(mDateManager.mCalendar.getTime());
    }

    public String getMonth() {
        SimpleDateFormat format = new SimpleDateFormat("MM", Locale.US);
        return (format.format(mDateManager.mCalendar.getTime()));
    }

    public void nextMonth(){ //翌月表示
        dayArray.clear();
        doneCheck=true;
        mDateManager.nextMonth();
        dateArray = mDateManager.getDays();
        this.notifyDataSetChanged();
    }

    public void prevMonth(){ //前月表示
        dayArray.clear();
        doneCheck=true;
        mDateManager.prevMonth();
        dateArray = mDateManager.getDays();
        this.notifyDataSetChanged();
    }
}