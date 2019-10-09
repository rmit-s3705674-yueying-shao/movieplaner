package com.movie.myapplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.movie.myapplication.R;
import com.movie.myapplication.controller.AddEventListener;
import com.movie.myapplication.controller.CancelAddListener;
import com.movie.myapplication.controller.DateListener;
import com.movie.myapplication.controller.TimeListener;
import com.movie.myapplication.model.AsyncTask.AddEventAsyncTask;
import com.movie.myapplication.model.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Random;

public class AddEvent extends AppCompatActivity {
    private TextView sDate;
    private TextView sTime;
    private TextView eDate;
    private TextView eTime;
    private EditText title;
    private EditText venue;
    private EditText latitude;
    private EditText longitude;
    private String newEventID;
    private boolean isSave=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        // match widget
        title = findViewById(R.id.title);
        sDate = findViewById(R.id.sDate);
        sDate.setOnClickListener(new DateListener(this, sDate));
        sTime = findViewById(R.id.sTime);
        sTime.setOnClickListener(new TimeListener(this, sTime));
        eDate = findViewById(R.id.eDate);
        eDate.setOnClickListener(new DateListener(this, eDate));
        eTime = findViewById(R.id.eTime);
        eTime.setOnClickListener(new TimeListener(this, eTime));
        venue = findViewById(R.id.venue);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        Button save = findViewById(R.id.save);

        //set a random string for new event ID
        Intent i = getIntent();
        Random rand = new Random();
        newEventID = i.getStringExtra("eventNum") + rand.nextInt(10);

        // setting button listener
        save.setOnClickListener(new AddEventListener(this));
        Button cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new CancelAddListener(this));

        String startDate = i.getStringExtra("startDate");
        if (startDate!=null)
        {
            sDate.setText(startDate);
        }


    }

    //custom the menu
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

    // According to id to do something
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            // back to main activity
            case 17:
                Intent back = new Intent();
                back.setClass(this, MainActivity.class);
                this.startActivity(back);
                break;
            // go to calendar
            case 18:
                Intent cal = new Intent();
                cal.setClass(this, EventCalendar.class);
                this.startActivity(cal);
                break;

        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!isSave)
            return;

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String startDate = sDate.getText().toString() + " " + sTime.getText().toString();
        String endDate = eDate.getText().toString() + " " + eTime.getText().toString();
        String location = latitude.getText().toString() + ", " + longitude.getText().toString();
        // add detail to database
        // DBHelper dbHelper = new DBHelper(this, "movieplan.db", null, 1);
        //dbHelper.getWritableDatabase();
        try {
            Event newEvent = new Event(newEventID, title.getText().toString(), sdf.parse(startDate), sdf.parse(endDate), venue.getText().toString(), location);
            new AddEventAsyncTask(this).execute(newEvent);
            //dbHelper.addEvent(newEvent);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Toast.makeText(this, "Event added", Toast.LENGTH_SHORT).show();
    }

    public void Saved(){
        this.isSave=true;
    }
    // add the new event to database
    //public void AddtoDB() {

        // get the new event detail by widget


        // go back to main activity


   // }
}
