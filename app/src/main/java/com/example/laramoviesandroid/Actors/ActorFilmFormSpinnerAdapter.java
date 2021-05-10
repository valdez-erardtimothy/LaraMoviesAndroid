package com.example.laramoviesandroid.Actors;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.laramoviesandroid.R;
import com.example.laramoviesandroid.models.Film;

import org.w3c.dom.Text;

import java.util.ArrayList;

//https://stackoverflow.com/questions/1625249/android-how-to-bind-spinner-to-custom-object-list
public class ActorFilmFormSpinnerAdapter extends ArrayAdapter<Film> {
    ArrayList<Film> films;

    public ActorFilmFormSpinnerAdapter(@NonNull Context context, int resource,@NonNull ArrayList<Film> objects) {
        super(context, resource, objects);
        films = objects;

    }

    @Override
    public int getCount() {
        return  films.size();
    }

    @Override
    public Film getItem( int position ) {
        return films.get(position);
    }

    @Override
    public long getItemId(int position) {
        return films.get(position).getId();
    }

    // sets the view of the spinner when not pressed
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView filmNameView = (TextView) super.getView(position, convertView,parent);
        filmNameView.setTextColor(getContext().getColor(R.color.purple_700));
        // set the display value
        filmNameView.setText(getItem(position).getTitle());
        return filmNameView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView dropdownItem = (TextView) super.getDropDownView(position,convertView,parent);
        dropdownItem.setTextColor(Color.BLACK);
        dropdownItem.setText(getItem(position).getTitle());
        return dropdownItem;

    }




}
