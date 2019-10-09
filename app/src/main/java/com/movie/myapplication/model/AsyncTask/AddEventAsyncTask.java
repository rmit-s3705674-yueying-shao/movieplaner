package com.movie.myapplication.model.AsyncTask;

import android.os.AsyncTask;
import android.widget.Toast;

import com.movie.myapplication.Database.DBHelper;
import com.movie.myapplication.model.Event;
import com.movie.myapplication.view.AddEvent;

// add event to database

public class AddEventAsyncTask extends AsyncTask<Event,Void,Void> {


    private AddEvent addEvent;

    public AddEventAsyncTask(AddEvent addEvent) {
        this.addEvent = addEvent;
    }

    @Override
    // automatically runs on UI thread
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Event... params) {


       // for (int i = 0; i < 10; i++) {
            // DON'T DO THIS: Called on worker thread
            //onProgressUpdate(i);

            // DO THIS: onProgressUpdate() will be called on the UI Thread



            Event newEvent = params[0];


            // add detail to database
            DBHelper dbHelper = new DBHelper(addEvent, "movieplan.db", null, 1);
            dbHelper.getWritableDatabase();
            dbHelper.addEvent(newEvent);


            return null;

        }

    protected void onPostExecute(Void result) {
        Toast.makeText(addEvent, "Event added", Toast.LENGTH_SHORT).show();

    }

}
