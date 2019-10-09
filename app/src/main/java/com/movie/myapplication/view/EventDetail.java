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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.movie.myapplication.R;
import com.movie.myapplication.adapter.movieItemAdapter;
import com.movie.myapplication.controller.MovieDetailListener;
import com.movie.myapplication.model.AsyncTask.GetEventAsyncTask;
import com.movie.myapplication.model.AsyncTask.GetMoviebyEventAsyncTask;
import com.movie.myapplication.model.AsyncTask.RemoveEventbyTitleAsyncTask;
import com.movie.myapplication.model.AsyncTask.UpdateEventAsyncTask;
import com.movie.myapplication.model.Event;
import com.movie.myapplication.model.Movie;
import com.movie.myapplication.model.MovieItem;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class EventDetail extends AppCompatActivity {
    private ArrayList<Event> eventList = new ArrayList<>();
    private ArrayList<Movie> movieList = new ArrayList<>();

    private Event thisEvent;
    private EditText title;
    private EditText sDate;
    private EditText eDate;
    private EditText Venue;
    private EditText Location;
    private MenuItem save;
    private boolean isDelete=false;
    private boolean isUpdate=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy h:mm:ss aa");

        // get current event
        this.thisEvent = locatEvent();

        //get movies linked this event
        try {
            movieList = new GetMoviebyEventAsyncTask(this).execute(thisEvent).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //match widget
        this.title = findViewById(R.id.eTitle);
        title.setEnabled(false);
        this.sDate = findViewById(R.id.eSdate);
        sDate.setEnabled(false);
        this.eDate = findViewById(R.id.eEdate);
        eDate.setEnabled(false);
        this.Venue = findViewById(R.id.eVenue);
        Venue.setEnabled(false);
        this.Location = findViewById(R.id.eLocation);
        Location.setEnabled(false);

        //display  event detail
        this.title.setText(thisEvent.getTitle());
        this.sDate.setText(sdf.format(thisEvent.getSdate()));
        this.eDate.setText(sdf.format(thisEvent.getEdate()));
        this.Venue.setText(thisEvent.getVenue());
        this.Location.setText(thisEvent.getLocation());

        // put linked movie to the list
        MovieItem[] movieItems = new MovieItem[movieList.size()];
        for (int i = 0; i < movieItems.length; i++) {
            Context ctx = getBaseContext();
            int resId = getResourceByReflect(movieList.get(i).getPostImgUrl());

            movieItems[i] = new MovieItem(movieList.get(i).getTitle(), movieList.get(i).getYear(), resId);
        }
        movieItemAdapter movieAdapter = new movieItemAdapter(this, movieItems);
        ListView movieListView = findViewById(R.id.movieList);
        movieListView.setAdapter(movieAdapter);
        movieListView.setOnItemClickListener(new MovieDetailListener(this, movieItems));

    }

    // get picture ID by picture name
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

    //setting menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_menu, menu);
        menu.add(0, 1, 0, "Add Movie");
        menu.add(0, 2, 0, "Attendees");
        menu.add(0, 3, 0, "Edit this event");
        menu.add(0, 4, 0, "Remove this event");
        MenuItem home = menu.add(0, 17, 0, "");
        MenuItem cal = menu.add(0, 18, 0, "");
        save = menu.add(0, 19, 0, "Save");
        cal.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        cal.setIcon(R.drawable.calendar);
        home.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        home.setIcon(R.drawable.home);
        save.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        save.setVisible(false);
        return super.onCreateOptionsMenu(menu);

    }

    //According to id do something
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case 1:
                Intent intent = new Intent();
                intent.setClass(EventDetail.this, AddMovie.class);
                intent.putExtra("eventID", thisEvent.getId());
                EventDetail.this.startActivity(intent);

                break;
            case 2:
                Intent attendIntent = new Intent();
                attendIntent.setClass(EventDetail.this, attendeeList.class);
                attendIntent.putExtra("eventID", thisEvent.getId());
                EventDetail.this.startActivity(attendIntent);
                break;
            case 3:
                title.setEnabled(true);
                sDate.setEnabled(true);
                eDate.setEnabled(true);
                Venue.setEnabled(true);
                Location.setEnabled(true);
                save.setVisible(true);
                break;
            case 4:
                final AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(EventDetail.this);
                normalDialog.setTitle("Warning");
                normalDialog.setMessage("Are you sure to remove this event?");
                normalDialog.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                             isDelete=true;

                                Intent intent = new Intent();
                                intent.setClass(EventDetail.this, MainActivity.class);
                                EventDetail.this.startActivity(intent);
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
            //update event
            case 19:


               this.isUpdate=true;
                Intent i = new Intent();
                i.setClass(this, EventDetail.class);
                i.putExtra("eventID",getIntent().getStringExtra("eventID"));
                this.startActivity(i);
                this.finish();


                break;
        }
        return true;
    }

        //get current event
    private Event locatEvent() {
        Event event = null;
        Intent intent = getIntent();




        try {
            this.eventList.addAll(new GetEventAsyncTask(this).execute().get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < eventList.size(); i++) {
            //find the event in event list
            if (eventList.get(i).getId().compareTo(intent.getStringExtra("eventID")) == 0)
                event = eventList.get(i);
        }
        return event;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(isDelete)
        {   new RemoveEventbyTitleAsyncTask(EventDetail.this).execute(thisEvent.getTitle());}
        if(isUpdate)
        {
            SimpleDateFormat sdf = new SimpleDateFormat("d/MM/yyyy h:mm:ss aa",Locale.ENGLISH);

            String startDate = sDate.getText().toString();
            String endDate = eDate.getText().toString();
            try {
                Date newSdate =  sdf.parse(startDate);
                Date newEdate =  sdf.parse(endDate);
                Event newEvent = new Event(thisEvent.getId(), title.getText().toString(), newSdate, newEdate, Venue.getText().toString(), Location.getText().toString());
                new UpdateEventAsyncTask(this).execute(newEvent);
                Toast.makeText(this, "Event Updated", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
