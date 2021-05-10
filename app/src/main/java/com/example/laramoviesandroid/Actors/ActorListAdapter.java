package com.example.laramoviesandroid.Actors;

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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.laramoviesandroid.MainActivity;
import com.example.laramoviesandroid.R;
import com.example.laramoviesandroid.authentication.AuthenticatedJSONObjectRequest;
import com.example.laramoviesandroid.models.Actor;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;

// find the viewholder (item detail view here
public class ActorListAdapter extends RecyclerView.Adapter<ActorListAdapter.ActorListAdapterViewHolder> {
    private ArrayList<Actor> mActors;
    private FragmentManager parentActivityFragmentManager;

    @NonNull
    @Override
    public ActorListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.actor_list_item, parent, false);

        return new ActorListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActorListAdapterViewHolder holder, int position) {
        Actor currentActor = mActors.get(position);
        holder.selectedActor = currentActor;
//        bind to recyclerview item view
        holder.tvName.setText(currentActor.getName());
        holder.mAdapterPosition = position;
        Picasso.get()
                .load(currentActor.getPortraitUrl())
                .into(holder.mivPortrait);
    }

    @Override
    public int getItemCount() {
        return mActors.size();
    }

    public class ActorListAdapterViewHolder extends  RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener{
        private  ImageView mivPortrait;
        private TextView tvName;
        public Actor selectedActor;
        public int mAdapterPosition;
        public ActorListAdapterViewHolder(View view) {

            super(view);
            mivPortrait = (ImageView) view.findViewById(R.id.image_actor_list_item_portrait);
            tvName = (TextView) view.findViewById(R.id.text_actor_list_item_name);

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }
        @Override
        public void onClick(View v) {
            ActorViewDialogFragment actorView = new ActorViewDialogFragment();
            actorView.setData(selectedActor);
            actorView.show(ActorListAdapter.this.parentActivityFragmentManager, "");
        }


        @Override
        public boolean onLongClick(View v) {
            Log.d(null, "Long pressed an item in actor list");
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle(R.string.actions_title)
                    .setItems(R.array.item_actions, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("actorlistlongpressaction", "Pressed which: " + Integer.toString(which));
                            // The 'which' argument contains the index position
                            // of the selected item
                            switch (which) {
                                case 0:
                                    MainActivity rootActivity = (MainActivity) v.getContext();
                                    rootActivity.launchNewFragment(
                                            ActorEditFragment.newInstance(
                                                    selectedActor,
                                                    ActorListAdapter.this,
                                                    getAdapterPosition())
                                        ,true
                                    );
                                    break;
                                case 1:
                                    // delete button
                                    deleteRequest(selectedActor, getAdapterPosition(), v.getContext());
                                    break;
                            }
                        }
                    }).show();
            return true;
        }
    }

    public ActorListAdapter(ArrayList<Actor> dataSet, FragmentManager parentActivityFragmentManager) {
        mActors = dataSet;
        this.parentActivityFragmentManager = parentActivityFragmentManager;
    }

    /**
     * edit and delete actor requests here
     */

    private void deleteRequest(Actor actor, int position, Context context) {
        JSONObject params = new JSONObject();

        try {
            params.put("_method", "DELETE");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AuthenticatedJSONObjectRequest deleteRequest = new AuthenticatedJSONObjectRequest(
                Request.Method.GET,
                context.getResources().getString(R.string.api_url) + "actors/" + Integer.toString(actor.getId())+"/delete",
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // delete successful
                        try {
                            Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mActors.remove(position);
                        ActorListAdapter.this.notifyItemRemoved(position);

                    }
                }
        );
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(deleteRequest);
    }
}
