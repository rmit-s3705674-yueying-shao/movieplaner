package com.movie.myapplication.controller;

import android.content.Intent;
import android.view.View;

import com.movie.myapplication.view.AddEvent;


public class AddEventListener implements View.OnClickListener {


    private AddEvent addEvent;


    public AddEventListener(AddEvent addEvent) {

        this.addEvent = addEvent;
    }

    @Override
    public void onClick(View v) {

        //add the event to Database
        addEvent.Saved();
        Intent intent = new Intent();
        intent.setClass(addEvent, AddEvent.class);
        addEvent.startActivity(intent);

    }


}
