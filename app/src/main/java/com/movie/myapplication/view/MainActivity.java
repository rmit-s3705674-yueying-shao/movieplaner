package com.movie.myapplication.view;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.movie.myapplication.R;
import com.movie.myapplication.adapter.eventItemAdapter;
import com.movie.myapplication.controller.ItemClickListener;
import com.movie.myapplication.model.AsyncTask.GetEventAsyncTask;
import com.movie.myapplication.model.AsyncTask.ImportEventFromTxtAsyncTask;
import com.movie.myapplication.model.AsyncTask.ImportMovieFromTxtAsyncTask;
import com.movie.myapplication.model.AsyncTask.RemoveEventbyTitleAsyncTask;
import com.movie.myapplication.model.AsyncTask.SelectMoviesAsyncTask;
import com.movie.myapplication.model.Event;
import com.movie.myapplication.model.Item;
import com.movie.myapplication.model.Movie;
import com.movie.myapplication.model.Service.NetworkStateService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {

    private ArrayList<Event> event = new ArrayList<>();
    private ArrayList<Movie> movie = new ArrayList<>();
    private String TAG = getClass().getName();
 //   private DBHelper dbHelper;
    private Button Asc;
    private Button Desc;
    private Item[] inItems;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getPermission();

        Asc = findViewById(R.id.btnAsc);
        Desc = findViewById(R.id.btnDesc);
/*
     DBHelper dbHelper = new DBHelper(this, "movieplan.db", null, 1);
        dbHelper.getWritableDatabase();
        dbHelper.removeOldRemind();
*/
        //dbHelper.cleanAll();

        // get all data from database

     // this.event.addAll(dbHelper.selectEvent());

        GetEventAsyncTask eventsTask = new GetEventAsyncTask(this);
        SelectMoviesAsyncTask moviesTask= new SelectMoviesAsyncTask(this);

        try {
            this.event= eventsTask.execute().get();
            this.movie=moviesTask.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {

        }

        //this.movie.addAll(dbHelper.selectMovie());

        //if no event data in database ,import event from event.txt
        if (this.event.size() == 0) {
            getEvent();
            try {
                GetEventAsyncTask eventsTask2 = new GetEventAsyncTask(this);
                this.event= eventsTask2.execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //if no movie data in database ,import movie from movie.txt
        if (this.movie.size() == 0) {
            getMovie();
            try {
                SelectMoviesAsyncTask moviesTask2= new SelectMoviesAsyncTask(this);
                this.movie= moviesTask2.execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // put all event to the list
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy h:mm:ss aa");
        Item[] items = new Item[event.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = new Item(event.get(i).getTitle(), sdf.format(event.get(i).getSdate()));
        }
        eventItemAdapter myAdapter = new eventItemAdapter(this, items);
        ListView eventListView = findViewById(R.id.eventListView);
        eventListView.setAdapter(myAdapter);
        eventListView.setOnItemClickListener(new ItemClickListener(this, items));
        inItems = items;
        eventListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int position, long arg3) {
                AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(MainActivity.this);
                normalDialog.setTitle("Warning");
                normalDialog.setMessage("Are you sure to remove this even?");
                normalDialog.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String title = inItems[position].getTitle();

                                new RemoveEventbyTitleAsyncTask(MainActivity.this).execute(title);

                                Toast.makeText(MainActivity.this, "Event Remooved", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent();
                                intent.setClass(MainActivity.this, MainActivity.class);
                                MainActivity.this.startActivity(intent);
                            }
                        });
                normalDialog.setNegativeButton("No", null);
                normalDialog.show();
                return true;
            }
        });

        // set the event list ascending
        Asc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventAsc();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy h:mm:ss aa");
                Item[] items = new Item[event.size()];
                for (int i = 0; i < items.length; i++) {
                    items[i] = new Item(event.get(i).getTitle(), sdf.format(event.get(i).getSdate()));
                }
                eventItemAdapter myAdapter = new eventItemAdapter(MainActivity.this, items);
                ListView eventListView = findViewById(R.id.eventListView);
                eventListView.setAdapter(myAdapter);
                eventListView.setOnItemClickListener(new ItemClickListener(MainActivity.this, items));
                eventListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                                   final int position, long arg3) {
                        final AlertDialog.Builder normalDialog =
                                new AlertDialog.Builder(MainActivity.this);
                        normalDialog.setTitle("Warning");
                        normalDialog.setMessage("Are you sure to remove this even?");
                        normalDialog.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        String title = inItems[position].getTitle();
                                        new RemoveEventbyTitleAsyncTask(MainActivity.this).execute(title);

                                        Toast.makeText(MainActivity.this, "Event Remooved", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent();
                                        intent.setClass(MainActivity.this, MainActivity.class);
                                        MainActivity.this.startActivity(intent);
                                    }
                                });
                        normalDialog.setNegativeButton("No", null);
                        normalDialog.show();
                        return true;
                    }
                });


            }
        });

        // set the event list descending
        Desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventDesc();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy h:mm:ss aa");
                Item[] items = new Item[event.size()];
                for (int i = 0; i < items.length; i++) {
                    items[i] = new Item(event.get(i).getTitle(), sdf.format(event.get(i).getSdate()));
                }
                eventItemAdapter myAdapter = new eventItemAdapter(MainActivity.this, items);
                ListView eventListView = findViewById(R.id.eventListView);
                eventListView.setAdapter(myAdapter);
                eventListView.setOnItemClickListener(new ItemClickListener(MainActivity.this, items));
                eventListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                                   final int position, long arg3) {
                        final AlertDialog.Builder normalDialog =
                                new AlertDialog.Builder(MainActivity.this);
                        normalDialog.setTitle("Warning");
                        normalDialog.setMessage("Are you sure to remove this even?");
                        normalDialog.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        String title = inItems[position].getTitle();
                                        new RemoveEventbyTitleAsyncTask(MainActivity.this).execute(title);
                                        Toast.makeText(MainActivity.this, "Event Remooved", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent();
                                        intent.setClass(MainActivity.this, MainActivity.class);
                                        MainActivity.this.startActivity(intent);
                                    }
                                });
                        normalDialog.setNegativeButton("No", null);
                        normalDialog.show();
                        return true;
                    }
                });


            }

        });
        /*
        PendingIntent pendingIntent,pendingIntent2;
        AlarmManager am,am2;

        am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, FirstRemindIntentService.class);//启动示例Service

        pendingIntent = PendingIntent.getService(this, 0, intent, 0);

        long interval = DateUtils.MINUTE_IN_MILLIS / 6;// 30分钟一次

        long firstWake = System.currentTimeMillis() + interval;
        am.setRepeating(AlarmManager.RTC, firstWake, interval, pendingIntent);


        am2 = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        Intent intent2 = new Intent(this, RemindAgainIntentService.class);//启动示例Service

        pendingIntent2 = PendingIntent.getService(this, 0, intent2, 0);


        long firstWake2 = System.currentTimeMillis() + interval;
        am2.setRepeating(AlarmManager.RTC, firstWake2, interval, pendingIntent2);
            */

        Intent i=new Intent(this,NetworkStateService.class);
        startService(i);

      //startService(new Intent(MainActivity.this, NetworkStateService.class));
       // startService(new Intent(MainActivity.this, RemindAgainIntentService.class));







    }






    //setting menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_menu, menu);
        menu.add(0, 1, 0, "Add Event");
        menu.add(0, 2, 0, "Movies");
        menu.add(0, 3, 0, "Setting");
        menu.add(0, 4, 0, "Soonest events");
        MenuItem home = menu.add(0, 17, 0, "");
        MenuItem cal = menu.add(0, 18, 0, "");

        cal.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        cal.setIcon(R.drawable.calendar);
        home.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        home.setIcon(R.drawable.home);


        return super.onCreateOptionsMenu(menu);

    }

    //do something by id
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                Intent intent = new Intent();
                intent.setClass(this, AddEvent.class);
                intent.putExtra("eventNum", this.event.size() + "");
                this.startActivity(intent);
                break;
            case 2:
                Intent intent2 = new Intent();
                intent2.setClass(this, MovieList.class);
                this.startActivity(intent2);
                break;
            case 3:
                Intent intent3 = new Intent();
                intent3.setClass(this, ConfigTime.class);
                this.startActivity(intent3);
                break;
            case 4:
                Intent intent4 = new Intent();
                intent4.setClass(this, MapsActivity.class);
                this.startActivity(intent4);
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

    //get the Event from txt to DataBase
    private void getEvent() {
        Event tempEvent = null;
        SimpleDateFormat sdf = new SimpleDateFormat("d/MM/yyyy h:mm:ss aa");

        try (Scanner input = new Scanner(getApplicationContext().getResources().openRawResource(R.raw.events))) {
            LinkedList<String> longString = new LinkedList<>();
            while (input.hasNextLine()) {
                longString.addLast(input.nextLine());
            }
            for (int i = 0; i < longString.size(); i++) {
                if(longString.get(i).contains("//"))
                {
                    continue;
                }
                String substring[] = longString.get(i).split(",");
                // remove the " in substring
                for (int j = 0; j < substring.length; j++) {
                    substring[j] = substring[j].replaceAll("\"", "");
                }

                boolean overlap = false;
                for (int j = 0; j < event.size(); j++) {
                    // two date overlap
                    if (sdf.parse(substring[2]).before(event.get(j).getEdate()) && sdf.parse(substring[3]).after(event.get(j).getSdate()))
                        overlap = true;

                }
                if (overlap)
                    continue;


                tempEvent = new Event(substring[0], substring[1], sdf.parse(substring[2]), sdf.parse(substring[3]), substring[4], substring[5] + ", " + substring[6]);
               ImportEventFromTxtAsyncTask IEFA = new  ImportEventFromTxtAsyncTask(this);
                IEFA.execute(tempEvent);
               // dbh.addEvent(tempEvent);
            }

        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        }
    }

    //get the Movie from txt to Database
    private void getMovie() {
        Movie tempMovie = null;
        try (Scanner input = new Scanner(getApplicationContext().getResources().openRawResource(R.raw.movies))) {
            LinkedList<String> longString = new LinkedList<>();
            while (input.hasNextLine()) {
                longString.addLast(input.nextLine());
            }
            for (int i = 0; i < longString.size(); i++) {
                String substring[] = longString.get(i).split(",");
                // remove the " in substring
                for (int j = 0; j < substring.length; j++) {
                    substring[j] = substring[j].replaceAll("\"", "");
                }
                tempMovie = new Movie(substring[0], substring[1], substring[2], substring[3]);
                ImportMovieFromTxtAsyncTask IEFA = new  ImportMovieFromTxtAsyncTask(this);
                IEFA.execute(tempMovie);
               // dbh.addMovie(tempMovie);
            }

        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        }


    }

    private void eventDesc() {
        Event tempEvent = null;
        for (int i = 1; i < event.size(); i++) {
            for (int j = 0; j < event.size() - i; j++) {
                if (event.get(j).getSdate().before(event.get(j + 1).getSdate())) {
                    tempEvent = event.get(j);
                    event.set(j, event.get(j + 1));
                    event.set(j + 1, tempEvent);
                }
            }
        }
    }


    private void eventAsc() {
        Event tempEvent = null;
        for (int i = 1; i < event.size(); i++) {
            for (int j = 0; j < event.size() - i; j++) {
                if (event.get(j).getSdate().after(event.get(j + 1).getSdate())) {
                    tempEvent = event.get(j);
                    event.set(j, event.get(j + 1));
                    event.set(j + 1, tempEvent);
                }
            }
        }
    }

    private static final int LOCATION_CODE = 1;
    private LocationManager lm;//【位置管理】

    public void getPermission(){
        lm = (LocationManager) MainActivity.this.getSystemService(MainActivity.this.LOCATION_SERVICE);
        boolean ok = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (ok) {//开了定位服务
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_CODE);
                      Toast.makeText(this, "No permission", Toast.LENGTH_SHORT).show();

            } else {

                // get permission
            }
        } else {

            Toast.makeText(MainActivity.this, "GPS hasn't turn on", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 1315);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                  // accept permission

                } else {
                   // deny permission
                    Toast.makeText(MainActivity.this, "Location permissions are disabled, some functions may unavailable!",Toast.LENGTH_LONG).show();
                }

            }
        }
    }



/*
    public void setEvent(ArrayList<Event> event) {
        this.event=event;
    }*/
}
