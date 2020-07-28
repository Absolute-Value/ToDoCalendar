package com.example.x3033076.finalextodocalendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        int year = getArguments().getInt("year"); // 引き渡された年を読み込む
        int month = getArguments().getInt("month")-1; // 引き渡された月を読み込む
        int day = getArguments().getInt("day"); // 引き渡された日を読み込む

        if(ToDoListFragment.editMode) { // 編集モードだったら
            return new DatePickerDialog(getActivity(), (EditToDo)getActivity(),  year, month, day); // EditToDoに渡す
        } else { // 編集モードじゃなかったら
            return new DatePickerDialog(getActivity(), (AddToDo)getActivity(),  year, month, day); // AddToDoに渡す
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {}
}
