package com.movie.myapplication.controller;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.movie.myapplication.Database.DBHelper;
import com.movie.myapplication.model.Movie;
import com.movie.myapplication.model.MovieItem;
import com.movie.myapplication.view.EventDetail;
import com.movie.myapplication.view.MovieDetail;

public class MovieDetailListener implements AdapterView.OnItemClickListener {
    private EventDetail ed;
    private MovieItem[] movieItems;

    public MovieDetailListener(EventDetail ed, MovieItem[] movieItems) {
        this.ed = ed;
        this.movieItems = movieItems;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // get the clicked movie
        String text = movieItems[position].getTitle();
        DBHelper dbHelper = new DBHelper(ed, "movieplan.db", null, 1);
        dbHelper.getWritableDatabase();
        Movie selectedMovie = dbHelper.getMoviebyTitle(text);

        // go to the movie detail
        Intent intent = new Intent();
        intent.setClass(ed, MovieDetail.class);
        intent.putExtra("movieTitle", selectedMovie.getTitle());
        ed.startActivity(intent);

    }

}
