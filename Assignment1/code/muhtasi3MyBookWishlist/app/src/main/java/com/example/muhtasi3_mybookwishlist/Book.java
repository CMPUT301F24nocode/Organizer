package com.example.muhtasi3_mybookwishlist;

import java.io.Serializable;

//Implements the Serializable interface to allow instances of class to be passed via Intents.
public class Book implements Serializable {
    private String title;
    private String author;
    private String genre;
    private int year;
    private boolean isRead;

    public Book(String title, String author, String genre, int year, boolean isRead) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.year = year;
        this.isRead = isRead;
    }

    // Getters and Setters for each information about Book
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
