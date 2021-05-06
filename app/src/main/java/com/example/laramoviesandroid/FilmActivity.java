package com.example.laramoviesandroid;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class FilmActivity extends AppCompatActivity {
    private Context mContext;
    private FragmentManager mFragmentManager;
    private int mFragmentContainerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film);
        this.setMemberProperties();
        this.showFilmListFragment();
//        initially, this will show film list
    }

    protected void showFilmListFragment() {
//      get the placeholder layout for Fragment
        mFragmentManager.beginTransaction()
            .replace(R.id.fl_film_activity_fragment_container, new FilmListFragment(mFragmentManager), null)
            .commit();

    }

    protected void setMemberProperties() {
        this.mContext = this;
        this.mFragmentManager = getSupportFragmentManager();
        this.mFragmentContainerId = R.id.fl_film_activity_fragment_container;
    }

}
