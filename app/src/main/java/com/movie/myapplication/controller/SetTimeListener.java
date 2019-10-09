package com.movie.myapplication.controller;

import android.view.View;

import com.movie.myapplication.view.ConfigTime;

public class SetTimeListener implements View.OnClickListener {

    private ConfigTime configTime;
    public SetTimeListener(ConfigTime configTime) {
        this.configTime=configTime;
    }

    @Override
    public void onClick(View v) {
        this.configTime.UpDateTime();
    }
}
