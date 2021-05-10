package com.example.laramoviesandroid.films;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.laramoviesandroid.R;
import com.example.laramoviesandroid.authentication.AuthenticatedJSONObjectRequest;
import com.example.laramoviesandroid.models.Film;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;


public class FilmViewDialogFragment extends DialogFragment {
    private Film mFilm; // send the film to show here
    private FilmProducersListAdapter mFilmProducersAdapter;
    private FilmActorsListAdapter mFilmActorsAdapter;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_film_view_dialog, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_film_view_title);
        TextView tvGenre = (TextView) view.findViewById(R.id.tv_film_view_genre);
        TextView tvReleaseDate = (TextView) view.findViewById(R.id.tv_film_view_release_date);
        TextView tvDuration = (TextView) view.findViewById(R.id.tv_film_view_duration);
        TextView tvStory = (TextView) view.findViewById(R.id.tv_film_view_story);
        TextView tvAdditionalInfo = (TextView) view.findViewById(R.id.tv_film_view_additional_info);
        ImageView ivPoster = (ImageView) view.findViewById(R.id.image_film_view_poster);
        RecyclerView rvFilmActors = view.findViewById(R.id.rv_film_view_actors);
        RecyclerView rvFilmProducers = view.findViewById(R.id.rv_film_view_producers);
        mFilmProducersAdapter = new FilmProducersListAdapter(mFilm.getFilmProducers(), false);
        mFilmActorsAdapter = new FilmActorsListAdapter(mFilm.getFilmActors(), false);

        //set up recyclerviews
        rvFilmActors.setHasFixedSize(true);
        rvFilmActors.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFilmActors.setAdapter(mFilmActorsAdapter);

        rvFilmProducers.setHasFixedSize(true);
        rvFilmProducers.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFilmProducers.setAdapter(mFilmProducersAdapter);

        // set the contents
        tvTitle.setText(mFilm.getTitle());
        tvGenre.setText(mFilm.getGenre());
        tvReleaseDate.setText(mFilm.getFormattedReleaseDate());
        tvDuration.setText(mFilm.getDurationString());
        tvStory.setText(mFilm.getStory());
        tvAdditionalInfo.setText(mFilm.getAdditionalInfo());

        loadFilmActorsAndProducers();
        Picasso.get().load(mFilm.getPosterURL()).into(ivPoster);
        builder.setView(view)
                .setPositiveButton(R.string.edit, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        redirect to edit
                    }
                })
                .setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                })
                .setNeutralButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FilmViewDialogFragment.this.getDialog().cancel();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
    public void setFilm(Film film) {
        this.mFilm = film;
    }

    void loadFilmActorsAndProducers() {
        AuthenticatedJSONObjectRequest req = new AuthenticatedJSONObjectRequest(
                Request.Method.GET,
                getResources().getString(R.string.api_url) + "films/" + mFilm.getId(),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject filmJSON  = new JSONObject(String.valueOf(response.getJSONObject("film")));
                            Log.d(null,"response film: "+ response);
                            mFilm.buildFilmActorsFromJSON(filmJSON);
                            mFilm.buildFilmProducersFromJSON(filmJSON);
                            mFilmProducersAdapter.notifyDataSetChanged();
                            mFilmActorsAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            Log.e("filmDetailRetrievalError", "Error in retrieving Film");
                            e.printStackTrace();
                        }

                    }
                }
        );
        Volley.newRequestQueue(getContext()).add(req);
    }
}
