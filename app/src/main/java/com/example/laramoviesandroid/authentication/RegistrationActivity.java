package com.example.laramoviesandroid.authentication;

import androidx.appcompat.app.AppCompatActivity;

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
import com.example.laramoviesandroid.MainActivity;
import com.example.laramoviesandroid.R;
import com.example.laramoviesandroid.SQLiteClasses.RememberedUserManager;
import com.example.laramoviesandroid.Singletons.GlobalMembers;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistrationActivity extends AppCompatActivity {
    EditText mEmail, mName, mPassword, mConfirmPassword;
    Button mRegister, mCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        this.initializeUIComponents();
    }

    void initializeUIComponents() {
        mEmail = (EditText) findViewById(R.id.input_registration_email);
        mName = (EditText) findViewById(R.id.input_registration_name);
        mPassword = (EditText) findViewById(R.id.input_registration_password);
        mConfirmPassword = (EditText) findViewById(R.id.input_registration_confirm_password);
        mRegister = (Button) findViewById(R.id.button_registration_register);
        mCancel = (Button) findViewById(R.id.button_registration_cancel);
        this.setButtonClickListeners();
    }
    void setButtonClickListeners() {
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString();
                String name = mName.getText().toString();
                String pass = mPassword.getText().toString();
                String confirm = mConfirmPassword.getText().toString();
                attemptRegister(name, email, pass, confirm);
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    void attemptRegister(String name, String email, String password, String confirm_password) {
        String url = getResources().getString(R.string.api_url)+"auth/register";
        JSONObject arguments = new JSONObject();

        try {
            arguments.put("email", email);
            arguments.put("name", name);
            arguments.put("password", password);
            arguments.put("password_confirmation", confirm_password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(RegistrationActivity.this);

        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                arguments,
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
                            String message = response.getString("message");

                            Toast.makeText(RegistrationActivity.this, message, Toast.LENGTH_SHORT).show();

                            // shift to the main activity

                            Intent intent=new Intent(RegistrationActivity.this , MainActivity.class);
//                            intent.putExtra("access_token",accessToken);
                            GlobalMembers.getInstance().accessToken = accessToken;
                            RegistrationActivity.this.startActivity(intent);
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
                        Toast.makeText(RegistrationActivity.this, "Login Error", Toast.LENGTH_LONG).show();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);

    }

    void ErrorDialog() {

    }

}