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
        int year = getArguments().getInt("year");
        int month = getArguments().getInt("month")-1;
        int day = getArguments().getInt("day");

        if(ToDoListFragment.editMode) return new DatePickerDialog(getActivity(), (EditToDo)getActivity(),  year, month, day);
        else return new DatePickerDialog(getActivity(), (AddToDo)getActivity(),  year, month, day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {}
}
