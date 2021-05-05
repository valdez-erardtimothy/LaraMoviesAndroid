package com.example.laramoviesandroid.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import com.example.laramoviesandroid.builders.FilmBuilder;

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

//  getters setters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }
    public String getFormattedReleaseDate() {
        return new SimpleDateFormat("MMMM dd, yyyy").format(this.releaseDate);
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
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

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additional_info) {
        this.additionalInfo = additional_info;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genre_id) {
        this.genreId = genre_id;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPosterURL() {
        return posterURL;
    }

    public void setPosterURL(String posterURL) {
        this.posterURL = posterURL;
    }

    public JSONObject getJson() {
        return json;
    }

    public void setJson(JSONObject json) {
        this.json = json;
    }
}
