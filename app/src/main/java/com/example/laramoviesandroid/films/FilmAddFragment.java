package com.example.laramoviesandroid.films;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.laramoviesandroid.Actors.ActorEditFragment;
import com.example.laramoviesandroid.R;
import com.example.laramoviesandroid.authentication.AuthenticatedJSONObjectRequest;
import com.example.laramoviesandroid.models.Film;
import com.example.laramoviesandroid.models.Genre;
import com.example.laramoviesandroid.utilities.ImageUtilities;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FilmAddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilmAddFragment extends Fragment {
    EditText mEditTitle, mEditDuration, mEditReleaseDate, mEditStory, mEditAdditionalInfo;
    Spinner spinnerGenre;
    Button mBtnSubmit, mBtnCancel, mBtnChooseImage;
    ArrayList<Genre> mGenres;
    FilmGenreSpinnerAdapter mGenreSpinnerAdapter;
    int genreSpinnerSelectedId;
    ImageView mIvPoster;


    public FilmAddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FilmAddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FilmAddFragment newInstance() {
        FilmAddFragment fragment = new FilmAddFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mGenres = new ArrayList<Genre>();
        View view = inflater.inflate(R.layout.fragment_film_add, container, false);
        getActivity().setTitle(R.string.film_add_title);
        mEditTitle = view.findViewById(R.id.edit_film_add_title);
        mEditDuration = view.findViewById(R.id.edit_film_add_duration);
        mEditReleaseDate = view.findViewById(R.id.edit_film_add_release_date);
        mEditStory = view.findViewById(R.id.edit_film_add_story);
        mEditAdditionalInfo = view.findViewById(R.id.edit_film_add_additional_info);
        spinnerGenre = (Spinner)view.findViewById(R.id.spinner_film_add_genre);
        mBtnSubmit = view.findViewById(R.id.button_film_add_submit);
        mBtnCancel = view.findViewById(R.id.button_film_add_cancel);
        mBtnChooseImage = view.findViewById(R.id.button_film_add_choose_image);
        mIvPoster = view.findViewById(R.id.image_film_add_poster);
        loadGenreSpinner();
        setButtonEventListeners();
        return view;
    }

    void setButtonEventListeners() {
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        mBtnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImageFromGallery();
            }
        });
    }
    void chooseImageFromGallery() {
        Intent chooseImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(chooseImageIntent, ActorEditFragment.GET_PORTRAIT_EDIT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(null, "Image received on Film Edit");
        if(requestCode == ActorEditFragment.GET_PORTRAIT_EDIT && resultCode == RESULT_OK && data != null) {
            Toast.makeText(getContext(), "Image Selected!", Toast.LENGTH_SHORT).show();
            Uri selectedImage = data.getData();

            Picasso.get().load(selectedImage).into(mIvPoster);
        }
    }


    void submit() {
        JSONObject requestParams = new JSONObject();
        mIvPoster.buildDrawingCache();
        Bitmap posterBmp = mIvPoster.getDrawingCache();
        try {
            requestParams.put("film_title", mEditTitle.getText());
            requestParams.put("duration", mEditDuration.getText());
            requestParams.put("release_date", mEditReleaseDate.getText());
            requestParams.put("story", mEditStory.getText());
            requestParams.put("additional_info", mEditAdditionalInfo.getText());
            requestParams.put("genre_id", genreSpinnerSelectedId);
            requestParams.put("poster_base64", ImageUtilities.bmpToBase64(posterBmp));
            requestParams.put("id", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(null, "Film upload request params: " +requestParams.toString());
        AuthenticatedJSONObjectRequest req = new AuthenticatedJSONObjectRequest(
                Request.Method.POST,
                getResources().getString(R.string.api_url) + "films/submit",
                requestParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d(null, "add film response: "+response.getString("message"));
                            Toast.makeText(getContext(), response.getString("message"), Toast.LENGTH_SHORT).show();

                            getActivity().onBackPressed();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        RequestQueue q = Volley.newRequestQueue(getContext());
        q.add(req);
    }

    void cancel() {
        getActivity().onBackPressed();
    }

    void loadGenreSpinner() {
        mGenreSpinnerAdapter = new FilmGenreSpinnerAdapter(getContext(),
                R.layout.spinner_textview,
                mGenres);
        AuthenticatedJSONObjectRequest req = new AuthenticatedJSONObjectRequest(
                Request.Method.GET,
                getResources().getString(R.string.api_url) + "genres",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject serverResponse) {
                        try {
                            int actor_film_iterator = 0;
                            int position = 0;
                            JSONObject response = new JSONObject(String.valueOf(serverResponse));
                            JSONArray genres = response.getJSONArray("genre");
                            for(int i = 0; i < genres.length(); i++) {
                                JSONObject genre = new JSONObject(String.valueOf(genres.getJSONObject(i)));

                                Genre newGenre = new Genre()
                                        .setId(genre.getInt("id"))
                                        .setGenre(genre.getString("genre"));

                                mGenres.add(newGenre);
                            }
                            spinnerGenre.setAdapter(mGenreSpinnerAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
        );
        RequestQueue q = Volley.newRequestQueue(getContext());
        q.add(req);
        loadGenreSpinnerListeners();
    }
    void loadGenreSpinnerListeners() {
        spinnerGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                genreSpinnerSelectedId = mGenreSpinnerAdapter.getItem(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}