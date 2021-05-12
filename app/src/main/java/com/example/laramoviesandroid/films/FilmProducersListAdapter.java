package com.example.laramoviesandroid.films;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.laramoviesandroid.R;
import com.example.laramoviesandroid.authentication.AuthenticatedJSONObjectRequest;
import com.example.laramoviesandroid.models.FilmProducer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FilmProducersListAdapter extends RecyclerView.Adapter<FilmProducersListAdapter.FilmProducersListViewHolder> {
    private ArrayList<FilmProducer> mFilmProducers;
    private boolean mHoldersEditable;

    //constructor
    public FilmProducersListAdapter(ArrayList<FilmProducer> filmProducers, boolean editable) {
        mFilmProducers = filmProducers;
        mHoldersEditable = editable;
    }

    @NonNull
    @Override
    public FilmProducersListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.film_producers_item, parent, false);
        return new FilmProducersListViewHolder(view, mHoldersEditable);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmProducersListViewHolder holder, int position) {
        holder.boundProducer = mFilmProducers.get(position);
        holder.mTvProducerName.setText(holder.boundProducer.getProducerName());
    }

    @Override
    public int getItemCount() {
        return mFilmProducers.size();
    }

    public class FilmProducersListViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private TextView mTvProducerName;
        private FilmProducer boundProducer;
        private boolean mHolderEditable;
        public FilmProducersListViewHolder(@NonNull View itemView, boolean editable) {
            super(itemView);
            mTvProducerName = itemView.findViewById(R.id.text_film_producers_item_producer_name);
            this.mHolderEditable = editable;
            if(mHolderEditable){
                itemView.setOnLongClickListener(this);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Remove this Producer from Film?")
                    .setMessage("Are eyou sure you want to detach this producer?")
                    .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            removeFilmProducer(getAdapterPosition());
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .show();
            return false;
        }
    }

    void deleteFilmProducerRequest(int position, FilmProducer filmProducer, Context context) {
        AuthenticatedJSONObjectRequest req = new AuthenticatedJSONObjectRequest(
                Request.Method.GET,
                context.getResources().getString(R.string.api_url)
                        + "films/"
                        + filmProducer.getFilmId()
                        + "/detach-producer/"
                        + filmProducer.getProducerId(),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        removeFilmProducer(position);
                    }
                }

        );
        Volley.newRequestQueue(context).add(req);
    }

    // crud for adapter recycler

    void addFilmProducer(FilmProducer filmProducer) {
        mFilmProducers.add(filmProducer);
        this.notifyItemInserted(getItemCount()-1);

    }

    void removeFilmProducer(int position) {
        mFilmProducers.remove(position);
        this.notifyItemRemoved(position);

    }
}
