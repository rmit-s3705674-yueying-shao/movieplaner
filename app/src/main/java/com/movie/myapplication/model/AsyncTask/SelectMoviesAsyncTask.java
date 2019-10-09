package com.movie.myapplication.model.AsyncTask;

import android.app.Activity;
import android.os.AsyncTask;

import com.movie.myapplication.Database.DBHelper;
import com.movie.myapplication.model.Movie;

import java.util.ArrayList;

public class SelectMoviesAsyncTask extends   AsyncTask<Void,Void,ArrayList<Movie>> {
    private Activity a;
    private ArrayList<Movie> movies;



    public SelectMoviesAsyncTask(Activity activity)
    {
        this.a = activity;
        this.movies = new ArrayList<>();
    }
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected  ArrayList<Movie> doInBackground(Void... unused) {

        DBHelper dbHelper = new DBHelper(a, "movieplan.db", null, 1);
        dbHelper.getWritableDatabase();
        movies = dbHelper.selectMovie();
        return movies;

    }




}
