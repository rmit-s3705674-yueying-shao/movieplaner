package com.movie.myapplication.model.AsyncTask;

import android.app.Activity;
import android.os.AsyncTask;

import com.movie.myapplication.Database.DBHelper;

import java.util.Date;

public class HasEventAsyncTask extends AsyncTask<Date,Void,String> {
    private Activity a;

    public HasEventAsyncTask(Activity a) {
        this.a = a;
    }

    @Override
    protected String doInBackground(Date... dates) {
        DBHelper dbHelper = new DBHelper(a, "movieplan.db", null, 1);
        dbHelper.getWritableDatabase();


        return dbHelper.hasEvent(dates[0]);
    }
}
