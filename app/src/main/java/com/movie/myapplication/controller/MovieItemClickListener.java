package com.movie.myapplication.controller;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.movie.myapplication.Database.DBHelper;
import com.movie.myapplication.model.Event;
import com.movie.myapplication.model.Movie;
import com.movie.myapplication.model.MovieItem;
import com.movie.myapplication.view.EventDetail;
import com.movie.myapplication.view.MovieDetail;
import com.movie.myapplication.view.MovieList;

public class MovieItemClickListener implements AdapterView.OnItemClickListener {
    MovieList ml;
    MovieItem[] movieItems;

    public MovieItemClickListener(MovieList ml, MovieItem[] movieItems) {
        this.ml = ml;
        this.movieItems = movieItems;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // get the click movie
        String text = movieItems[position].getTitle();
        DBHelper dbHelper = new DBHelper(ml, "movieplan.db", null, 1);
        dbHelper.getWritableDatabase();
        Movie selectedMovie = dbHelper.getMoviebyTitle(text);

        //if the movie has a linked event ,go to that event
        if (dbHelper.checkMovieExist(selectedMovie)) {
            Event relateEvent = dbHelper.getEventbyMovie(selectedMovie);
            Intent intent = new Intent();
            intent.setClass(ml, EventDetail.class);
            intent.putExtra("eventName", relateEvent.getTitle());
            ml.startActivity(intent);
        } else {
            // go to movie detail
            Intent intent = new Intent();
            intent.setClass(ml, MovieDetail.class);
            intent.putExtra("movieTitle", selectedMovie.getTitle());
            ml.startActivity(intent);
        }

    }
}
