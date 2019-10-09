package com.movie.myapplication.controller;

import android.content.Intent;
import android.view.View;

import com.movie.myapplication.view.AddEvent;
import com.movie.myapplication.view.MainActivity;

public class CancelAddListener implements View.OnClickListener {

    private AddEvent addEvent;

    public CancelAddListener(AddEvent addEvent) {
        this.addEvent = addEvent;
    }

    @Override
    public void onClick(View v) {
        //Go back to main activity
        Intent intent = new Intent();
        intent.setClass(addEvent, MainActivity.class);
        addEvent.startActivity(intent);
    }
}
