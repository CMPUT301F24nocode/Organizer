package com.example.lonelytwitter;

public class isHappy extends CurrentMood{

    public isHappy(String mood) {
        super(mood);
    }

    @Override
    public String getMood() {
        return "My current mood is Happiness";
    }
}
