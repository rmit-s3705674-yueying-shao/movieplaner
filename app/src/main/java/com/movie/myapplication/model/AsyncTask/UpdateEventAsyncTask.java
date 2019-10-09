package com.movie.myapplication.model.AsyncTask;

import android.app.Activity;
import android.os.AsyncTask;

import com.movie.myapplication.Database.DBHelper;
import com.movie.myapplication.model.Event;

public class UpdateEventAsyncTask extends AsyncTask<Event,Void,Void> {
    private Activity activity;




    public UpdateEventAsyncTask(Activity activity)
    {
        this.activity = activity;

    }
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected  Void doInBackground(Event... event) {

        DBHelper dbHelper = new DBHelper(activity, "movieplan.db", null, 1);
        dbHelper.getWritableDatabase();
        dbHelper.updateEvent(event[0]);
        return null;

    }




}
