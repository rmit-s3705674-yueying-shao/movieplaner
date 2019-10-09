package com.movie.myapplication.model;

import android.app.Activity;
import android.os.AsyncTask;

import com.movie.myapplication.Database.DBHelper;

import java.util.ArrayList;

public class GetAttendeesbyEvent extends AsyncTask<Event,Void, ArrayList<Attendees>> {
    private Activity a;

    public GetAttendeesbyEvent(Activity activity) {
        this.a = activity;
    }

    @Override
    protected ArrayList<Attendees> doInBackground(Event... event) {

        DBHelper dbHelper = new DBHelper(a, "movieplan.db", null, 1);
        dbHelper.getWritableDatabase();


         return dbHelper.getAttendeesbyEvent(event[0]);
    }
}
