package com.movie.myapplication.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.movie.myapplication.R;
import com.movie.myapplication.adapter.eventItemAdapter;
import com.movie.myapplication.controller.ChooseEventListener;
import com.movie.myapplication.model.AsyncTask.GetEventAsyncTask;
import com.movie.myapplication.model.AsyncTask.GetMoviebyTitleAsyncTask;
import com.movie.myapplication.model.AsyncTask.RemoveMoveAsyncTask;
import com.movie.myapplication.model.Event;
import com.movie.myapplication.model.Item;
import com.movie.myapplication.model.Movie;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MovieDetail extends AppCompatActivity {

    private ArrayList<Event> event = new ArrayList<>();
  //  private DBHelper dbHelper;
    private EditText title;
    private EditText Year;
    private EditText Poster;
    private ImageView img;
    private Movie movie;
    private TextView choose;
    private ListView eventListView;
    private boolean isRemove =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // get current movie
        movie = locateMovie();

        //match widget
        this.title = findViewById(R.id.mTitle);
        title.setEnabled(false);
        this.Year = findViewById(R.id.mYear);
        Year.setEnabled(false);
        this.Poster = findViewById(R.id.mPoster);
        Poster.setEnabled(false);
        this.img = findViewById(R.id.movieImage);

        // display movie detail
        this.title.setText(movie.getTitle());
        this.Year.setText(movie.getYear());
        this.Poster.setText(movie.getPostImgUrl());
        Context ctx = getBaseContext();
        int resId = getResourceByReflect(movie.getPostImgUrl());
        this.img.setImageResource(resId);
        choose = findViewById(R.id.Label2);
        choose.setVisibility(View.INVISIBLE);


        // when edit movie, display a event list. selected event will be linked to the movie


        try {
            this.event.addAll(new GetEventAsyncTask(this).execute().get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy H:mm:ss aa");
        Item[] items = new Item[event.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = new Item(event.get(i).getTitle(), sdf.format(event.get(i).getSdate()));
        }
        eventItemAdapter myAdapter = new eventItemAdapter(this, items);
        eventListView = findViewById(R.id.chooseEvent);
        eventListView.setVisibility(View.INVISIBLE);
        eventListView.setAdapter(myAdapter);
        eventListView.setOnItemClickListener(new ChooseEventListener(this, items, movie));
    }

    //setting menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_menu, menu);
        menu.add(0, 1, 0, "Edit this Movie");
        menu.add(0, 2, 0, "Remove this Movie");
        MenuItem home = menu.add(0, 17, 0, "");
        MenuItem cal = menu.add(0, 18, 0, "");

        cal.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        cal.setIcon(R.drawable.calendar);
        home.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        home.setIcon(R.drawable.home);
        return super.onCreateOptionsMenu(menu);

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

    // do something by id
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // user can see the event list for choosing
            case 1:
                choose.setVisibility(View.VISIBLE);
                eventListView.setVisibility(View.VISIBLE);
                break;
            //remove movie
            case 2:
                final AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(MovieDetail.this);
                normalDialog.setTitle("Warning");
                normalDialog.setMessage("Are you sure to remove this movie?");
                normalDialog.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                           isRemove=true;

                                Intent intent = new Intent();
                                intent.setClass(MovieDetail.this, MainActivity.class);
                                MovieDetail.this.startActivity(intent);


                            }
                        });
                normalDialog.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                normalDialog.show();
                break;
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

    // get current movie
    private Movie locateMovie() {
        Movie movie=null;
        Intent intent = getIntent();
        try {
            movie=new GetMoviebyTitleAsyncTask(this).execute(intent.getStringExtra("movieTitle")).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return movie;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isRemove)
        new RemoveMoveAsyncTask(MovieDetail.this).execute(movie);
    }
}
