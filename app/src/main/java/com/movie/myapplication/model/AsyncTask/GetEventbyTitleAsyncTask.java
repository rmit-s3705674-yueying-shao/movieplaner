package com.movie.myapplication.model.AsyncTask;

import android.app.Activity;
import android.os.AsyncTask;

import com.movie.myapplication.Database.DBHelper;
import com.movie.myapplication.model.Event;

public class GetEventbyTitleAsyncTask extends AsyncTask<String,Void, Event> {
    private Activity a;

    public GetEventbyTitleAsyncTask(Activity activity) {
        this.a = activity;
    }

    @Override
    protected Event doInBackground(String... title) {

        DBHelper dbHelper = new DBHelper(a, "movieplan.db", null, 1);
        dbHelper.getWritableDatabase();


         return dbHelper.getEventbyTitle(title[0]);
    }
}
