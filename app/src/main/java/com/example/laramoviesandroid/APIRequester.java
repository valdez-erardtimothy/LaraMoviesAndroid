package com.example.laramoviesandroid;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

/**
 * discontinued use for now
 */
public class APIRequester {
    private String api_domain;
    private String endpoint;
    private static APIRequester INSTANCE;

    public APIRequester() {
        this.api_domain = "http://192.168.100.201:8000/api/";
    }

    public static APIRequester getInstance() {
        if(APIRequester.INSTANCE == null) {
            APIRequester.INSTANCE = new APIRequester();
        }
        return APIRequester.INSTANCE;
    }

    // for B in bread
//    public void getList(String endpoint) {
//        String fullRequestURI = this.api_domain + endpoint;
//        JsonArrayRequest request = new JsonArrayRequest(
//                Request.Method.GET,
//                fullRequestURI,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.d("listquery", response.toString());
////                        return response.getJSONArray(0);
//                    }
//                    @Override
//                    public void onError
//                }
//        );
//        return null;
//    }

}
