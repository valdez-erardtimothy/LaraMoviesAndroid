package com.example.laramoviesandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String mAccessToken;
    Button mButtonFilms;
    Button mButtonActors;
    Button mButtonProducers;
    Button mButtonLogout;

    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
//        test activities for now
        Intent intent = getIntent();
        mAccessToken = intent.getStringExtra("access_token");
        mContext = this;
        Log.i(null, "Main Activity accesss token:"+mAccessToken);
        this.initializeMembers();
    }

    /**
     * put the activities we want to test here
     */
    public void launchTestActivity() {

        Intent intent = new Intent(this, FilmActivity.class);
        startActivity(intent);
    }

    void initializeMembers() {
        mButtonFilms = (Button) findViewById(R.id.btn_main_films);
        mButtonActors = (Button) findViewById(R.id.btn_main_actors);
        mButtonProducers = (Button) findViewById(R.id.btn_main_producers);
        mButtonLogout = (Button) findViewById(R.id.btn_main_logout);

        this.initializeButtonListeners();
    }

    void initializeButtonListeners() {
        mButtonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.logout();
            }
        });
    }

    void logout() {
        String url = getResources().getString(R.string.api_url) + "auth/logout";

        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Do something with response
                        //mTextView.setText(response.toString());

                        // Process the JSON
                        try{
                            // Get the JSON array

                            Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                            Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_SHORT).show();
                            mContext.startActivity(intent);
                            finish();

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        Toast.makeText(mContext, "Logout Error", Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String accessToken;
                params.put("Authorization", "Bearer " + MainActivity.this.mAccessToken);
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}