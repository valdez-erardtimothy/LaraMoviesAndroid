package com.example.laramoviesandroid.Actors;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActorListFragment extends Fragment {
    private ActorListAdapter mActorAdapter;
    private RecyclerView mActorRecyclerView;
    ArrayList<Actor> mActors;

    public ActorListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView =  inflater.inflate(R.layout.fragment_actor_list, container, false);

        initializeMembers(fragmentView);
        findAndRenderActors();
        return fragmentView;
    }

    public void initializeMembers(View v) {
        mActorRecyclerView = (RecyclerView) v.findViewById(R.id.rv_actor_list);
        mActors = new ArrayList<Actor>();
        mActorAdapter = new ActorListAdapter(mActors, getActivity().getSupportFragmentManager());
        mActorRecyclerView.setHasFixedSize(true);
        mActorRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mActorRecyclerView.setAdapter(mActorAdapter);
    }

    public void findAndRenderActors() {
        String url = getResources().getString(R.string.api_url)+"actors";
        URL requestURLObject;
        try {
            requestURLObject= new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();

        }

        JsonObjectRequest request = new JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray actorsJSON = response.getJSONArray("actors");
                        for (int i = 0; i < actorsJSON.length(); i++) {
                            JSONObject currentActor = actorsJSON.getJSONObject(i);
                            Log.i(null, "actor acquired: " + currentActor);
//                            mActors.add(new Actor()
//                                    .setId(currentActor.getInt("id"))
//                                    .setName(currentActor.getString("actor_fullname"))
//                                    .setPortraitUrl(currentActor.getString("portrait"))
//                                    .setNotes(currentActor.has("actor_notes")
//                                            ?currentActor.getString("actor_notes")
//                                            :"None"
//                                    )
//                            );
                            mActors.add(Actor.buildFromJSON(currentActor));
                            mActorAdapter.notifyDataSetChanged();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("volleyError", error.toString());
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

        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }
}