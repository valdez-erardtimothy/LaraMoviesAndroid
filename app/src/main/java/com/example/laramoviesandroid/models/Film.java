package com.example.laramoviesandroid.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import com.example.laramoviesandroid.builders.FilmBuilder;

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
    private JSONObject json;

    public Film() {
        this.id = 0; // set for creating new ID
    }

    public Film(int id) {
         this();
         this.id = id;
    }
//  api-connecting methods
//  not used as of now, can't find a pattern I like for this
    public static ArrayList<Film> getAll() {
    return null;
    }

//  builder for easy making of film
    public static FilmBuilder builder() {return new FilmBuilder();}

    public static FilmBuilder builder(int id) {
        return new FilmBuilder(id);
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

    public JSONObject getJson() {
        return json;
    }

    public void setJson(JSONObject json) {
        this.json = json;
    }
}
