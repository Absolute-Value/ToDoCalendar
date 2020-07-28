package com.example.x3033076.finalextodocalendar;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class TimePickerDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        int hour = getArguments().getInt("hour"); // 引き渡された時間を読み込む
        int minute = getArguments().getInt("minute"); // 引き渡された分を読み込む
        if(ToDoListFragment.editMode) { // 編集モードだったら
            return new TimePickerDialog(getActivity(), (EditToDo)getActivity(), hour, minute, true); // EditToDoに渡す
        } else { // 編集モードじゃなかったら
            return new TimePickerDialog(getActivity(), (AddToDo)getActivity(), hour, minute, true); // AddToDoに渡す
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {}
}
