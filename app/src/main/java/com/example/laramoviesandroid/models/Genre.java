package com.example.laramoviesandroid.models;

public class Genre {
    private int id;
    private String genre;

    public int getId() {
        return id;
    }

    public Genre setId(int id) {
        this.id = id;
        return this;
    }

    public String getGenre() {
        return genre;
    }

    public Genre setGenre(String genre) {
        this.genre = genre;
        return this;
    }
}
