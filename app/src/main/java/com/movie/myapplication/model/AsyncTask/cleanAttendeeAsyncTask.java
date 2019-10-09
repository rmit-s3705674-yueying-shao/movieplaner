package com.movie.myapplication.model.AsyncTask;

import android.app.Activity;
import android.os.AsyncTask;

import com.movie.myapplication.Database.DBHelper;


public class cleanAttendeeAsyncTask extends AsyncTask<String,Void,Void> {
    private Activity activity;

    public cleanAttendeeAsyncTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(String... name) {

        DBHelper dbHelper = new DBHelper(activity, "movieplan.db", null, 1);
        dbHelper.getWritableDatabase();
        dbHelper.cleanAttendee(name[0]);
        return null;
    }
}
