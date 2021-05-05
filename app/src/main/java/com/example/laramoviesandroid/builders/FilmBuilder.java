package com.example.laramoviesandroid.builders;

import com.example.laramoviesandroid.models.Film;

import java.util.Date;

/**
 * a builder class meant to facilitate building a new film object.
 */
public class FilmBuilder {
    private Film film;

    public FilmBuilder() {
        this.film = new Film();
    }
    public FilmBuilder(int id) {
        this.film = new Film(id);
    }

//  setters
    public FilmBuilder setTitle(String value){
        film.setTitle(value);
        return this;
    }
    public FilmBuilder setDuration(int value){
        film.setDuration(value);
        return this;
    }
    public FilmBuilder setReleaseDate(Date value){
        film.setReleaseDate(value);
        return this;
    }
    public FilmBuilder setGenre(int value){
        film.setGenreId(value);
        return this;
    }
    public FilmBuilder setStory(String value){
        film.setStory(value);
        return this;
    }
    public FilmBuilder setAdditionalInfo(String value) {
        film.setAdditionalInfo(value);
        return this;
    }

    public FilmBuilder setGenre(String genre) {
        film.setGenre(genre);
        return this;
    }

    public FilmBuilder setPosterURL(String imageURL) {
        film.setPosterURL(imageURL);
        return this;
    }
//  return the built object
    public Film build() {
        return film;
    }





}
