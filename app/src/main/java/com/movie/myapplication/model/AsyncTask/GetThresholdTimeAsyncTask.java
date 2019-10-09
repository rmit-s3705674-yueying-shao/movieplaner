package com.movie.myapplication.model.AsyncTask;

import android.app.Activity;
import android.os.AsyncTask;

import com.movie.myapplication.Database.DBHelper;

public class GetThresholdTimeAsyncTask extends AsyncTask<Void,Void,Integer> {
    private Activity activity;




    public GetThresholdTimeAsyncTask(Activity activity)
    {
        this.activity = activity;

    }
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected  Integer doInBackground(Void... unused) {

        DBHelper dbHelper = new DBHelper(activity, "movieplan.db", null, 1);
        dbHelper.getWritableDatabase();
        return dbHelper.getThresholdTime();

    }




}
