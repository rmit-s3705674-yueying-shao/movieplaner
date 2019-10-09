package com.movie.myapplication.model.AsyncTask;

import android.app.Activity;
import android.os.AsyncTask;

import com.movie.myapplication.Database.DBHelper;
import com.movie.myapplication.model.Event;
import com.movie.myapplication.model.Movie;

import java.util.ArrayList;

public class GetMoviebyEventAsyncTask extends AsyncTask<Event, Void, ArrayList<Movie>> {
    private Activity a;

    public GetMoviebyEventAsyncTask(Activity activity) {
        this.a = activity;
    }

    @Override
    protected ArrayList<Movie> doInBackground(Event... event) {

        DBHelper dbHelper = new DBHelper(a, "movieplan.db", null, 1);
        dbHelper.getWritableDatabase();
        return dbHelper.getMoviebyEvent(event[0]);
    }
}
