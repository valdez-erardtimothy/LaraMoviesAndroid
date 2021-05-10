package com.example.laramoviesandroid.authentication;

import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.laramoviesandroid.Singletons.GlobalMembers;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * request that automatically attaches bearer
 */
public class AuthenticatedJSONObjectRequest extends JsonObjectRequest {
    public AuthenticatedJSONObjectRequest(int method, String url, @Nullable JSONObject jsonRequest, Response.Listener<JSONObject> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    public AuthenticatedJSONObjectRequest(String url, @Nullable JSONObject jsonRequest, Response.Listener<JSONObject> listener, @Nullable Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
    }

    /**
     * JSONObjectRequest with an default errorlistener that gets the
     * @param url the full link to the server endpoint
     * @param jsonRequestParameters the parameters to be passed
     */
    public AuthenticatedJSONObjectRequest(int method, String url, @Nullable JSONObject jsonRequestParameters, Response.Listener<JSONObject> listener) {
        super(method, url, jsonRequestParameters, listener, AuthenticatedJSONObjectRequest.defaultErrorListener());
    }

    public static Response.ErrorListener defaultErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        };

    }

    /**
     * sets the default Authorization header
     * @return Map of all headers. used internally.
     */
    @Override
    public Map<String,String> getHeaders() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        String accessToken = GlobalMembers.getInstance().accessToken;
        params.put("Authorization", "Bearer " + accessToken);
        return params;
    }
}
