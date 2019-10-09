package com.movie.myapplication.model;

public class AddAttendeeParams {
    private Attendees attendees;
    private Event event;

    public AddAttendeeParams(Attendees attendees, Event event) {
        this.attendees = attendees;
        this.event = event;
    }

    public Attendees getAttendees() {
        return attendees;
    }

    public Event getEvent() {
        return event;
    }
}

