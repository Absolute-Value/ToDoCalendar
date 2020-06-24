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
        int hour = getArguments().getInt("hour");
        int minute = getArguments().getInt("minute");
        if(ToDoListFragment.editMode) return new TimePickerDialog(getActivity(), (EditToDo)getActivity(), hour, minute, true);
        else return new TimePickerDialog(getActivity(), (AddToDo)getActivity(), hour, minute, true);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {}
}
