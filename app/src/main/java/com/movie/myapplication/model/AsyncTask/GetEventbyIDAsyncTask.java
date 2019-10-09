package com.movie.myapplication.model.AsyncTask;

import android.app.Activity;
import android.os.AsyncTask;

import com.movie.myapplication.Database.DBHelper;
import com.movie.myapplication.model.Event;

public class GetEventbyIDAsyncTask extends AsyncTask<String,Void, Event> {
    private Activity a;

    public GetEventbyIDAsyncTask(Activity activity) {
        this.a = activity;
    }

    @Override
    protected Event doInBackground(String... eventID) {

        DBHelper dbHelper = new DBHelper(a, "movieplan.db", null, 1);
        dbHelper.getWritableDatabase();


         return dbHelper.getEventbyID(eventID[0]);
    }
}
