package com.movie.myapplication.model.AsyncTask;

import android.app.Activity;
import android.os.AsyncTask;

import com.movie.myapplication.Database.DBHelper;
import com.movie.myapplication.model.Event;

public class RemoveEventbyTitleAsyncTask extends AsyncTask<String, Void, Void> {

    private Activity activity;


    public RemoveEventbyTitleAsyncTask(Activity activity) {
        this.activity = activity;

    }

    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String... title) {

        DBHelper dbHelper = new DBHelper(activity, "movieplan.db", null, 1);
        dbHelper.getWritableDatabase();
        Event clickedEvent = dbHelper.getEventbyTitle(title[0]);
        dbHelper.cleanEvent(clickedEvent);
        return null;
    }
}
