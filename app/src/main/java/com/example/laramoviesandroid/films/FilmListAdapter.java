package com.example.laramoviesandroid.films;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.laramoviesandroid.Actors.ActorEditFragment;
import com.example.laramoviesandroid.Actors.ActorListAdapter;
import com.example.laramoviesandroid.MainActivity;
import com.example.laramoviesandroid.R;
import com.example.laramoviesandroid.authentication.AuthenticatedJSONObjectRequest;
import com.example.laramoviesandroid.models.Film;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FilmListAdapter extends RecyclerView.Adapter<FilmListAdapter.FilmListItemViewHolder> {
    private ArrayList<Film> mFilms;
    private FragmentManager mParentActivityFragmentManager;



    // the ViewHolder to be made
    public  class FilmListItemViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener, View.OnLongClickListener{
        private  TextView mTvTitle, mTvRelease, mTvDuration, mTvGenre;
        private  ImageView mIvPoster;
        public Film currentFilm;
        public FilmListItemViewHolder(View view) {
            super(view);
//widgets from the layout intended for the recycler view items
            mTvTitle = (TextView) view.findViewById(R.id.tv_film_list_item_title);
            mTvRelease = (TextView) view.findViewById(R.id.tv_film_list_item_release_date);
            mTvDuration = (TextView) view.findViewById(R.id.tv_film_list_item_duration);
            mTvGenre = (TextView) view.findViewById(R.id.tv_film_list_item_genre);
            mIvPoster = (ImageView) view.findViewById(R.id.image_film_list_item_poster);

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }


        @Override
        public void onClick(View v) {
            Log.i(null, "Clicked "+ currentFilm.getTitle());
            FilmViewDialogFragment filmView = new FilmViewDialogFragment();
            filmView.setFilm(currentFilm);
            filmView.show(FilmListAdapter.this.mParentActivityFragmentManager, "");
        }

        @Override
        public boolean onLongClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Select an action")
                    .setItems(R.array.item_actions, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("actorlistlongpressaction", "Pressed which: " + Integer.toString(which));
                            // The 'which' argument contains the index position
                            // of the selected item
                            switch (which) {
                                case 0:
                                    MainActivity rootActivity = (MainActivity) v.getContext();
                                    rootActivity.launchNewFragment(
                                            FilmEditFragment.newInstance(currentFilm),true
                                    );
                                    break;
                                case 1:
                                    // delete button
                                    deleteRequest(currentFilm, getAdapterPosition(), v.getContext());
                                    break;
                            }
                        }
                    }).show();
            return false;
        }
    }

    public FilmListAdapter(ArrayList<Film> dataSet, FragmentManager parentActivityFragmentManager) {
        mFilms = dataSet;
        this.mParentActivityFragmentManager = parentActivityFragmentManager;
    }

    void deleteRequest(Film toDelete, int position, Context context) {
        AuthenticatedJSONObjectRequest deleteRequest = new AuthenticatedJSONObjectRequest(
                Request.Method.GET,
                context.getResources().getString(R.string.api_url) + "films/" + Integer.toString(toDelete.getId())+"/delete",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // delete successful
                        try {
                            Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mFilms.remove(position);
                        FilmListAdapter.this.notifyItemRemoved(position);
                    }
                }
        );
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(deleteRequest);
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public FilmListItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.film_list_item, viewGroup, false);
        return new FilmListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmListItemViewHolder holder, int position) {
//        get the current entry to bind
        holder.currentFilm = mFilms.get(position);
        Film currentItem = holder.currentFilm;

//        set the fields
        holder.mTvTitle.setText(currentItem.getTitle());
        holder.mTvRelease.setText(currentItem.getFormattedReleaseDate());
//        holder.mTvGenre.setText(Integer.toString(currentItem.getGenreId()));
        holder.mTvGenre.setText(currentItem.getGenre());
        holder.mTvDuration.setText(currentItem.getDurationString());
//        set the image
        Picasso.get()
                .load(currentItem.getPosterURL())
                .resize(200,300)
                .into(holder.mIvPoster);
    }

    @Override
    public int getItemCount() {
        return mFilms.size();
    }


}
