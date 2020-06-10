package com.example.x3033076.finalextodocalendar;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.example.x3033076.finalextodocalendar.ui.main.SectionsPagerAdapter;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Date now;
    int month, day, hour, minute, second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.addFab);
        final Intent intent = new Intent(this, AddToDo.class);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                now = new Date();
                month = now.getMonth() + 1;
                day = now.getDate();
                hour = now.getHours();
                minute = now.getMinutes();
                second = now.getSeconds();

                ToDoList.adapter.add(month+"/"+day+" "+ String.format("%02d",hour) +":"+ String.format("%02d",minute) +":"+ String.format("%02d",second) +" プログラミング実践");
                */


                startActivity(intent);
            }
        });
    }
}