package com.example.laramoviesandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.example.laramoviesandroid.SQLiteClasses.RememberedUserManager;
import com.example.laramoviesandroid.Singletons.GlobalMembers;
import com.example.laramoviesandroid.authentication.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FragmentManager mFragmentManager;
    private int mFragmentContainerId;

    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
//        test activities for now
        GlobalMembers globals = GlobalMembers.getInstance();
        String accessToken = globals.accessToken;
        mFragmentManager = getSupportFragmentManager();
        mFragmentContainerId = R.id.fl_main_fragment_container;
        mContext = this;
        Log.i(null, "Main Activity accesss token:"+accessToken);
        launchNewFragment(new MainMenuFragment(), false);
    }

    public void launchNewFragment(Fragment fragment, boolean pushToBackStack) {
         FragmentTransaction ft = mFragmentManager.beginTransaction();
         if(pushToBackStack) {
             ft.addToBackStack(null);
         }
         ft.replace(mFragmentContainerId, fragment).commit();
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
//                            Remove remembered user
                            RememberedUserManager rum = new RememberedUserManager(mContext);
                            rum.removeRemember();

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
                String accessToken = GlobalMembers.getInstance().accessToken;
                params.put("Authorization", "Bearer " + accessToken);
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}