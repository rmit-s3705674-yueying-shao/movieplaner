package com.movie.myapplication.model.AsyncTask;

import android.app.Activity;
import android.os.AsyncTask;

import com.movie.myapplication.Database.DBHelper;
import com.movie.myapplication.model.Event;

import java.util.ArrayList;

public class GetEventAsyncTask extends AsyncTask<Void,Void,ArrayList<Event>> {
    private Activity activity;
    private ArrayList<Event> event;



    public GetEventAsyncTask(Activity activity)
    {
        this.activity = activity;
        this.event = new ArrayList<>();
    }
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected  ArrayList<Event> doInBackground(Void... unused) {

        DBHelper dbHelper = new DBHelper(activity, "movieplan.db", null, 1);
        dbHelper.getWritableDatabase();
        event = dbHelper.selectEvent();
        return event;

    }
    @Override
    protected void onPostExecute(ArrayList<Event> event) {

    }



}
