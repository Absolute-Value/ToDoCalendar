package com.example.x3033076.finalextodocalendar;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
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
    static Boolean day2SwitchBool = false;
    static Boolean day1SwitchBool = false;
    static Boolean hour3SwitchBool = false;
    static Boolean hour1SwitchBool = false;
    static Boolean justSwitchBool = false;

    private Resources toDoFresource;
    private DBAdapter dbAdapter; // DBAdapter
    private List<Map<String,Object>> list = new ArrayList<>();

    private View toDoColorView, tdRootView;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        tdRootView = inflater.inflate(R.layout.todo_list_layout, container, false);

        listV = tdRootView.findViewById(R.id.toDoLV);
        toDoColorView = tdRootView.findViewById(R.id.toDoColorV);
        toDoDate = tdRootView.findViewById(R.id.dateTV);
        toDoHeader = tdRootView.findViewById(R.id.headerTV);
        toDoTitle = tdRootView.findViewById(R.id.titleTV);
        toDoTime = tdRootView.findViewById(R.id.timeTV);
        toDoMemo = tdRootView.findViewById(R.id.memoTV);
        finishButton = tdRootView.findViewById(R.id.finBtn);
        editButton = tdRootView.findViewById(R.id.editBtn);
        deleteButton = tdRootView.findViewById(R.id.delBtn);

        // リスナ登録
        listV.setOnItemClickListener(new ListItemClickListener());
        finishButton.setOnClickListener(this);
        editButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);

        toDoFresource = tdRootView.getResources();
        listUpdate(); // ToDoリスト更新

        return tdRootView;
    }

    void listUpdate() {
        dbAdapter = new DBAdapter(getActivity());
        dbAdapter.openDB(); // DBの読み込み(読み書きの方)

        // DBのデータを取得
        String[] columns = {DBAdapter.COL_TITLE, DBAdapter.COL_HEADER, DBAdapter.COL_DEADLINE, DBAdapter.COL_COLOR, DBAdapter.COL_ID}; // DBのカラム：ToDo名, 項目名, 色, id
        Cursor c = dbAdapter.getDB(columns);

        int listId, listYear, listMonth, listDay, listHour, listMinute;

        list.clear(); // これがないとリスト増殖バグ発生

        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll(); // すべての通知を削除

        if (c.moveToFirst()) {
            do {
                listId    = Integer.parseInt(c.getString(4));
                listYear  = Integer.parseInt(c.getString(2).substring(0,4));
                listMonth = Integer.parseInt(c.getString(2).substring(4,6));
                listDay   = Integer.parseInt(c.getString(2).substring(6,8));
                listHour  = Integer.parseInt(c.getString(2).substring(8,10));
                listMinute= Integer.parseInt(c.getString(2).substring(10,12));
                Map<String,Object> map = new HashMap<>();
                map.put("title", c.getString(0));
                map.put("header", c.getString(1));
                map.put("year", listYear);
                map.put("date", c.getString(2).substring(0,8));
                map.put("time", c.getString(2).substring(8,10) + ":" + c.getString(2).substring(10,12));
                map.put("color", c.getString(3));
                Calendar now = Calendar.getInstance();
                Calendar cale = Calendar.getInstance();
                now.setTimeInMillis(System.currentTimeMillis());
                cale.setTimeInMillis(System.currentTimeMillis());
                cale.set(listYear, listMonth-1, listDay, listHour, listMinute, 0);
                if (justSwitchBool && now.getTimeInMillis() <= cale.getTimeInMillis()) { // まだ設定時間がきてなかったら
                    scheduleNotification(listId, c.getString(1), "「"+c.getString(0)+"」の時間です" ,cale); // 締め切り時間の通知を追加
                }
                cale.add(Calendar.HOUR_OF_DAY, -1); // カレンダーの時間を1時間引く
                if (hour1SwitchBool && now.getTimeInMillis() <= cale.getTimeInMillis()) { // まだ設定時間がきてなかったら
                    scheduleNotification(listId+10000, c.getString(1), "「"+c.getString(0)+"」の1時間前です" ,cale); // 1時間前の通知を追加
                }
                cale.add(Calendar.HOUR_OF_DAY, -2); // カレンダーの時間を2時間引く
                if (hour3SwitchBool && now.getTimeInMillis() <= cale.getTimeInMillis()) { // まだ設定時間がきてなかったら
                    scheduleNotification(listId+20000, c.getString(1), "「"+c.getString(0)+"」の3時間前です" ,cale); // 3時間前の通知を追加
                }
                cale.add(Calendar.HOUR_OF_DAY, 3); // カレンダーの時間を3時間足す
                cale.add(Calendar.DATE, -1); // カレンダーの日付を1日引く
                if (day1SwitchBool && now.getTimeInMillis() <= cale.getTimeInMillis()) { // まだ設定時間がきてなかったら
                    scheduleNotification(listId+30000, c.getString(1), "「"+c.getString(0)+"」の1日前です" ,cale); // 1日前の通知を追加
                }
                cale.add(Calendar.DATE, -1); // カレンダーの日付を1日引く
                if (day2SwitchBool && now.getTimeInMillis() <= cale.getTimeInMillis()) { // まだ設定時間がきてなかったら
                    scheduleNotification(listId+40000, c.getString(1), "「"+c.getString(0)+"」の2日前です" ,cale); // 2日前の通知を追加
                }
                list.add(map); // リストにマップを追加
            } while (c.moveToNext());
        }
        c.close(); // カーソルを閉じる
        dbAdapter.closeDB(); // DBを閉じる

        SimpleAdapter adapter = new MyAdapter(getActivity(), list, R.layout.list_layout, new String[]{"title", "header", "date", "time", "color", "year"},
                new int[]{R.id.listTitleTV, R.id.listHeaderTV, R.id.listDateTV, R.id.listTimeTV, R.id.listColorTV});
        listV.setAdapter(adapter); //ListViewにアダプターをセット(=表示)
    }

    private class ListItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            dbAdapter.openDB();// DBの読み込み(読み書きの方)
            // DBのデータを取得
            String[] columns = {DBAdapter.COL_ID, DBAdapter.COL_TITLE, DBAdapter.COL_HEADER, DBAdapter.COL_DEADLINE, DBAdapter.COL_MEMO, DBAdapter.COL_COLOR}; // DBのカラム：ID, ToDo名, 期限, メモ, 色
            Cursor c = dbAdapter.getDB(columns); // カーソル
            getPosition = position + 1;
            c.move(getPosition); // カーソルを移動
            Calendar calendar = Calendar.getInstance(); // カレンダーのインスタンス生成
            calendar.set(Integer.valueOf(c.getString(3).substring(0,4)),
                    Integer.valueOf(c.getString(3).substring(4,6))-1, Integer.valueOf(c.getString(3).substring(6,8)));
            getId = c.getString(0);
            getDate = c.getString(3).substring(0,4) + "年" + c.getString(3).substring(4,6) + "月" + c.getString(3).substring(6,8) +"日("+week_name[calendar.get(Calendar.DAY_OF_WEEK)-1]+")";
            getTime = c.getString(3).substring(8,10) + ":" + c.getString(3).substring(10,12);
            init(getDate, c.getString(2), c.getString(1), getTime, c.getString(4), Integer.valueOf(c.getString(5)),true);
            c.close(); // カーソル終了
            dbAdapter.closeDB(); // DBを閉じる
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.finBtn: // "完了"ボタンを押した時
            case R.id.delBtn: // "削除"ボタンを押した時
                dbAdapter.openDB(); // DBの読み込み(読み書きの方)
                dbAdapter.selectDelete(getId); // DBを選択削除
                dbAdapter.closeDB(); // DBを閉じる
                init("", "", "", "", "", toDoFresource.getColor(R.color.colorWhite),false);
                listUpdate(); // ToDoリスト更新
                Intent updateIntent = new Intent(getActivity(), Update.class); // Update のインテントを作成
                startActivity(updateIntent); // Activity展開(onResumeを呼び出すため)
                break;
            case R.id.editBtn: // "編集"ボタンを押した時
                Intent editIntent = new Intent(getActivity(), EditToDo.class); // EditToDo のインテントを作成
                dbAdapter.openDB();// DBの読み込み(読み書きの方)
                String[] columns = {DBAdapter.COL_ID, DBAdapter.COL_TITLE, DBAdapter.COL_HEADER, DBAdapter.COL_DEADLINE, DBAdapter.COL_MEMO, DBAdapter.COL_COLOR}; // DBのカラム：ID, ToDo名, 期限, メモ
                Cursor c = dbAdapter.getDB(columns); // カーソル
                c.move(getPosition); // カーソル移動
                // EditToDoのインテントに引き渡す値
                editIntent.putExtra("id", c.getString(0));
                editIntent.putExtra("title", c.getString(1));
                editIntent.putExtra("header", c.getString(2));
                editIntent.putExtra("year", Integer.valueOf((c.getString(3).substring(0,4))));
                editIntent.putExtra("month", Integer.valueOf(c.getString(3).substring(4,6)));
                editIntent.putExtra("day", Integer.valueOf(c.getString(3).substring(6,8)));
                editIntent.putExtra("hour", Integer.valueOf(c.getString(3).substring(8,10)));
                editIntent.putExtra("minute", Integer.valueOf(c.getString(3).substring(10,12)));
                editIntent.putExtra("memo", c.getString(4));
                editIntent.putExtra("color", Integer.valueOf(c.getString(5)));

                c.close(); // カーソル終了
                dbAdapter.closeDB(); // DBを閉じる
                editMode = true; // 編集モードをオンにする
                startActivity(editIntent); // Activity展開
                break;
        }
    }

    @Override
    public void onPause() { // 一時停止時
        super.onPause();
        init("", "", "", "", "", toDoFresource.getColor(R.color.colorWhite), false);
    }

    @Override
    public void onResume() { // 再開時
        super.onResume();
        listUpdate(); // ToDoリストの更新
    }

    void init(String dateText, String headerText, String titleText, String timeText, String memoText, int colorId, boolean bool) { // 配置物の初期化
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

    private void scheduleNotification(int id, String header, String title, Calendar calendar){
        Intent notificationIntent = new Intent(getActivity(), AlarmReceiver.class); // AlarmReceiver にインテントを作成
        // AlarmReceiver のインテントに引き渡す値
        notificationIntent.putExtra(AlarmReceiver.NOTIFICATION_ID, id);
        notificationIntent.putExtra(AlarmReceiver.NOTIFICATION_HEADER, header);
        notificationIntent.putExtra(AlarmReceiver.NOTIFICATION_TITLE, title);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= 23) { // SDKのバージョンが23以上なら
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent); // アラームセット
        } else if (Build.VERSION.SDK_INT >= 19) { // SDKのバージョンが19以上23未満なら
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent); // アラームセット
        } else { // SDKのバージョンが19未満なら
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent); // アラームセット
        }
    }
}
