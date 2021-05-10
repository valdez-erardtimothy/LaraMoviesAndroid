package com.example.laramoviesandroid.producers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.laramoviesandroid.MainActivity;
import com.example.laramoviesandroid.R;
import com.example.laramoviesandroid.authentication.AuthenticatedJSONObjectRequest;
import com.example.laramoviesandroid.models.Producer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProducerListAdapter extends RecyclerView.Adapter<ProducerListAdapter.ProducerListItemViewHolder> {
    ArrayList<Producer> mProducers;
    FragmentManager mParentActivityManager;
    MainActivity mainActivity;
    Context mContext;

    public ProducerListAdapter(FragmentManager parentActivityManager, Context context, MainActivity activity) {
        this.mParentActivityManager = parentActivityManager;
        this.mContext = context;
        mainActivity = activity;
        mProducers = new ArrayList<Producer>();
        retrieveProducersAndPopulateRV();
    }

    @NonNull
    @Override
    public ProducerListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.producer_list_item, parent, false);
        return new ProducerListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProducerListItemViewHolder holder, int position) {
        Producer boundProducer = mProducers.get(position);
        holder.mTvName.setText(boundProducer.getName());
        holder.mTvEmail.setText(boundProducer.getEmail());
        holder.mTvWebsite.setText(boundProducer.getWebsite());
    }

    @Override
    public int getItemCount() {
        return mProducers.size();
    }

    public class ProducerListItemViewHolder extends RecyclerView.ViewHolder implements
        View.OnLongClickListener{
        private Producer selectedProducer;
        private TextView mTvName, mTvEmail, mTvWebsite;

        public ProducerListItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvName = itemView.findViewById(R.id.text_producer_list_item_name);
            mTvWebsite = itemView.findViewById(R.id.text_producer_list_item_website);
            mTvEmail = itemView.findViewById(R.id.text_producer_list_item_email);
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }


    void retrieveProducersAndPopulateRV() {
        AuthenticatedJSONObjectRequest producerListRequest = new AuthenticatedJSONObjectRequest(
                Request.Method.GET,
                mContext.getResources().getString(R.string.api_url) + "producers",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray producers = response.getJSONArray("producers");
                            for(int i = 0; i < producers.length(); i++) {
                                mProducers.add(Producer.buildInstanceFromJSON(producers.getJSONObject(i)));
                            }
                            ProducerListAdapter.this.notifyDataSetChanged();
                        } catch (JSONException exception) {
                            exception.printStackTrace();
                        }
                    }
                }
        );
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(producerListRequest);
    }

    /**
     *
     * crud-related methods (these methods are for handling the display on recyclerview)
     *
     */


    public void addProducer(Producer toAdd) {
        mProducers.add(toAdd);
        this.notifyItemInserted(getItemCount()-1);
    }
}
