package com.movie.myapplication.controller;


import android.app.DatePickerDialog;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.movie.myapplication.view.AddEvent;

import java.util.Calendar;


public class DateListener implements View.OnClickListener {
    AddEvent ae;
    private Calendar cal;
    private int year, month, day;
    TextView date;

    public DateListener(AddEvent ae, TextView date) {
        this.ae = ae;
        this.date = date;
        getDate();
    }


    @Override
    public void onClick(View view) {

        // put chose date into TextView
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker arg0, int year, int month, int day) {
                date.setText(day + "-" + (++month) + "-" +year );
            }
        };
        DatePickerDialog dialog = new DatePickerDialog(ae, DatePickerDialog.THEME_HOLO_LIGHT, listener, year, month, day);
        dialog.show();


    }

    //get current date
    private void getDate() {
        cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        Log.i("wxy", "year" + year);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
    }

}
