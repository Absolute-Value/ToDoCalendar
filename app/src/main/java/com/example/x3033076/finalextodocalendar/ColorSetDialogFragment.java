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
    Button setRed, setYellow, setLightYellow, setLightGreen, setGreen, setSkyBlue, setLightBlue, setBlue, setDeepPurple, setPurple;
    Resources resource;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.color_set_layout, null);
        setRed = content.findViewById(R.id.setRedBtn);
        setYellow = content.findViewById(R.id.setYellowBtn);
        setLightYellow = content.findViewById(R.id.setLightYellowBtn);
        setLightGreen = content.findViewById(R.id.setLightGreenBtn);
        setGreen = content.findViewById(R.id.setGreenBtn);
        setSkyBlue = content.findViewById(R.id.setSlyBlueBtn);
        setLightBlue = content.findViewById(R.id.setLightBlueBtn);
        setBlue = content.findViewById(R.id.setBlueBtn);
        setDeepPurple = content.findViewById(R.id.setDeepPurpleBtn);
        setPurple = content.findViewById(R.id.setPurpleBtn);

        setRed.setOnClickListener(this);
        setYellow.setOnClickListener(this);
        setLightYellow.setOnClickListener(this);
        setLightGreen.setOnClickListener(this);
        setGreen.setOnClickListener(this);
        setSkyBlue.setOnClickListener(this);
        setLightBlue.setOnClickListener(this);
        setBlue.setOnClickListener(this);
        setDeepPurple.setOnClickListener(this);
        setPurple.setOnClickListener(this);

        resource = getResources();
        AddToDo.color = resource.getColor(R.color.colorLightBlue);

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
        int setColor;
        switch (view.getId()) {
            case R.id.setRedBtn:
                setColor = resource.getColor(R.color.colorRed);
                break;
            case R.id.setYellowBtn:
                setColor = resource.getColor(R.color.colorYellow);
                break;
            case R.id.setLightYellowBtn:
                setColor = resource.getColor(R.color.colorLightYellow);
                break;
            case R.id.setLightGreenBtn:
                setColor = resource.getColor(R.color.colorLightGreen);
                break;
            case R.id.setGreenBtn:
                setColor = resource.getColor(R.color.colorGreen);
                break;
            case R.id.setSlyBlueBtn:
                setColor = resource.getColor(R.color.colorSkyBlue);
                break;
            default:
                setColor = resource.getColor(R.color.colorLightBlue);
                break;
            case R.id.setBlueBtn:
                setColor = resource.getColor(R.color.colorBlue);
                break;
            case R.id.setDeepPurpleBtn:
                setColor = resource.getColor(R.color.colorDeepPurple);
                break;
            case R.id.setPurpleBtn:
                setColor = resource.getColor(R.color.colorPurple);
                break;
        }
        AddToDo.setColorButton.setBackgroundColor(setColor);
        AddToDo.color = setColor;
    }
}
