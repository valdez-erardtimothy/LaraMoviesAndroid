package com.example.laramoviesandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
//        test activities for now
        this.launchTestActivity();
    }

    /**
     * put the activities we want to test here
     */
    public void launchTestActivity() {

        Intent intent = new Intent(this, FilmActivity.class);
        startActivity(intent);
    }
}