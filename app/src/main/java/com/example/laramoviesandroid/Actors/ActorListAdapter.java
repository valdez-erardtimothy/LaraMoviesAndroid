package com.example.laramoviesandroid.Actors;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laramoviesandroid.FilmListAdapter;
import com.example.laramoviesandroid.R;
import com.example.laramoviesandroid.models.Actor;
import com.example.laramoviesandroid.models.Film;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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
        Picasso.get()
                .load(currentActor.getPortraitUrl())
                .into(holder.mivPortrait);
    }

    @Override
    public int getItemCount() {
        return mActors.size();
    }

    public class ActorListAdapterViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        private  ImageView mivPortrait;
        private TextView tvName;
        public Actor selectedActor;
        public ActorListAdapterViewHolder(View view) {

            super(view);
            mivPortrait = (ImageView) view.findViewById(R.id.image_actor_list_item_portrait);
            tvName = (TextView) view.findViewById(R.id.text_actor_list_item_name);

            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            ActorViewDialogFragment actorView = new ActorViewDialogFragment();
            actorView.setActor(selectedActor);
            actorView.show(ActorListAdapter.this.parentActivityFragmentManager, "");
        }
    }

    public ActorListAdapter(ArrayList<Actor> dataSet, FragmentManager parentActivityFragmentManager) {
        mActors = dataSet;
        this.parentActivityFragmentManager = parentActivityFragmentManager;
    }
}
