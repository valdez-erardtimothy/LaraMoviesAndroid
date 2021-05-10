package com.example.laramoviesandroid.Actors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laramoviesandroid.R;
import com.example.laramoviesandroid.models.FilmActor;

import java.util.ArrayList;

public class ActorFilmographyAdapter extends RecyclerView.Adapter<ActorFilmographyAdapter.ActorFilmographyViewHolder> {
    private ArrayList<FilmActor>mActorFilmography;
    private boolean mContentsEditable;
    @NonNull
    @Override
    public ActorFilmographyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.actor_filmography_item, parent, false);
        return new ActorFilmographyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActorFilmographyViewHolder holder, int position) {
        holder.selectedActorFilm = mActorFilmography.get(position);
        holder.tvCharacterName.setText(holder.selectedActorFilm.getCharacterName());
        holder.tvFilmTitle.setText(holder.selectedActorFilm.getFilmName());
    }

    @Override
    public int getItemCount() {
        return mActorFilmography.size();
    }

    /**
     * the view for actor filmography items.
     * set all the filmography item interaction here.
     */
    public class ActorFilmographyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvCharacterName;
        private TextView tvFilmTitle;
        public FilmActor selectedActorFilm;
        public ActorFilmographyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCharacterName = (TextView) itemView.findViewById(R.id.text_actor_filmography_item_character);
            tvFilmTitle = (TextView) itemView.findViewById(R.id.text_actor_filmography_item_film_title);
        }
    }

    public ActorFilmographyAdapter(ArrayList<FilmActor> dataSet) {
        this.mActorFilmography = dataSet;
        mContentsEditable = false;
    }

    /**
     *
     * @param dataSet
     * @param editable true if in actor edit, false (or leave blank) if in actor view.
     */
    public ActorFilmographyAdapter(ArrayList<FilmActor> dataSet, boolean editable) {
        this.mActorFilmography = dataSet;
        this.mContentsEditable = editable;
    }

}
