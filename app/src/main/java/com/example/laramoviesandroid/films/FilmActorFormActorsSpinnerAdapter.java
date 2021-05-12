package com.example.laramoviesandroid.films;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.laramoviesandroid.R;
import com.example.laramoviesandroid.models.Actor;
import com.example.laramoviesandroid.models.Film;

import java.util.ArrayList;

public class FilmActorFormActorsSpinnerAdapter extends ArrayAdapter<Actor> {
    ArrayList<Actor> actors;

    public FilmActorFormActorsSpinnerAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Actor> objects) {
        super(context, resource, objects);
        actors = objects;
    }
    @Override
    public int getCount() {
        return  actors.size();
    }

    @Override
    public Actor getItem(int position ) {
        return actors.get(position);
    }

    @Override
    public long getItemId(int position) {
        return actors.get(position).getId();
    }

    // sets the view of the spinner when not pressed
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView filmNameView = (TextView) super.getView(position, convertView,parent);
        filmNameView.setTextColor(getContext().getColor(R.color.purple_700));
        // set the display value
        filmNameView.setText(getItem(position).getName());
        return filmNameView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView dropdownItem = (TextView) super.getDropDownView(position,convertView,parent);
        dropdownItem.setTextColor(Color.BLACK);
        dropdownItem.setText(getItem(position).getName());
        return dropdownItem;

    }
}

