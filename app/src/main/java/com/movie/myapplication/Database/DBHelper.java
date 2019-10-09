package com.movie.myapplication.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.movie.myapplication.model.Attendees;
import com.movie.myapplication.model.Event;
import com.movie.myapplication.model.Movie;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // create all tables
        String CREATE_TABLE_Event = "CREATE TABLE Event( ID varchar(10) PRIMARY KEY, title varchar(20), sdate varchar(50), edate varchar(50), Venue varchar(20), Location varchar(100))";
        db.execSQL(CREATE_TABLE_Event);
        String CREATE_TABLE_Movie = "CREATE TABLE movie( ID varchar(10) PRIMARY KEY, title varchar(20), year varchar(4), posturl varchar(200))";
        db.execSQL(CREATE_TABLE_Movie);
        String CREATE_TABLE_MovieEvent = "CREATE TABLE movieevent( MovieID varchar(10) PRIMARY KEY, EventID varchar(10))";
        db.execSQL(CREATE_TABLE_MovieEvent);
        String CREATE_TABLE_EventAttend = "CREATE TABLE EventAttend( ID varchar(10) PRIMARY KEY, EventID varchar(10), attendname varchar(10), attentphone varchar(11))";
        db.execSQL(CREATE_TABLE_EventAttend);
        String CREATE_TABLE_UserSetting = "CREATE TABLE UserSetting( name varchar(10) PRIMARY KEY,Period int,Threshold int,Remind int)";
        db.execSQL(CREATE_TABLE_UserSetting);
        String CREATE_TABLE_RemindAgain = "CREATE TABLE Remind( eventID varchar(10) PRIMARY KEY,RemindTime varchar(50))";
        db.execSQL(CREATE_TABLE_RemindAgain);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // get all event form database
    public ArrayList<Event> selectEvent() {
        ArrayList<Event> event = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("d/MM/yyyy h:mm:ss aa");
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query("Event", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(0);
                String Title = cursor.getString(cursor.getColumnIndex("title"));
                String sdate = cursor.getString(cursor.getColumnIndex("sdate"));
                String edate = cursor.getString(cursor.getColumnIndex("edate"));
                String venue = cursor.getString(4);
                String location = cursor.getString(5);
                try {
                    Date sDate = sdf.parse(sdate);
                    Date eDate = sdf.parse(edate);
                    Event newEvent = new Event(id, Title, sDate, eDate, venue, location);
                    event.add(newEvent);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        return event;
    }

    // get all movie from database
    public ArrayList<Movie> selectMovie() {
        ArrayList<Movie> movies = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query("Movie", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(0);
                String Title = cursor.getString(cursor.getColumnIndex("title"));
                String Year = cursor.getString(cursor.getColumnIndex("year"));
                String postUrl = cursor.getString(3);

                Movie movie = new Movie(id, Title, Year, postUrl);
                movies.add(movie);


            } while (cursor.moveToNext());
        }
        cursor.close();

        return movies;
    }

    // add event into database
    public void addEvent(Event newEvent) {
        SimpleDateFormat sdf = new SimpleDateFormat("d/MM/yyyy h:mm:ss aa");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ID", newEvent.getId());
        values.put("title", newEvent.getTitle());
        values.put("sdate", sdf.format(newEvent.getSdate()));
        values.put("edate", sdf.format(newEvent.getEdate()));
        values.put("Venue", newEvent.getVenue());
        values.put("Location", newEvent.getLocation());
        db.insert("Event", null, values);

    }

    //  Default three  user setting
    public void addDefaultTime() {

        SQLiteDatabase db = this.getWritableDatabase();
        int number = 0;
        Cursor c = db.rawQuery("select * from UserSetting", null);
        number = c.getCount();
        if (number != 0)
            return;
        ContentValues values = new ContentValues();
        values.put("name", "overall");
        values.put("Period", 1);
        values.put("Threshold", 60);
        values.put("Remind", 1);
        db.insert("UserSetting", null, values);

    }

    // add a new remind time(current time + remind time 'N' )
    public void addRemind(String evenID) {

        if (checkEventRemindAgain(evenID)) {
            removeOldRemind(evenID);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("d/MM/yyyy HH:mm");
        int RemindN = this.getRemindTime();
        Date currentTime = new Date(System.currentTimeMillis() + ( RemindN * 60000));
        String RemindTime = sdf.format(currentTime);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("eventID", evenID);
        values.put("RemindTime", RemindTime);
        db.insert("Remind", null, values);
    }

    // Check if this event has been notified
    public boolean checkEventRemindAgain(String eventID) {
        boolean check = false;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query("Remind", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(0);
                if (id.compareTo(eventID) == 0) {
                    check = true;
                    break;
                }


            } while (cursor.moveToNext());
        }
        cursor.close();


        return check;
    }

    // get next notification time
    public Date getRemindTime(String evenID) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("d/MM/yyyy HH:mm");
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select RemindTime from Remind where eventID='" + evenID + "'", null);

        if (cursor.moveToFirst()) {
            do {
                String time = cursor.getString(0);

                try {
                    date = sdf.parse(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            } while (cursor.moveToNext());
        }
        cursor.close();

        return date;
    }

    // remove the old remind time
    public void removeOldRemind(String evenID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Remind", "eventID=?", new String[]{evenID});
    }

    // get the Remind time 'N'
    public int getRemindTime() {
        int time = 1;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query("UserSetting", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String strTime = cursor.getString(3);
                time = Integer.parseInt(strTime);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return time;
    }

    // get Notification Period time
    public int getPeriodTime() {
        int time = 1;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query("UserSetting", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String strTime = cursor.getString(1);
                time = Integer.parseInt(strTime);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return time;
    }

    // get the notification Threshold time
    public int getThresholdTime() {
        int time = 60;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query("UserSetting", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String strTime = cursor.getString(2);
                time = Integer.parseInt(strTime);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return time;
    }


    // get a event by event ID
    public Event getEventbyID(String eventID) {
        Event event = null;
        SimpleDateFormat sdf = new SimpleDateFormat("d/MM/yyyy h:mm:ss aa");
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query("Event", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(0);
                if (id.compareTo(eventID) == 0) {
                    String Title = cursor.getString(cursor.getColumnIndex("title"));
                    String sdate = cursor.getString(cursor.getColumnIndex("sdate"));
                    String edate = cursor.getString(cursor.getColumnIndex("edate"));
                    String venue = cursor.getString(4);
                    String location = cursor.getString(5);
                    try {
                        Date sDate = sdf.parse(sdate);
                        Date eDate = sdf.parse(edate);
                        event = new Event(id, Title, sDate, eDate, venue, location);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                }


            } while (cursor.moveToNext());
        }
        cursor.close();


        return event;
    }

    // Get movie linked event
    public Event getEventbyMovie(Movie movie) {
        String eventID = this.getEventIDbyMovie(movie.getTitle());
        Event event = this.getEventbyID(eventID);

        return event;


    }

    // update event detail
    public void updateEvent(Event newEvent) {
        SimpleDateFormat sdf = new SimpleDateFormat("d/MM/yyyy h:mm:ss aa");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", newEvent.getTitle());
        values.put("sdate", sdf.format(newEvent.getSdate()));
        values.put("edate", sdf.format(newEvent.getEdate()));
        values.put("Venue", newEvent.getVenue());
        values.put("Location", newEvent.getLocation());
        db.update("Event", values, "id=?", new String[]{newEvent.getId()});
    }

    //  movie link to a new event
    public void updateMovie(Movie movie, Event newEvent) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("eventID", newEvent.getId());
        db.update("movieevent", values, "movieid=?", new String[]{movie.getID()});

    }

    // create movie into database
    public void addMovie(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("ID", movie.getID());
        values.put("title", movie.getTitle());
        values.put("year", movie.getYear());
        values.put("posturl", movie.getPostImgUrl().toLowerCase());
        db.insert("movie", null, values);

    }

    // get movies which linked to the event
    public ArrayList<Movie> getMoviebyEvent(Event thisEvent) {
        ArrayList<Movie> movies = new ArrayList<>();
        ArrayList<String> movieIDs = getMovieIds(thisEvent);

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query("Movie", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(0);

                for (int i = 0; i < movieIDs.size(); i++) {
                    if (id.compareTo(movieIDs.get(i)) == 0) {
                        String Title = cursor.getString(cursor.getColumnIndex("title"));
                        String Year = cursor.getString(cursor.getColumnIndex("year"));
                        String postUrl = cursor.getString(3);
                        Movie movie = new Movie(id, Title, Year, postUrl);
                        movies.add(movie);
                    }
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        return movies;

    }

    // get a movie by title
    public Movie getMoviebyTitle(String title) {
        Movie movie = null;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query("Movie", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String Title = cursor.getString(cursor.getColumnIndex("title"));
                if (Title.compareTo(title) == 0) {
                    String id = cursor.getString(0);
                    String Year = cursor.getString(cursor.getColumnIndex("year"));
                    String postUrl = cursor.getString(3);
                    movie = new Movie(id, Title, Year, postUrl);
                    break;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        return movie;

    }

    //  get event ID by a linked movie title
    private String getEventIDbyMovie(String title) {
        String eventID = "";
        String movieID = this.getMoviebyTitle(title).getID();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query("movieevent", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                ;
                if (movieID.compareTo(cursor.getString(0)) == 0) {
                    eventID = cursor.getString(1);
                    break;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        return eventID;

    }

    // get all movie ids which linked to the event
    private ArrayList<String> getMovieIds(Event thisEvent) {
        ArrayList<String> movieIDs = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query("movieevent", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String eventID = cursor.getString(1);
                if (eventID.compareTo(thisEvent.getId()) == 0)
                    movieIDs.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return movieIDs;
    }

    // remove a event
    public void cleanEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("movieevent", "EventID=?", new String[]{event.getId()});
        db.delete("eventattend", "EventID=?", new String[]{event.getId()});
        db.delete("Remind", "eventID=?", new String[]{event.getId()});
        db.delete("Event", "id=?", new String[]{event.getId()});

    }

    // remove all data
    public void cleanAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Event", null, null);
        db.delete("Movie", null, null);
        db.delete("movieevent", null, null);
        db.delete("EventAttend", null, null);
        db.delete("UserSetting", null, null);
        db.delete("Remind", null, null);

    }

    // remove a movie
    public void cleanMovie(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("movieevent", "MovieID=?", new String[]{movie.getID()});
        db.delete("Movie", "id=?", new String[]{movie.getID()});

    }

    //get all attendees of a event
    public ArrayList<Attendees> getAttendeesbyEvent(Event thisEvent) {

        ArrayList<Attendees> attendees = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query("eventattend", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String eventID = cursor.getString(1);
                if (thisEvent.getId().compareTo(eventID) == 0) {
                    String name = cursor.getString(2);
                    String phone = cursor.getString(3);
                    Attendees att = new Attendees(name, phone);
                    attendees.add(att);

                }

            } while (cursor.moveToNext());
        }
        cursor.close();

        return attendees;

    }

    // add a new attendee to event
    public void addAttendee(Attendees newAttendee, Event thisEvent) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        Random rand = new Random();
        values.put("ID", this.getAttendeesbyEvent(thisEvent).size() + "" + rand.nextInt(100));
        values.put("eventId", thisEvent.getId());
        values.put("attendname", newAttendee.getName());
        values.put("attentphone", newAttendee.getPhoneNum());
        db.insert("EventAttend", null, values);


    }

    //delete an attendee
    public void cleanAttendee(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("eventattend", "attendname=?", new String[]{name});


    }

    // linked a movie to event
    public void connectEventMovie(Movie chooseMovie, Event event) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MovieID", chooseMovie.getID());
        values.put("EventID", event.getId());
        db.insert("movieevent", null, values);


    }

    // check a movie have a linked event or not
    public boolean checkMovieExist(Movie movie) {
        boolean check = false;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query("movieevent", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(0);
                if (id.compareTo(movie.getID()) == 0) {
                    check = true;
                    break;
                }


            } while (cursor.moveToNext());
        }
        cursor.close();


        return check;
    }

    // check a date have event or not, return the event title
    public String hasEvent(Date date) {
        String eventTitle = "";

        SimpleDateFormat sdf = new SimpleDateFormat("d/MM/yyyy h:mm:ss aa");
        SimpleDateFormat checkDay = new SimpleDateFormat("yyyy-MM-dd");
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query("Event", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {

                String sdate = cursor.getString(cursor.getColumnIndex("sdate"));
                String edate = cursor.getString(cursor.getColumnIndex("edate"));
                try {
                    Date sDate = sdf.parse(sdate);
                    Date eDate = sdf.parse(edate);
                    if (checkDay.format(date).compareTo(checkDay.format(sDate)) == 0 || checkDay.format(date).compareTo(checkDay.format(eDate)) == 0) {

                        eventTitle = cursor.getString(1);
                        break;
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }


            } while (cursor.moveToNext());
        }
        cursor.close();


        return eventTitle;
    }


    // get a event by event title
    public Event getEventbyTitle(String title) {
        Event event = null;
        SimpleDateFormat sdf = new SimpleDateFormat("d/MM/yyyy h:mm:ss aa");
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query("Event", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String Title = cursor.getString(cursor.getColumnIndex("title"));
                if (title.compareTo(Title) == 0) {
                    String id = cursor.getString(0);
                    String sdate = cursor.getString(cursor.getColumnIndex("sdate"));
                    String edate = cursor.getString(cursor.getColumnIndex("edate"));
                    String venue = cursor.getString(4);
                    String location = cursor.getString(5);
                    try {
                        Date sDate = sdf.parse(sdate);
                        Date eDate = sdf.parse(edate);
                        event = new Event(id, Title, sDate, eDate, venue, location);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                }


            } while (cursor.moveToNext());
        }
        cursor.close();


        return event;

    }

    // update Remind time 'n'
    public void updateRemindTime(String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("Remind", Integer.parseInt(time));
        db.update(" UserSetting", values, "name=?", new String[]{"overall"});

    }
    // update notification Threshold time
    public void updateThresholdTime(String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("Threshold", Integer.parseInt(time));
        db.update(" UserSetting", values, "name=?", new String[]{"overall"});

    }
    // update notification Period time
    public void updatePeriodTime(String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("Period", Integer.parseInt(time));
        db.update(" UserSetting", values, "name=?", new String[]{"overall"});

    }
    // remove all Remind again
    public void removeOldRemind() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Remind", null, null);
    }



        /*  //update movie detail
        public void updateMovie(Movie newMovie) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put("title", newMovie.getTitle());
            values.put("year", newMovie.getYear());
            values.put("posturl", newMovie.getPostImgUrl());
            db.update("Movie", values, "id=?", new String[]{newMovie.getID()});

        }
    */
}
