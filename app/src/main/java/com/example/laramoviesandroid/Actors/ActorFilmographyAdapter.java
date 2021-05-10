package com.example.laramoviesandroid.Actors;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.laramoviesandroid.R;
import com.example.laramoviesandroid.authentication.AuthenticatedJSONObjectRequest;
import com.example.laramoviesandroid.models.FilmActor;
import com.example.laramoviesandroid.producers.ProducerFormFragment;
import com.example.laramoviesandroid.producers.ProducerListAdapter;

import org.json.JSONObject;

import java.util.ArrayList;

public class ActorFilmographyAdapter extends RecyclerView.Adapter<ActorFilmographyAdapter.ActorFilmographyViewHolder> {
    private ArrayList<FilmActor>mActorFilmography;
    private boolean mContentsEditable;
    FragmentManager fm; //will be null when not editable
    @NonNull
    @Override
    public ActorFilmographyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.actor_filmography_item, parent, false);
        return new ActorFilmographyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActorFilmographyViewHolder holder, int position) {
        holder.selectedActorFilm = mActorFilmography.get(position);
        holder.tvCharacterName.setText(holder.selectedActorFilm.getCharacterName());
        holder.tvFilmTitle.setText(holder.selectedActorFilm.getFilmName());
    }

    @Override
    public int getItemCount() {
        return mActorFilmography.size();
    }

    /**
     * the view for actor filmography items.
     * set all the filmography item interaction here.
     */
    public class ActorFilmographyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
        private TextView tvCharacterName;
        private TextView tvFilmTitle;
        public FilmActor selectedActorFilm;
        public ActorFilmographyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCharacterName = (TextView) itemView.findViewById(R.id.text_actor_filmography_item_character);
            tvFilmTitle = (TextView) itemView.findViewById(R.id.text_actor_filmography_item_film_title);
            if(mContentsEditable) itemView.setOnLongClickListener(ActorFilmographyViewHolder.this);
        }

        @Override
        public boolean onLongClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle(R.string.actions_title)
                    .setItems(R.array.item_actions, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("actorlistlongpressaction", "Pressed which: " + Integer.toString(which));
                            // The 'which' argument contains the index position
                            // of the selected item
                            switch (which) {
                                case 0:
//                                    mainActivity.launchNewFragment(
//                                            ProducerFormFragment.newInstance(
//                                                    boundProducer,
//                                                    ProducerListAdapter.this,
//                                                    getAdapterPosition())
//                                            ,true
//                                    );
                                    ActorFilmFormDialogFragment.newInstance(selectedActorFilm, ActorFilmographyAdapter.this)
                                            .show(fm, null);
                                    break;
                                case 1:
                                    // delete button
//                                    deleteRequest(boundProducer, getAdapterPosition(), v.getContext());
                                    deleteFilmographyRequest(getAdapterPosition(), selectedActorFilm, v.getContext());
                                    break;
                            }
                        }
                    }).show();
            return true;
        }
    }

    void deleteFilmographyRequest(int position, FilmActor filmographyEntry, Context context) {
        AuthenticatedJSONObjectRequest req = new AuthenticatedJSONObjectRequest(
                Request.Method.GET,
                context.getResources().getString(R.string.api_url) + "films/" + filmographyEntry.getFilmId() + "/remove-film-actor/" + filmographyEntry.getActorId(),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        removeFilmographyEntry(position);
                    }
                }

        );
        Volley.newRequestQueue(context).add(req);
    }

    public ActorFilmographyAdapter(ArrayList<FilmActor> dataSet) {
        this(dataSet, false, null);
    }

    /**
     *
     * @param dataSet
     * @param editable true if in actor edit, false (or leave blank) if in actor view.
     */
    public ActorFilmographyAdapter(ArrayList<FilmActor> dataSet, boolean editable, FragmentManager fm) {
        this.mActorFilmography = dataSet;
        this.mContentsEditable = editable;
        this.fm = fm;
    }

    void removeFilmographyEntry(int position) {
        mActorFilmography.remove(position);
        this.notifyItemRemoved(position);
    }

    public void addFilmographyEntry(FilmActor filmographyEntry) {
        mActorFilmography.add(filmographyEntry);
        this.notifyItemInserted(getItemCount()-1);
    }

    /**
     * replaces the filmography entry with
     * @param newFilmographyDetails - the FilmActor to replace the old one with.
     */
    void searchAndUpdateFilmographyEntry(FilmActor newFilmographyDetails) {
        int position = 0;
        boolean found =false;
        for(FilmActor entry : mActorFilmography) {
            if(newFilmographyDetails.getFilmId() == entry.getFilmId()) {

                found=true;
                break;
            }
            position++;
        }
        if(found) {
            mActorFilmography.set(position, newFilmographyDetails);
            this.notifyItemChanged(position);
        }
    }


}
