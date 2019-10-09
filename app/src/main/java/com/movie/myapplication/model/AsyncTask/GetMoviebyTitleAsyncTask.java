package com.movie.myapplication.model.AsyncTask;

import android.app.Activity;
import android.os.AsyncTask;

import com.movie.myapplication.Database.DBHelper;
import com.movie.myapplication.model.Movie;

public class GetMoviebyTitleAsyncTask extends AsyncTask<String,Void, Movie> {
    private Activity a;

    public GetMoviebyTitleAsyncTask(Activity activity) {
        this.a = activity;
    }


    @Override
    protected Movie doInBackground(String... title) {

        DBHelper dbHelper = new DBHelper(a, "movieplan.db", null, 1);
        dbHelper.getWritableDatabase();

        return dbHelper.getMoviebyTitle(title[0]);
    }
}
