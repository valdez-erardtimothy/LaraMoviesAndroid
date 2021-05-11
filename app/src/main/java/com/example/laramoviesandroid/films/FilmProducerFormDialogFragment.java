package com.example.laramoviesandroid.films;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.laramoviesandroid.R;
import com.example.laramoviesandroid.authentication.AuthenticatedJSONObjectRequest;
import com.example.laramoviesandroid.models.Actor;
import com.example.laramoviesandroid.models.FilmProducer;
import com.example.laramoviesandroid.models.Producer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Use the {@link FilmProducerFormDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilmProducerFormDialogFragment extends DialogFragment {
    FilmProducer mFilmProducer;
    ArrayList<Producer> mProducers;
    Spinner mProducersSpinner;
    FilmActorFormProducersSpinnerAdapter spinnerAdapter;
    int mSelectedProducerId;
    String mSelectedProducerName;
    FilmProducersListAdapter parentFilmProducerAdapter;

    public FilmProducerFormDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FilmProducerFormDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FilmProducerFormDialogFragment newInstance(FilmProducer filmProducer, FilmProducersListAdapter parentFilmProducerAdapter) {
        FilmProducerFormDialogFragment fragment = new FilmProducerFormDialogFragment();
        fragment.mFilmProducer = filmProducer;
        fragment.parentFilmProducerAdapter = parentFilmProducerAdapter;
        fragment.mProducers = new ArrayList<Producer>();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_film_producer_form_dialog, null);
        mProducersSpinner = view.findViewById(R.id.spinner_film_producer_form_producers);
        spinnerAdapter = new FilmActorFormProducersSpinnerAdapter(
                getContext(),
                R.layout.spinner_textview,
                mProducers
        );
        loadSpinnerData();
        loadSpinnerEventListeners();
        builder.setView(view)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FilmProducerFormDialogFragment.this.getDialog().cancel();
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
                getResources().getString(R.string.api_url) + "films/"+mFilmProducer.getFilmId()+"/non-producers",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject serverResponse) {
                        try {
                            JSONObject response = new JSONObject(String.valueOf(serverResponse));
                            JSONArray films = response.getJSONArray("producers");
                            for(int i = 0; i < films.length(); i++) {
                                JSONObject producer = new JSONObject(String.valueOf(films.getJSONObject(i)));



                                mProducers.add(Producer.buildInstanceFromJSON(producer));
                            }
                            mProducersSpinner.setAdapter(spinnerAdapter);
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
        mProducersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedProducerId = spinnerAdapter.getItem(position).getId();
                mSelectedProducerName = spinnerAdapter.getItem(position).getName();
                Log.d("FilmProducerSpinner", "Spinner changed, selected ID: " + mSelectedProducerId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    void submit() {
        JSONObject params = new JSONObject();
        try {
            params.put("producer_id", mSelectedProducerId);
            params.put("film_id", mFilmProducer.getFilmId());
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        Log.d(null, "Submitted parameters: " + params.toString());

        AuthenticatedJSONObjectRequest req = new AuthenticatedJSONObjectRequest(
                Request.Method.POST,
                getResources().getString(R.string.api_url) + "films/submit-film-producer",
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            response = new JSONObject(String.valueOf(response));
                            Toast.makeText(getParentFragment().getContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                            JSONObject filmProducerInfo = response.getJSONObject("producer");
                            mFilmProducer.setProducerId(filmProducerInfo.getInt("id"));;
                            mFilmProducer.setProducerName(mSelectedProducerName);

                            Boolean didInsert = response.getBoolean("success");
                            if(didInsert) {
                                parentFilmProducerAdapter.addFilmProducer(mFilmProducer);
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