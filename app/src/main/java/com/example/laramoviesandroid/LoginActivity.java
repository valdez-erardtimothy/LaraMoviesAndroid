package com.example.laramoviesandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    protected EditText mEmailInput;
    protected EditText mPasswordInput;
    protected Button mLoginButton;
    protected Button mRegisterButton;

    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.initializeMembers();
    }

    protected void initializeMembers() {
        this.mEmailInput = (EditText) findViewById(R.id.input_login_email);
        this.mPasswordInput = (EditText) findViewById(R.id.input_login_password);
        this.mLoginButton = (Button) findViewById(R.id.button_login_login);
        this.mRegisterButton = (Button) findViewById(R.id.button_login_register);
        this.mContext = this.getApplicationContext();
        this.setButtonEventListeners();
    }

    protected void setButtonEventListeners() {
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                Toast.makeText(mContext, "Trying to log in ", Toast.LENGTH_SHORT).show();
                LoginActivity.this.attemptLogin();
            }
        });
    }

    protected void attemptLogin() {
        String url = getResources().getString(R.string.api_url) + "auth/login";
        JSONObject loginInfoJSON = new JSONObject();
        try {
            loginInfoJSON.put("email", mEmailInput.getText());
            loginInfoJSON.put("password", mPasswordInput.getText());
        }catch (JSONException e) {
            e.printStackTrace();
        }

        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                loginInfoJSON,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Do something with response
                        //mTextView.setText(response.toString());

                        // Process the JSON
                        try{
                            // Get the JSON array
                            Log.i(null, "response: " + response.toString());
                            String accessToken = response.getString("token");
                            Toast.makeText(mContext, "Login " +response.getString("status"), Toast.LENGTH_SHORT).show();
//                            shift to the main activity
                            Intent intent=new Intent(LoginActivity.this ,MainActivity.class);
                            intent.putExtra("access_token",accessToken);
                            LoginActivity.this.startActivity(intent);
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
                        Toast.makeText(mContext, "Login Error", Toast.LENGTH_LONG).show();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }


}