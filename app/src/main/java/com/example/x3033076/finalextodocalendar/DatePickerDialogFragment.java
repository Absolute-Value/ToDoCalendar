package com.example.x3033076.finalextodocalendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import java.util.Date;

public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Date now = new Date();
        int year = now.getYear()+1900;
        int month = now.getMonth();
        int day = now.getDate();

        return new DatePickerDialog(getActivity(),
                (AddToDo)getActivity(),  year, month, day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {}
}
