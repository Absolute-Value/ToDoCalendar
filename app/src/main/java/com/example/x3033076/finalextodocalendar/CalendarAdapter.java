package com.example.x3033076.finalextodocalendar;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
        public FrameLayout aroundLayout;
        public LinearLayout insideLayout;
        public TextView dateText;
        public TextView toDoShowText1;
        public TextView toDoShowText2;
        public TextView toDoShowText3;
        public TextView toDoOtherText;
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
            holder.aroundLayout = convertView.findViewById(R.id.around);
            holder.insideLayout = convertView.findViewById(R.id.inside);
            holder.dateText = convertView.findViewById(R.id.cellTV);
            holder.toDoShowText1 = convertView.findViewById(R.id.toDoShowTV1);
            holder.toDoShowText2 = convertView.findViewById(R.id.toDoShowTV2);
            holder.toDoShowText3 = convertView.findViewById(R.id.toDoShowTV3);
            holder.toDoOtherText = convertView.findViewById(R.id.toDoOtherTV);
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

        DBAdapter dbAdapter = new DBAdapter(convertView.getContext());
        dbAdapter.openDB(); // DBの読み込み(読み書きの方)
        String[] columns = {DBAdapter.COL_TITLE, DBAdapter.COL_DEADLINE, DBAdapter.COL_COLOR}; // DBのカラム：ToDo名
        Cursor c = dbAdapter.getDB(columns);
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyyMMdd");
        holder.toDoShowText1.setText("");
        holder.toDoShowText1.setBackgroundColor(Color.argb(0,255,255,255));
        holder.toDoShowText2.setText("");
        holder.toDoShowText2.setBackgroundColor(Color.argb(0,255,255,255));
        holder.toDoShowText3.setText("");
        holder.toDoShowText3.setBackgroundColor(Color.argb(0,255,255,255));
        holder.toDoOtherText.setText("");
        holder.toDoOtherText.setBackgroundColor(Color.argb(0,255,255,255));
        int count = 0;

        if (c.moveToFirst()) {
            do {
                if(c.getString(1).substring(0,8).equals(dFormat.format(dateArray.get(position)))) {
                    switch (count) {
                        case 0:
                            holder.toDoShowText1.setText(c.getString(0));
                            holder.toDoShowText1.setBackgroundColor(Integer.valueOf(c.getString(2)));
                            break;
                        case 1:
                            holder.toDoShowText2.setText(c.getString(0));
                            holder.toDoShowText2.setBackgroundColor(Integer.valueOf(c.getString(2)));
                            break;
                        case 2:
                            holder.toDoShowText3.setText(c.getString(0));
                            holder.toDoShowText3.setBackgroundColor(Integer.valueOf(c.getString(2)));
                            break;
                        default:
                            holder.toDoOtherText.setText("+"+(count-2));
                            holder.toDoOtherText.setBackgroundColor(Color.argb(180,255,200,200));
                            break;
                    }
                    count++;
                }
            } while (c.moveToNext());
        }
        c.close();
        dbAdapter.closeDB(); // DBを閉じる

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
        if (dFormat.format(Calendar.getInstance().getTime()).equals(dFormat.format(dateArray.get(position)))) {
            holder.aroundLayout.setBackgroundColor(Color.rgb(255,187,85));
            holder.dateText.setBackgroundColor(Color.rgb(255,187,85));
            holder.dateText.setTextColor(Color.WHITE);
        } else {
            holder.aroundLayout.setBackgroundColor(Color.WHITE);
            holder.dateText.setBackgroundColor(Color.WHITE);
        }

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