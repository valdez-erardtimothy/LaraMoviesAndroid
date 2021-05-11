package com.example.laramoviesandroid.films;

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
import com.example.laramoviesandroid.Actors.ActorFilmFormDialogFragment;
import com.example.laramoviesandroid.Actors.ActorFilmographyAdapter;
import com.example.laramoviesandroid.R;
import com.example.laramoviesandroid.authentication.AuthenticatedJSONObjectRequest;
import com.example.laramoviesandroid.models.FilmActor;

import org.json.JSONObject;

import java.util.ArrayList;

public class FilmActorsListAdapter extends  RecyclerView.Adapter<FilmActorsListAdapter.FilmActorsViewHolder>{
    private ArrayList<FilmActor> mFilmActors;
    private boolean mHoldersEditable;
    private FragmentManager fm;

     public FilmActorsListAdapter(ArrayList<FilmActor> dataSet, boolean editable) {
         mFilmActors = dataSet;
         mHoldersEditable = editable;

     }
    public FilmActorsListAdapter(ArrayList<FilmActor> dataSet, boolean editable, FragmentManager fm) {
        this(dataSet,editable);
        this.fm = fm;
    }
    @NonNull
    @Override
    public FilmActorsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view = LayoutInflater.from(parent.getContext())
                 .inflate(R.layout.film_actors_item, parent, false);
         return new FilmActorsViewHolder(view, mHoldersEditable);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmActorsViewHolder holder, int position) {
         FilmActor currentFilmActor = mFilmActors.get(position);
         holder.boundFilmActor = currentFilmActor;
         holder.mTvActorName.setText(currentFilmActor.getActorName());
         holder.mTvCharacter.setText(currentFilmActor.getCharacterName());
    }

    @Override
    public int getItemCount() {
        return mFilmActors.size();
    }

    public class FilmActorsViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private boolean mHolderEditable;
        private TextView mTvActorName, mTvCharacter;
        private FilmActor boundFilmActor;
         public FilmActorsViewHolder(@NonNull View itemView, boolean editable) {
            super(itemView);
            mHolderEditable = editable;
            mTvActorName = itemView.findViewById(R.id.text_film_actors_item_actor_name);
            mTvCharacter = itemView.findViewById(R.id.text_film_actors_item_character_name);
            if(mHolderEditable) {
                itemView.setOnLongClickListener(this);

            }
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
                                    FilmActorFormDialogFragment.newInstance(boundFilmActor, FilmActorsListAdapter.this)
                                            .show(fm, null);
                                    break;
                                case 1:
                                    // delete button
//                                    deleteRequest(boundProducer, getAdapterPosition(), v.getContext());
                                    deleteFilmographyRequest(getAdapterPosition(), boundFilmActor, v.getContext());
                                    break;
                            }
                        }
                    }).show();
            return false;
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


    // crud for adapter recycler only
    void removeFilmographyEntry(int position) {
        mFilmActors.remove(position);
        this.notifyItemRemoved(position);
    }

    public void addFilmographyEntry(FilmActor filmographyEntry) {
        mFilmActors.add(filmographyEntry);
        this.notifyItemInserted(getItemCount()-1);
    }
    /**
     * replaces the filmography entry with
     * @param newFilmographyDetails - the FilmActor to replace the old one with.
     */
    void searchAndUpdateFilmographyEntry(FilmActor newFilmographyDetails) {
        int position = 0;
        boolean found =false;
        for(FilmActor entry : mFilmActors) {
            if(newFilmographyDetails.getActorId() == entry.getActorId()) {

                found=true;
                break;
            }
            position++;
        }
        if(found) {
            mFilmActors.set(position, newFilmographyDetails);
            this.notifyItemChanged(position);
        }
    }
}
