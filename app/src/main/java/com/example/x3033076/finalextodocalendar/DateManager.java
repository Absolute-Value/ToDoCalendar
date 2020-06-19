package com.example.x3033076.finalextodocalendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class DateManager {
    Calendar mCalendar;

    public DateManager(){
        mCalendar = Calendar.getInstance();
    }

    public List<Date> getDays(){ //当月の要素を取得
        Date startDate = mCalendar.getTime();//現在の状態を保持

        int count = getWeeks() * 7 ; //GridViewに表示するマスの合計を計算

        //当月のカレンダーに表示される前月分の日数を計算
        mCalendar.set(Calendar.DATE, 1);
        int dayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        mCalendar.add(Calendar.DATE, -dayOfWeek);

        List<Date> days = new ArrayList<>();

        for (int i = 0; i < count; i ++){
            days.add(mCalendar.getTime());
            mCalendar.add(Calendar.DATE, 1);
        }

        //状態を復元
        mCalendar.setTime(startDate);

        return days;
    }

    public boolean isCurrentMonth(Date date){ //当月かどうか確認
        SimpleDateFormat format = new SimpleDateFormat("yyyy"+"年"+"MM"+"月", Locale.US);
        String currentMonth = format.format(mCalendar.getTime());
        if (currentMonth.equals(format.format(date))){
            return true;
        }else {
            return false;
        }
    }

    public int getWeeks(){ //週数を取得
        return mCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
    }

    public int getDayOfWeek(Date date) { //曜日を取得
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public void nextMonth(){ //翌月へ
        mCalendar.add(Calendar.MONTH, 1);
    }

    public void prevMonth(){ //前月へ
        mCalendar.add(Calendar.MONTH, -1);
    }
}