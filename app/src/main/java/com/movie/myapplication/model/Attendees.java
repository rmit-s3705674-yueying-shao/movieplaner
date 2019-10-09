package com.movie.myapplication.model;

public class Attendees {
    private String Name;
    private String PhoneNum;


    public Attendees(String name, String phoneNum) {
        Name = name;
        PhoneNum = phoneNum;

    }

    public String getName() {
        return Name;
    }

    public String getPhoneNum() {
        return PhoneNum;
    }


}
