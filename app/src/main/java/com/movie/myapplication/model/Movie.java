package com.movie.myapplication.model;

public class Movie {

    private String ID;
    private String Title;
    private String Year;
    private String PostImgUrl;

    public Movie(String ID, String title, String year, String postImgUrl) {
        this.ID = ID;
        Title = title;
        Year = year;
        PostImgUrl = postImgUrl;
    }

    public String getID() {
        return ID;
    }

    public String getTitle() {
        return Title;
    }

    public String getYear() {
        return Year;
    }

    public String getPostImgUrl() {
        return PostImgUrl;
    }
}
