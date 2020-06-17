package com.example.x3033076.finalextodocalendar;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class ToDoList extends Fragment implements View.OnClickListener {

    static ListView list;
    Button finishButton, editButton, deleteButton;
    TextView toDoTitle;

    String getTitle = "";

    private DBAdapter dbAdapter;                // DBAdapter
    private ArrayAdapter<String> adapter;       // ArrayAdapter
    private ArrayList<String> items;            // ArrayList

    private View tdRootView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        tdRootView = inflater.inflate(R.layout.todo_list_layout, container, false);

        list = (ListView) tdRootView.findViewById(R.id.toDoLV);
        toDoTitle = (TextView) tdRootView.findViewById(R.id.titleTV);
        finishButton = (Button) tdRootView.findViewById(R.id.finBtn);
        editButton = (Button) tdRootView.findViewById(R.id.editBtn);
        deleteButton = (Button) tdRootView.findViewById(R.id.delBtn);

        list.setOnItemClickListener(new ListItemClickListener());
        finishButton.setOnClickListener(this);
        editButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);

        dbAdapter = new DBAdapter(getActivity());
        dbAdapter.openDB();     // DBの読み込み(読み書きの方)

        // ArrayListを生成
        items = new ArrayList<>();

        // DBのデータを取得
        String[] columns = {DBAdapter.COL_TITLE};     // DBのカラム：品名
        Cursor c = dbAdapter.getDB(columns);

        if (c.moveToFirst()) {
            do {
                items.add(c.getString(0));
                Log.d("取得したCursor:", c.getString(0));
            } while (c.moveToNext());
        }
        c.close();
        dbAdapter.closeDB();    // DBを閉じる

        adapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_list_item_1, items);


        list.setAdapter(adapter); //ListViewにアダプターをセット(=表示)

        return tdRootView;
    }

    private class ListItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            getTitle = (String) adapterView.getItemAtPosition(position);
            toDoTitle.setText(getTitle);

            finishButton.setEnabled(true);
            editButton.setEnabled(true);
            deleteButton.setEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.finBtn:
                break;
            case R.id.editBtn:
                break;
            case R.id.delBtn:
                break;
        }
    }
}
