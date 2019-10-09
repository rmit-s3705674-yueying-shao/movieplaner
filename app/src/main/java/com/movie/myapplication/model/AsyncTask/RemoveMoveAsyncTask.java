package com.movie.myapplication.model.AsyncTask;

import android.app.Activity;
import android.os.AsyncTask;

import com.movie.myapplication.Database.DBHelper;
import com.movie.myapplication.model.Movie;

public class RemoveMoveAsyncTask extends AsyncTask<Movie, Void, Void> {

    private Activity activity;


    public RemoveMoveAsyncTask(Activity activity) {
        this.activity = activity;

    }

    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Movie... movies) {

        DBHelper dbHelper = new DBHelper(activity, "movieplan.db", null, 1);
        dbHelper.getWritableDatabase();
        dbHelper.cleanMovie(movies[0]);
        return null;
    }
}
