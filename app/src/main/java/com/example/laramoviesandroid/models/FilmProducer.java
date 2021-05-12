package com.example.laramoviesandroid.models;

public class FilmProducer {
    private long producerId;
    private String producerName;
    private long filmId;
    private String filmTitle;

    public long getProducerId() {
        return producerId;
    }

    public FilmProducer setProducerId(long producerId) {
        this.producerId = producerId;
        return this;
    }

    public String getProducerName() {
        return producerName;
    }

    public FilmProducer setProducerName(String producerName) {
        this.producerName = producerName;
        return this;
    }

    public long getFilmId() {
        return filmId;
    }

    public FilmProducer setFilmId(long filmId) {
        this.filmId = filmId;
        return this;
    }

    public String getFilmTitle() {
        return filmTitle;
    }

    public FilmProducer setFilmTitle(String filmTitle) {
        this.filmTitle = filmTitle;
        return this;
    }
}
