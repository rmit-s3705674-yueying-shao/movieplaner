package com.movie.myapplication.model.AsyncTask;

import android.os.AsyncTask;

import com.movie.myapplication.Database.DBHelper;
import com.movie.myapplication.model.Movie;
import com.movie.myapplication.view.MainActivity;

public class ImportMovieFromTxtAsyncTask extends AsyncTask<Movie,Void,Void> {
    private MainActivity activity;



    public ImportMovieFromTxtAsyncTask(MainActivity activity)
    {
        this.activity = activity;

    }
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected  Void doInBackground(Movie... movies) {

        DBHelper dbHelper = new DBHelper(activity, "movieplan.db", null, 1);
        dbHelper.getWritableDatabase();
        dbHelper.addMovie(movies[0]);
        return  null;
    }

}
