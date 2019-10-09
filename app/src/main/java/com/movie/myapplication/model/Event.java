package com.movie.myapplication.model;

import java.util.Date;

public class Event {

    private String Id;
    private String Title;
    private Date Sdate;
    private Date Edate;
    private String Venue;
    private String Location;


    public Event(String id, String title, Date sdate, Date edate, String venue, String location) {
        Id = id;
        Title = title;
        Sdate = sdate;
        Edate = edate;
        Venue = venue;
        Location = location;

    }

    public String getId() {
        return Id;
    }

    public String getTitle() {
        return Title;
    }

    public Date getSdate() {
        return Sdate;
    }

    public Date getEdate() {
        return Edate;
    }

    public String getVenue() {
        return Venue;
    }

    public String getLocation() {
        return Location;
    }


}
