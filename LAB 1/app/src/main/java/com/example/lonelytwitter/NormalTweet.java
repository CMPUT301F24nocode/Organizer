package com.example.lonelytwitter;

import java.util.Date;

public class NormalTweet extends Tweet{

    public NormalTweet(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "This is a Normal Tweet";
    }

    @Override
    public Boolean isImportant() {
        return Boolean.FALSE;
    }

    @Override
    public Date getDate() {
        return null;
    }
}
