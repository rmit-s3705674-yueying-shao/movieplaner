package com.movie.myapplication.controller;

import android.content.Intent;
import android.view.View;

import com.movie.myapplication.view.EventCalendar;
import com.movie.myapplication.view.EventDetail;

/*
*
*       Calendar click a event, go to the event detail
*
*/
public class CalendarClickListener implements View.OnClickListener {

    private String evenTitle;
    private EventCalendar ec;

    public CalendarClickListener(String evenTitle, EventCalendar ec) {
        this.evenTitle = evenTitle;
        this.ec = ec;
    }

    @Override
    public void onClick(View v) {

        Intent intent2 = new Intent();
        intent2.setClass(ec, EventDetail.class);
        intent2.putExtra("eventName", evenTitle);
        ec.startActivity(intent2);

    }
}
