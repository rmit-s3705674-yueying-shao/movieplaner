package com.movie.myapplication.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.movie.myapplication.R;
import com.movie.myapplication.adapter.movieItemAdapter;
import com.movie.myapplication.controller.AddMovieListener;
import com.movie.myapplication.model.AsyncTask.GetEventbyIDAsyncTask;
import com.movie.myapplication.model.AsyncTask.SelectMoviesAsyncTask;
import com.movie.myapplication.model.Event;
import com.movie.myapplication.model.Movie;
import com.movie.myapplication.model.MovieItem;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class AddMovie extends AppCompatActivity {
    private ArrayList<Movie> addMovieList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        // get all movie from database
        SelectMoviesAsyncTask moviesTask= new  SelectMoviesAsyncTask (this);

        try {
            this.addMovieList=moviesTask.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //set the movie to the listview
        MovieItem[] movieItems = new MovieItem[addMovieList.size()];
        for (int i = 0; i < movieItems.length; i++) {
            Context ctx = getBaseContext();
            int resId = getResourceByReflect(addMovieList.get(i).getPostImgUrl());

            movieItems[i] = new MovieItem(addMovieList.get(i).getTitle(), addMovieList.get(i).getYear(), resId);
        }
        movieItemAdapter movieAdapter = new movieItemAdapter(this, movieItems);
        ListView movieListView = findViewById(R.id.allMovieList);
        movieListView.setAdapter(movieAdapter);
        movieListView.setOnItemClickListener(new AddMovieListener(this, movieItems, getCurrentEvent()));

    }

    // get current event for then add to the event
    private Event getCurrentEvent() {

        Intent i = getIntent();
        String eventId = i.getStringExtra("eventID");
        Event currentEvent=null;
        try {
            currentEvent= new GetEventbyIDAsyncTask(this).execute(eventId).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return currentEvent;
    }

    //setting menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_menu, menu);
        MenuItem home = menu.add(0, 17, 0, "");
        MenuItem cal = menu.add(0, 18, 0, "");
        cal.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        cal.setIcon(R.drawable.calendar);
        home.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        home.setIcon(R.drawable.home);
        return super.onCreateOptionsMenu(menu);

    }

    //According to id do something
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 17:
                Intent back = new Intent();
                back.setClass(this, MainActivity.class);
                this.startActivity(back);
                break;
            case 18:
                Intent cal = new Intent();
                cal.setClass(this, EventCalendar.class);
                this.startActivity(cal);
                break;

        }
        return true;
    }

    // get picture id by picture name
    public int getResourceByReflect(String imageName) {
        String shortName = imageName.replaceAll(".jpg", "");

        Class drawable = R.drawable.class;
        Field field = null;
        int r_id;
        try {
            field = drawable.getField(shortName);
            r_id = field.getInt(field.getName());
        } catch (Exception e) {
            r_id = 0;
            Log.e("ERROR", "PICTURE NOT　FOUND！");
        }
        return r_id;
    }


}
