package com.movie.myapplication.model;

public class Item {
    private String title;
    private String sDate;

    public Item(String title, String sDate) {
        this.title = title;
        this.sDate = sDate;
    }

    public String getTitle() {
        return title;
    }

    public String getsDate() {
        return sDate;
    }
}
