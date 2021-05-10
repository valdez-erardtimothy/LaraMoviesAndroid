package com.example.laramoviesandroid.models;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Film {
    private int id;
    private String title;
    private Date releaseDate;
    private int duration; // in minutes
    private String story;
    private String additionalInfo;
    private int genreId;
    private String genre;
    private String posterURL;
    private ArrayList<FilmActor>filmActors;
    private ArrayList<FilmProducer>filmProducers;

    public Film() {
        this(0); // set for creating new ID
    }

    public Film(int id) {
         this.id = id;
         filmActors = new ArrayList<FilmActor>();
         filmProducers = new ArrayList<FilmProducer>();
    }
//  api-connecting methods
//  not used as of now, can't find a pattern I like for this
    public static ArrayList<Film> getAll() {
    return null;
    }


    /**
     * sets the film object attributes with data from json object
     * @param jsonData the json object that will populate the Film object
     * @return
     * @throws JSONException
     */
    public Film buildFromJSON(JSONObject jsonData) throws JSONException, ParseException {
        String genre;
        try {
            genre = new JSONObject(String.valueOf(jsonData.getJSONObject("genre"))).getString("genre");
        } catch (JSONException e) {
            genre = "None";
        }
        jsonData = new JSONObject(String.valueOf(jsonData));
        this.setId(jsonData.getInt("id"))
                .setTitle(jsonData.getString("film_title"))
                .setStory(jsonData.getString("story"))
                .setReleaseDate(new SimpleDateFormat("yyyy-mm-dd")
                        .parse(jsonData.getString("release_date")))
                .setPosterURL(jsonData.getString("poster"))
                .setDuration(jsonData.getInt("duration"))
                .setAdditionalInfo(jsonData.getString("additional_info"))
                .setGenreId(jsonData.getInt("genre_id"))
                .setGenre(genre);

        return this;
    }

    public ArrayList<FilmActor> buildFilmActorsFromJSON(JSONObject filmDetails) throws JSONException {
        filmActors.clear();
        JSONArray filmActorsJSON =  filmDetails.getJSONArray("actors");
        for(int i = 0; i < filmActorsJSON.length(); i++) {
            JSONObject currentFilmActor = filmActorsJSON.getJSONObject(i);
            Log.d(null, "currentFilmActor: " + currentFilmActor);
            JSONObject pivot = currentFilmActor.getJSONObject("pivot");

            this.filmActors.add(
                    new FilmActor()
                    .setFilmId(this.getId())
                    .setFilmName(this.getTitle())
                    .setActorId(pivot.getInt("actor_id"))
                    .setActorName(currentFilmActor.getString("actor_fullname"))
                    .setCharacterName(pivot.getString("character"))
            );
        }
        return this.getFilmActors();
    }

    public ArrayList<FilmProducer> buildFilmProducersFromJSON(JSONObject filmDetails) throws  JSONException {
        filmProducers.clear();
        JSONArray filmProducersJSON = filmDetails.getJSONArray("producers");
        for(int i = 0; i < filmProducersJSON.length(); i++) {
            JSONObject currentFilmProducer = filmProducersJSON.getJSONObject(i);

            this.filmProducers.add(
                    new FilmProducer().setFilmId(this.getId())
                    .setFilmTitle(this.getTitle())
                    .setProducerId(currentFilmProducer.getInt("id"))
                    .setProducerName(currentFilmProducer.getString("producer_fullname"))
            );
        }
        return this.getFilmProducers();
    }

    public static Film newFilmFromJSON(JSONObject jsonData) throws JSONException, ParseException {
        return new Film().buildFromJSON(jsonData);
    }

//  getters setters
    public int getId() {
        return id;
    }

    public Film setId(int id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Film setTitle(String title) {
        this.title = title;
        return this;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }
    public String getFormattedReleaseDate() {
        return new SimpleDateFormat("MMMM dd, yyyy").format(this.releaseDate);
    }

    public Film setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public int getDuration() {
        return duration;
    }
    public String getDurationString() {
        double duration = this.duration;
        int hours = (int)Math.floor(duration/60);
        int minutes = (int)duration%60;
        return Integer.toString(hours) + " hours, " + Integer.toString(minutes) + "minutes";
    }

    public Film setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public String getStory() {
        return story;
    }

    public Film setStory(String story) {
        this.story = story;
        return this;

    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public Film setAdditionalInfo(String additional_info) {
        this.additionalInfo = additional_info;
        return this;
    }

    public int getGenreId() {
        return genreId;
    }

    public Film setGenreId(int genre_id) {
        this.genreId = genre_id;
        return this;

    }

    public String getGenre() {
        return genre;
    }

    public Film setGenre(String genre) {
        this.genre = genre;
        return this;
    }

    public String getPosterURL() {
        return posterURL;
    }

    public Film setPosterURL(String posterURL) {
        this.posterURL = posterURL;
        return this;
    }

    public ArrayList<FilmActor> getFilmActors() {
        return filmActors;
    }

    public Film setFilmActors(ArrayList<FilmActor> filmActors) {
        this.filmActors = filmActors;
        return this;
    }

    public ArrayList<FilmProducer> getFilmProducers() {
        return filmProducers;
    }

    public Film setFilmProducers(ArrayList<FilmProducer> filmProducers) {
        this.filmProducers = filmProducers;
        return this;
    }
}
