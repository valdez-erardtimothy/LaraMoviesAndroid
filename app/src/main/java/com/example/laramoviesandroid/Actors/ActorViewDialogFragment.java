package com.example.laramoviesandroid.Actors;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.laramoviesandroid.R;
import com.example.laramoviesandroid.Singletons.GlobalMembers;
import com.example.laramoviesandroid.models.Actor;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * find read related methods at the ViewDialogFragments
 */
public class ActorViewDialogFragment extends DialogFragment {
    private Actor mActor;
    ActorFilmographyAdapter mActorFilmAdapter;
    RecyclerView mRvFilmography;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        // the layout and layout objects for the actor details
        View view = inflater.inflate(R.layout.fragment_actor_view_dialog, null);
        TextView tvName = (TextView) view.findViewById(R.id.text_actor_view_name);
        TextView tvNotes = (TextView) view.findViewById(R.id.text_actor_notes);
        ImageView ivPortrait = (ImageView) view.findViewById(R.id.image_actor_portrait);

        mRvFilmography = (RecyclerView) view.findViewById(R.id.rv_actor_view_filmography);
        setUpFilmographyAdapter();
        retrieveDetailAndBuildFilmography(mActor.getId());

        tvName.setText(mActor.getName());
        tvNotes.setText(mActor.getNotes());
        Picasso.get().load(mActor.getPortraitUrl()).into(ivPortrait);

        builder.setView(view)
                .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActorViewDialogFragment.this.getDialog().cancel();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    /**
     * set the actor info from recyclerview,
     * the list recyclerview's adapter,
     * and the actor's position from recyclerview adapter
     * @param actor the actor model retrieved from recyclerview
     *
     */
    public void setData(Actor actor) {
        this.mActor = actor;
    }

    void retrieveDetailAndBuildFilmography(int actor_id) {
        JSONObject requestArgs = null;
        try {
            requestArgs = new JSONObject().put("film", actor_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET,
                getResources().getString(R.string.api_url)+"actors/"+Integer.toString(actor_id),
                requestArgs,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject actorDetails = new JSONObject(String.valueOf(response.getJSONObject("actor")));
                            // only build filmography here so it only gets built when needed
                            mActor.buildFilmography(actorDetails);
                            mActorFilmAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String accessToken = GlobalMembers.getInstance().accessToken;
                params.put("Authorization", "Bearer " + accessToken);
                return params;

            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(req);

    }

    void setUpFilmographyAdapter() {
        mActorFilmAdapter = new ActorFilmographyAdapter(mActor.getFilmography());
        mRvFilmography.setHasFixedSize(true);
        mRvFilmography.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvFilmography.setAdapter(mActorFilmAdapter);
    }

}