package com.example.lonelytwitter;

import java.util.Date;

public class CurrentMood {
    private String mood;

    private Date date;

    public CurrentMood(String mood) {
        this.mood = mood;
        this.date = new Date();
    }

    public CurrentMood(String mood, Date date) {
        this.mood = mood;
        this.date = date;
    }

    public String getMood() {
        return mood;
    }

    public Date getDate() {
        return date;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
