package com.example.laramoviesandroid.models;

public class FilmActor {
    private int actorId;
    private int filmId;
    private String actorName;
    private String filmName;
    private String characterName;

    public int getActorId() {
        return actorId;
    }

    public FilmActor setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getFilmId() {
        return filmId;
    }

    public FilmActor setFilmId(int filmId) {
        this.filmId = filmId;
        return this;
    }

    public String getActorName() {
        return actorName;
    }

    public FilmActor setActorName(String actorName) {
        this.actorName = actorName;
        return this;
    }

    public String getFilmName() {
        return filmName;
    }

    public FilmActor setFilmName(String filmName) {
        this.filmName = filmName;
        return this;
    }

    public String getCharacterName() {
        return characterName;
    }

    public FilmActor setCharacterName(String characterName) {
        this.characterName = characterName;
        return this;
    }


}
