package com.example.laramoviesandroid.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Producer {
    private int id;
    private String name = null, email= null, website= null;

    public Producer() {
        this.id = 0;
    }

    public int getId() {
        return id;
    }

    public Producer setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Producer setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Producer setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getWebsite() {
        return website;
    }

    public Producer setWebsite(String website) {
        this.website = website;
        return this;
    }

    /**
     * set an instance's data from the provided producerJson
     *
     * @param instance the instance to be modified
     * @param producerJson the data from server
     * @return instance
     * @throws JSONException when one of the json fields is invalid (usually the server returned no data)
     */
    public static Producer buildInstanceFromJSON(Producer instance, JSONObject producerJson) throws JSONException {
        return instance
                .setId(producerJson.getInt("id"))
                .setName(producerJson.getString("producer_fullname"))
                .setEmail(producerJson.getString("email"))
                .setWebsite(producerJson.getString("website"));

    }

    /**
     *
     * @param producerJson the data for the new producer
     * @return a new producer instance with properties set from producerJson
     */
    public static Producer buildInstanceFromJSON(JSONObject producerJson) throws JSONException {
        return Producer.buildInstanceFromJSON(new Producer(), producerJson);
    }
}
