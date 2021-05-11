package com.example.laramoviesandroid.Actors;

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
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.laramoviesandroid.R;
import com.example.laramoviesandroid.authentication.AuthenticatedJSONObjectRequest;
import com.example.laramoviesandroid.models.Film;
import com.example.laramoviesandroid.models.FilmActor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ActorFilmFormDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActorFilmFormDialogFragment extends DialogFragment {
    FilmActor mActorFilm;
    ArrayList<Film> mFilms;
    TextView mActorName;
    EditText mEditCharacterName;
    Spinner mFilmsSpinner;
    ActorFilmFormSpinnerAdapter mSpinnerAdapter;
    int mSelectedFilmId;
    String mSelectedFilmTitle;
    ActorFilmographyAdapter mParentFilmographyAdapter;

    public ActorFilmFormDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ActorFilmFormDialogFragment.
     */
    public static ActorFilmFormDialogFragment newInstance(FilmActor actorFilm, ActorFilmographyAdapter filmographyAdapter) {
        ActorFilmFormDialogFragment fragment = new ActorFilmFormDialogFragment();
        fragment.mActorFilm = actorFilm;
        fragment.mFilms = new ArrayList<Film>();
        fragment.mParentFilmographyAdapter = filmographyAdapter;
        return fragment;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_actor_film_form_dialog, null);
        mEditCharacterName = view.findViewById(R.id.edit_actor_film_form_character);
        mFilmsSpinner = (Spinner) view.findViewById(R.id.spinner_actor_film_form_films);
        mActorName = view.findViewById(R.id.text_actor_film_form_actor_name);
        mActorName.setText(mActorFilm.getActorName());
        mSpinnerAdapter = new ActorFilmFormSpinnerAdapter(
                getContext(),
//                R.layout.fragment_actor_film_form_dialog,
                R.layout.spinner_textview,
                mFilms
                );
        loadSpinnerData();
        loadSpinnerEventListeners();
        builder.setView(view)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActorFilmFormDialogFragment.this.getDialog().cancel();
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
                getResources().getString(R.string.api_url) + "films",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject serverResponse) {
                        try {
                            int actor_film_iterator = 0;
                            int position = -1;
                            JSONObject response = new JSONObject(String.valueOf(serverResponse));
                            JSONArray films = response.getJSONArray("films");
                            for(int i = 0; i < films.length(); i++) {
                                JSONObject film = new JSONObject(String.valueOf(films.getJSONObject(i)));

                                Film newFilm = Film.newFilmFromJSON(film);
                                if(newFilm.getId() == mActorFilm.getFilmId()) {
                                     position = actor_film_iterator;
                                } else {
                                    actor_film_iterator++;
                                }

                                mFilms.add(newFilm);
                            }
                            mFilmsSpinner.setAdapter(mSpinnerAdapter);
//                            select the film the corresponds to the current entry
//                            this will only get to > 0 when it is called via edit
                            if(position > -1 ) {
                                mFilmsSpinner.setSelection(position);
                                mEditCharacterName.setText(mActorFilm.getCharacterName());
//                                disable editing the spinner to ensure that only the film will be edited
                                mFilmsSpinner.setClickable(false);
                                mFilmsSpinner.setEnabled(false);
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }
        );
        RequestQueue q = Volley.newRequestQueue(getContext());
        q.add(req);
    }

    void loadSpinnerEventListeners() {
        mFilmsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedFilmId = mSpinnerAdapter.getItem(position).getId();
                mSelectedFilmTitle = mSpinnerAdapter.getItem(position).getTitle();
                Log.d("actorFilmSpinner", "Spinner changed, selected ID: " + mSelectedFilmId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    void submit() {
        JSONObject params = new JSONObject();
        try {
            params.put("actor_id", mActorFilm.getActorId());
            params.put("film_id", mSelectedFilmId);
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
                            mActorFilm.setActorId(filmActorInfo.getInt("actor_id"));
                            mActorFilm.setFilmId(filmActorInfo.getInt("film_id"));
                            mActorFilm.setCharacterName(filmActorInfo.getString("character"));
                            mActorFilm.setFilmName(mSelectedFilmTitle);

                            String entityStatus = response.getString("action");
                            boolean isNewEntity = false;
                            if (entityStatus.equals("add")) {
                                isNewEntity = true;
                            }
                            if(isNewEntity) {
                                // add to parent adapter list
                                mParentFilmographyAdapter.addFilmographyEntry(mActorFilm);
                            } else {
                                mParentFilmographyAdapter.searchAndUpdateFilmographyEntry(mActorFilm);
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