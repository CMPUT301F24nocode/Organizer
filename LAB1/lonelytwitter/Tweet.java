package com.example.lonelytwitter;

import java.util.Date;


public abstract class Tweet implements Tweetable{
    private String message;
    private Date date;

    public Tweet(String message) {
        this.message = message;
        this.date = new Date();
    }

    public Tweet(String message, Date date) {
        this.message = message;
        this.date = date;
    }

    public abstract Boolean isImportant();

        public void setDate(Date date) {
            this.date = date;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public Date getDate() {
            return date;
        }
}
