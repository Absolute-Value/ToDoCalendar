package com.example.x3033076.finalextodocalendar;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

public class Update extends FragmentActivity { // onResumeを呼び出すために一時的に生成されるアクティビティ
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        finish(); // アクティビティを閉じる
    }
}
