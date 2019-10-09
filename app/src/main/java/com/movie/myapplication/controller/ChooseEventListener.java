package com.movie.myapplication.controller;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.movie.myapplication.Database.DBHelper;
import com.movie.myapplication.model.Event;
import com.movie.myapplication.model.Item;
import com.movie.myapplication.model.Movie;
import com.movie.myapplication.view.EventDetail;
import com.movie.myapplication.view.MovieDetail;

import java.util.ArrayList;

public class ChooseEventListener implements AdapterView.OnItemClickListener {

    private MovieDetail md;
    private Item[] items;
    private Movie movie;

    public ChooseEventListener(MovieDetail md, Item[] items, Movie movie) {
        this.md = md;
        this.items = items;
        this.movie = movie;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // get clicked event by event title
        String text = items[position].getTitle();
        ArrayList<Event> eventList;
        DBHelper dbHelper;
        dbHelper = new DBHelper(md, "movieplan.db", null, 1);
        dbHelper.getWritableDatabase();
        eventList = dbHelper.selectEvent();
        Event newEvent = null;
        for (int i = 0; i < eventList.size(); i++) {
            //find the event in event list
            if (eventList.get(i).getTitle().compareTo(text) == 0)
                newEvent = eventList.get(i);
        }

        // if Movie already linked a event, change the link to new event
        if (dbHelper.checkMovieExist(movie)) {
            dbHelper.updateMovie(movie, newEvent);
            Toast.makeText(md, "" + movie.getTitle() + " associated with " + newEvent.getTitle(), Toast.LENGTH_SHORT).show();
        } else {
            // Create a link between event and movie
            dbHelper.connectEventMovie(movie, newEvent);
            Toast.makeText(md, "" + movie.getTitle() + " associated with " + newEvent.getTitle(), Toast.LENGTH_SHORT).show();
        }


        // Go back to event detail
        Intent intent = new Intent();
        intent.setClass(md, EventDetail.class);
        intent.putExtra("eventName", newEvent.getTitle());
        md.startActivity(intent);

    }
}