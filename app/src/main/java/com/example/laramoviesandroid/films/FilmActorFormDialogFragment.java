package com.example.laramoviesandroid.films;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.laramoviesandroid.Actors.ActorFilmFormDialogFragment;
import com.example.laramoviesandroid.Actors.ActorFilmFormSpinnerAdapter;
import com.example.laramoviesandroid.Actors.ActorFilmographyAdapter;
import com.example.laramoviesandroid.R;
import com.example.laramoviesandroid.authentication.AuthenticatedJSONObjectRequest;
import com.example.laramoviesandroid.models.Actor;
import com.example.laramoviesandroid.models.Film;
import com.example.laramoviesandroid.models.FilmActor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

public class FilmActorFormDialogFragment extends DialogFragment {
    FilmActor mFilmActor;
    ArrayList<Actor> mActors;

    TextView mFilmName;
    EditText mEditCharacterName;
    Spinner mActorsSpinner;
    FilmActorFormActorsSpinnerAdapter mSpinnerAdapter;
    int mSelectedActorId;
    String mSelectedActorName;
    FilmActorsListAdapter mParentFilmActorAdapter;

    // factory
    public static FilmActorFormDialogFragment newInstance(FilmActor filmActor, FilmActorsListAdapter parentAdapter) {
        FilmActorFormDialogFragment fragment = new FilmActorFormDialogFragment();
        fragment.mFilmActor = filmActor;
        fragment.mActors = new ArrayList<Actor>();
        fragment.mParentFilmActorAdapter = parentAdapter;
        return fragment;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_film_actor_form_dialog, null);
        mEditCharacterName = view.findViewById(R.id.edit_film_actor_form_character);
        mActorsSpinner = (Spinner) view.findViewById(R.id.spinner_film_actor_form_actors);
        mFilmName = view.findViewById(R.id.text_film_actor_form_film);
        mFilmName.setText(mFilmActor.getFilmName());
        mSpinnerAdapter = new FilmActorFormActorsSpinnerAdapter(
                getContext(),
                R.layout.spinner_textview,
                mActors
        );
        loadSpinnerData();
        loadSpinnerEventListeners();
        builder.setView(view)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FilmActorFormDialogFragment.this.getDialog().cancel();
                    }
                })
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        submit();
                    }
                });

        return builder.create();
    }
    void loadSpinnerData() {
//        get all films for spinner
        AuthenticatedJSONObjectRequest req = new AuthenticatedJSONObjectRequest(
                Request.Method.GET,
                getResources().getString(R.string.api_url) + "actors",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject serverResponse) {
                        try {
                            int actor_film_iterator = 0;
                            int position = -1;
                            JSONObject response = new JSONObject(String.valueOf(serverResponse));
                            JSONArray films = response.getJSONArray("actors");
                            for(int i = 0; i < films.length(); i++) {
                                JSONObject film = new JSONObject(String.valueOf(films.getJSONObject(i)));

                                Actor newActor = Actor.buildFromJSON(film);
                                if(newActor.getId() == mFilmActor.getActorId()) {
                                    position = actor_film_iterator;
                                } else {
                                    actor_film_iterator++;
                                }

                                mActors.add(newActor);
                            }
                            mActorsSpinner.setAdapter(mSpinnerAdapter);
//                            select the film the corresponds to the current entry
//                            this will only get to > 0 when it is called via edit
                            if(position > -1 ) {
                                mActorsSpinner.setSelection(position);
                                mEditCharacterName.setText(mFilmActor.getCharacterName());
//                                disable editing the spinner to ensure that only the film will be edited
                                mActorsSpinner.setClickable(false);
                                mActorsSpinner.setEnabled(false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
        );
        RequestQueue q = Volley.newRequestQueue(getContext());
        q.add(req);
    }

    void loadSpinnerEventListeners() {
        mActorsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedActorId = mSpinnerAdapter.getItem(position).getId();
                mSelectedActorName = mSpinnerAdapter.getItem(position).getName();
                Log.d("actorFilmSpinner", "Spinner changed, selected ID: " + mSelectedActorId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    void submit() {
        JSONObject params = new JSONObject();
        try {
            params.put("actor_id", mSelectedActorId);
            params.put("film_id", mFilmActor.getFilmId());
            params.put("character", mEditCharacterName.getText());
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        Log.d(null, "Submitted parameters: " + params.toString());

        AuthenticatedJSONObjectRequest req = new AuthenticatedJSONObjectRequest(
                Request.Method.POST,
                getResources().getString(R.string.api_url) + "films/submit-film-actor",
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            response = new JSONObject(String.valueOf(response));
                            Toast.makeText(getParentFragment().getContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                            JSONObject filmActorInfo = response.getJSONObject("film_actor_info");
                            mFilmActor.setActorId(filmActorInfo.getInt("actor_id"));
                            mFilmActor.setFilmId(filmActorInfo.getInt("film_id"));
                            mFilmActor.setCharacterName(filmActorInfo.getString("character"));
                            mFilmActor.setActorName(mSelectedActorName);

                            String entityStatus = response.getString("action");
                            boolean isNewEntity = false;
                            if (entityStatus.equals("add")) {
                                isNewEntity = true;
                            }
                            if(isNewEntity) {
                                // add to parent adapter list
                                mParentFilmActorAdapter.addFilmographyEntry(mFilmActor);
                            } else {
                                mParentFilmActorAdapter.searchAndUpdateFilmographyEntry(mFilmActor);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        RequestQueue q = Volley.newRequestQueue(getContext());
        q.add(req);
    }
}

