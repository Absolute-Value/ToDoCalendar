package com.example.x3033076.finalextodocalendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;

public class ColorSetDialogFragment extends DialogFragment implements View.OnClickListener {
    Button setRed, setYellow, setLightGreen, setGreen, setLightBlue, setBlue;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.color_set_layout, null);
        setRed = content.findViewById(R.id.setRedBtn);
        setYellow = content.findViewById(R.id.setYellowBtn);
        setLightGreen = content.findViewById(R.id.setLightGreenBtn);
        setGreen = content.findViewById(R.id.setGreenBtn);
        setLightBlue = content.findViewById(R.id.setLightBlueBtn);
        setBlue = content.findViewById(R.id.setBlueBtn);

        setRed.setOnClickListener(this);
        setYellow.setOnClickListener(this);
        setLightGreen.setOnClickListener(this);
        setGreen.setOnClickListener(this);
        setLightBlue.setOnClickListener(this);
        setBlue.setOnClickListener(this);

        builder.setView(content);

        builder.setMessage("色を設定")
                .setNegativeButton("閉じる", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {}
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onClick(View view) {
        Resources res = getResources();
        int setColor;
        switch (view.getId()) {
            case R.id.setRedBtn:
                setColor = res.getColor(R.color.colorRed);
                break;
            case R.id.setYellowBtn:
                setColor = res.getColor(R.color.colorYellow);
                break;
            case R.id.setLightGreenBtn:
                setColor = res.getColor(R.color.colorLightGreen);
                break;
            case R.id.setGreenBtn:
                setColor = res.getColor(R.color.colorGreen);
                break;
            case R.id.setBlueBtn:
                setColor = res.getColor(R.color.colorBlue);
                break;
            default:
                setColor = res.getColor(R.color.colorLightBlue);
                break;
        }
        AddToDo.setToDoColor = setColor;
        AddToDo.setColorButton.setBackgroundColor(setColor);
    }
}
