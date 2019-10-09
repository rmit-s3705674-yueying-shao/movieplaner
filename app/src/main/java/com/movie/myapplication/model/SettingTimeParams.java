package com.movie.myapplication.model;

public class SettingTimeParams {
    private String period;
    private String threshold;
    private String remind;

    public SettingTimeParams(String period, String threshold, String remind) {
        this.period = period;
        this.threshold = threshold;
        this.remind = remind;
    }

    public String getPeriod() {
        return period;
    }

    public String getThreshold() {
        return threshold;
    }

    public String getRemind() {
        return remind;
    }
}

