package com.example.laramoviesandroid.films;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.laramoviesandroid.R;
import com.example.laramoviesandroid.models.Film;
import com.example.laramoviesandroid.models.Genre;

import java.util.ArrayList;

public class FilmGenreSpinnerAdapter extends ArrayAdapter<Genre> {
    ArrayList<Genre> genres;
    public FilmGenreSpinnerAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Genre> objects) {
        super(context, resource, objects);
        this.genres = objects;
    }

    @Override
    public int getCount() {
        return  genres.size();
    }

    @Override
    public Genre getItem(int position ) {
        return genres.get(position);
    }

    @Override
    public long getItemId(int position) {
        return genres.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView genreView = (TextView) super.getView(position, convertView,parent);
        genreView.setTextColor(getContext().getColor(R.color.purple_700));
        // set the display value
        genreView.setText(getItem(position).getGenre());
        return genreView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView dropdownItem = (TextView) super.getDropDownView(position,convertView,parent);
        dropdownItem.setTextColor(Color.BLACK);
        dropdownItem.setText(getItem(position).getGenre());
        return dropdownItem;

    }

}
