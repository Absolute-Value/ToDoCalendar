package com.example.x3033076.finalextodocalendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class ToDoList extends Fragment {

    ListView list;

    static List<String> dataList = new ArrayList<String>();
    static ArrayAdapter<String> adapter;

    private View tdRootView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        tdRootView = inflater.inflate(R.layout.todo_list_layout, container, false);
        list = (ListView) tdRootView.findViewById(R.id.toDoLV);

        adapter =
                new ArrayAdapter<String>(
                        getActivity(),
                        android.R.layout.simple_list_item_1,
                        dataList);

        list.setAdapter(adapter);

        return tdRootView;
    }
}
