package com.example.laramoviesandroid.films;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laramoviesandroid.R;
import com.example.laramoviesandroid.models.FilmProducer;

import java.util.ArrayList;

public class FilmProducersListAdapter extends RecyclerView.Adapter<FilmProducersListAdapter.FilmProducersListViewHolder> {
    private ArrayList<FilmProducer> mFilmProducers;
    private boolean mHoldersEditable;

    //constructor
    public FilmProducersListAdapter(ArrayList<FilmProducer> filmProducers, boolean editable) {
        mFilmProducers = filmProducers;
        mHoldersEditable = editable;
    }

    @NonNull
    @Override
    public FilmProducersListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.film_producers_item, parent, false);
        return new FilmProducersListViewHolder(view, mHoldersEditable);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmProducersListViewHolder holder, int position) {
        holder.boundProducer = mFilmProducers.get(position);
        holder.mTvProducerName.setText(holder.boundProducer.getProducerName());
    }

    @Override
    public int getItemCount() {
        return mFilmProducers.size();
    }

    public class FilmProducersListViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvProducerName;
        private FilmProducer boundProducer;
        private boolean mHolderEditable;
        public FilmProducersListViewHolder(@NonNull View itemView, boolean editable) {
            super(itemView);
            mTvProducerName = itemView.findViewById(R.id.text_film_producers_item_producer_name);
            this.mHolderEditable = editable;
        }

    }
}
