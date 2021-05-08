package com.example.laramoviesandroid.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Actor {
    private int id;
    private String name;
    private String notes;
    private JSONObject jsonData;
    private String portraitUrl;

    public Actor() {

    }

    public int getId() {
        return id;
    }

    public Actor setId(int id) {
        this.id = id;
        return this;
    }

    public String getNotes() {
        return notes;
    }

    public Actor setNotes(String notes) {
        this.notes = notes;
        return this;
    }

    public String getName() {
        return name;
    }

    public Actor setName(String name) {
        this.name = name;
        return this;
    }

    public JSONObject getJsonData() {
        return jsonData;
    }

    public Actor setJsonData(JSONObject jsonData) {
        this.jsonData = jsonData;
        return this;
    }


    public String getPortraitUrl() {
        return portraitUrl;
    }

    public Actor setPortraitUrl(String portraitUrl) {
        this.portraitUrl = portraitUrl;
        return this;
    }

    public static Actor buildFromJSON(JSONObject actorJSON) {
        try {
            Actor newActor = new Actor()
                    .setJsonData(actorJSON)
                    .setId(actorJSON.getInt("id"))
                    .setName(actorJSON.getString("actor_fullname"))
                    .setPortraitUrl(actorJSON.getString("portrait"))
                    .setNotes(actorJSON.has("actor_notes")
                            ?actorJSON.getString("actor_notes")
                            :"None"
                    );
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
