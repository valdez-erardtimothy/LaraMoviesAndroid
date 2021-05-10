package com.example.laramoviesandroid.films;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laramoviesandroid.R;
import com.example.laramoviesandroid.models.FilmActor;

import java.util.ArrayList;

public class FilmActorsListAdapter extends  RecyclerView.Adapter<FilmActorsListAdapter.FilmActorsViewHolder>{
    private ArrayList<FilmActor> mFilmActors;
     private boolean mHoldersEditable;

     public FilmActorsListAdapter(ArrayList<FilmActor> dataSet, boolean editable) {
         mFilmActors = dataSet;
         mHoldersEditable = editable;

     }
    @NonNull
    @Override
    public FilmActorsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view = LayoutInflater.from(parent.getContext())
                 .inflate(R.layout.film_actors_item, parent, false);
         return new FilmActorsViewHolder(view, mHoldersEditable);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmActorsViewHolder holder, int position) {
         FilmActor currentFilmActor = mFilmActors.get(position);
         holder.boundFilmActor = currentFilmActor;
         holder.mTvActorName.setText(currentFilmActor.getActorName());
         holder.mTvCharacter.setText(currentFilmActor.getCharacterName());
    }

    @Override
    public int getItemCount() {
        return mFilmActors.size();
    }

    public class FilmActorsViewHolder extends RecyclerView.ViewHolder {
        private boolean mHolderEditable;
        private TextView mTvActorName, mTvCharacter;
        private FilmActor boundFilmActor;
         public FilmActorsViewHolder(@NonNull View itemView, boolean editable) {
            super(itemView);
            mHolderEditable = editable;
            mTvActorName = itemView.findViewById(R.id.text_film_actors_item_actor_name);
            mTvCharacter = itemView.findViewById(R.id.text_film_actors_item_character_name);

        }
    }
}
