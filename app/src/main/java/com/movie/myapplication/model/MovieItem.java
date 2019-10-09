package com.movie.myapplication.model;

public class MovieItem {

    private String title;
      private String year;
      private int imgID;
      public MovieItem (String title,String year,int imgID){
               this.title=title;
               this.year=year;
               this.imgID=imgID;
      }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public int getimgID() {
        return imgID;
    }
}
