package com.example.lonelytwitter;

public class isSad extends CurrentMood{
    public isSad(String mood) {
        super(mood);
    }

    @Override
    public String getMood() {
        return "My current mood is Sadness";
    }
}
