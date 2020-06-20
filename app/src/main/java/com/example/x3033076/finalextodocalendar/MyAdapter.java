package com.example.x3033076.finalextodocalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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

        TextView textA = (TextView) convertView.findViewById(Rid[0]);
        textA.setText((String)data.get(colm[0]));

        TextView textB = (TextView) convertView.findViewById(Rid[1]);
        textB.setText((String)data.get(colm[1]));

        TextView textC = (TextView) convertView.findViewById(Rid[2]);
        textC.setText((String)data.get(colm[2]));

        TextView textD = (TextView) convertView.findViewById(Rid[3]);
        textD.setText((String)data.get(colm[3]));

        View viewA = (View) convertView.findViewById(Rid[4]);
        String colId = (String) data.get(colm[4]);
        viewA.setBackgroundColor(Integer.valueOf(colId));

        return convertView;
    }
}
