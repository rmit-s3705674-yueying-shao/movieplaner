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
import com.movie.myapplication.controller.MovieItemClickListener;
import com.movie.myapplication.model.AsyncTask.SelectMoviesAsyncTask;
import com.movie.myapplication.model.Movie;
import com.movie.myapplication.model.MovieItem;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MovieList extends AppCompatActivity {
    private ArrayList<Movie> movieList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        //get all movie and put to the list



        try {
            movieList = new SelectMoviesAsyncTask(this).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MovieItem[] movieItems = new MovieItem[movieList.size()];
        for (int i = 0; i < movieItems.length; i++) {
            Context ctx = getBaseContext();
            int resId = getResourceByReflect(movieList.get(i).getPostImgUrl());

            movieItems[i] = new MovieItem(movieList.get(i).getTitle(), movieList.get(i).getYear(), resId);
        }
        movieItemAdapter movieAdapter = new movieItemAdapter(this, movieItems);
        ListView movieListView = findViewById(R.id.allMovieList);
        movieListView.setAdapter(movieAdapter);
        movieListView.setOnItemClickListener(new MovieItemClickListener(this, movieItems));
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

    // do something by id
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //根据不同的id点击不同按钮控制activity需要做的事件
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
