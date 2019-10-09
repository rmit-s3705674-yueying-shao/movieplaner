package com.movie.myapplication.controller;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.movie.myapplication.Database.DBHelper;
import com.movie.myapplication.model.Event;
import com.movie.myapplication.model.Movie;
import com.movie.myapplication.model.MovieItem;
import com.movie.myapplication.view.AddMovie;
import com.movie.myapplication.view.EventDetail;


public class AddMovieListener implements AdapterView.OnItemClickListener {

    private AddMovie addMovie;
    private MovieItem[] movieItems;
    private Event event;

    public AddMovieListener(AddMovie addMovie, MovieItem[] movieItems, Event event) {
        this.addMovie = addMovie;
        this.movieItems = movieItems;
        this.event = event;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // Get the movie was clicked
        String text = movieItems[position].getTitle();
        DBHelper dbHelper;
        dbHelper = new DBHelper(addMovie, "movieplan.db", null, 1);
        dbHelper.getWritableDatabase();
        Movie chooseMovie = dbHelper.getMoviebyTitle(text);

        // if Movie already linked a event, change the link to new event
        if (dbHelper.checkMovieExist(chooseMovie))
            dbHelper.updateMovie(chooseMovie, event);
            // Create a link between event and movie
        else
            dbHelper.connectEventMovie(chooseMovie, event);


        Toast.makeText(addMovie, "" + chooseMovie.getTitle() + " associated with " + event.getTitle(), Toast.LENGTH_SHORT).show();

        //Go back to event detail
        Intent intent = new Intent();
        intent.setClass(addMovie, EventDetail.class);
        intent.putExtra("eventName", event.getTitle());
        addMovie.startActivity(intent);


    }
}
