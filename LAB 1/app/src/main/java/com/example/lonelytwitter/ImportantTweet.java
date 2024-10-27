package com.example.lonelytwitter;

import java.util.Date;

public class ImportantTweet extends Tweet {

    public ImportantTweet(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "This is an Important Tweet";
    }

    @Override
    public Boolean isImportant() {
        return Boolean.TRUE;
    }

    @Override
    public Date getDate() {
        return null;
    }
}
