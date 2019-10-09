package com.movie.myapplication.controller;

import android.view.View;
import android.widget.Toast;

import com.movie.myapplication.view.EventCalendar;

public class NoEventListener implements View.OnClickListener {
    private EventCalendar ec;

    public NoEventListener(EventCalendar ec) {
        this.ec = ec;
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(ec, "No event in this day", Toast.LENGTH_SHORT).show();

    }
}
