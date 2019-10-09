package com.movie.myapplication.controller;


import android.app.TimePickerDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.movie.myapplication.view.AddEvent;


public class TimeListener implements View.OnClickListener {

    private AddEvent ae;
    private TextView time;

    public TimeListener(AddEvent ae, TextView time) {
        this.ae = ae;
        this.time = time;
    }

    public void onClick(View view) {

        //put chose time into TextView
        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                time.setText(hour + ":" + minute + ":" + "00");

            }
        };
        TimePickerDialog dialog = new TimePickerDialog(ae, TimePickerDialog.THEME_HOLO_LIGHT, listener, 23, 59, true);
        dialog.show();

    }
}
