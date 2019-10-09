package com.movie.myapplication.model.AsyncTask;

import android.os.AsyncTask;

import com.movie.myapplication.Database.DBHelper;
import com.movie.myapplication.model.Event;
import com.movie.myapplication.view.MainActivity;

public class ImportEventFromTxtAsyncTask extends AsyncTask<Event,Void,Void> {
    private MainActivity activity;



    public ImportEventFromTxtAsyncTask(MainActivity activity)
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
        dbHelper.addEvent(event[0]);


        return  null;
    }

}
