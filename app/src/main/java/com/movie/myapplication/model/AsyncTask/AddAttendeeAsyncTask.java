package com.movie.myapplication.model.AsyncTask;

import android.app.Activity;
import android.os.AsyncTask;

import com.movie.myapplication.Database.DBHelper;
import com.movie.myapplication.model.AddAttendeeParams;

// add attendee to database


public class AddAttendeeAsyncTask extends AsyncTask<AddAttendeeParams, Void, Void> {
    private Activity activity;

    public AddAttendeeAsyncTask(Activity activity) {

        this.activity = activity;
    }

    @Override
    protected Void doInBackground(AddAttendeeParams... params) {

        DBHelper dbHelper = new DBHelper(activity, "movieplan.db", null, 1);
        dbHelper.getWritableDatabase();
        dbHelper.addAttendee(params[0].getAttendees(), params[0].getEvent());
        return null;
    }
}



