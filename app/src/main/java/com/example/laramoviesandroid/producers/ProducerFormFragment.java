package com.example.laramoviesandroid.producers;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.laramoviesandroid.R;
import com.example.laramoviesandroid.authentication.AuthenticatedJSONObjectRequest;
import com.example.laramoviesandroid.models.Producer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProducerFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProducerFormFragment extends Fragment {
    Producer mProducerToEdit;
    ProducerListAdapter mParentListAdapter;
    int adapterPosition;
    boolean mEditMode; // true for edit, false for add
    private EditText mEditName, mEditWebsite, mEditEmail;
    Button mBtnSubmit, mBtnCancel;

    public ProducerFormFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param producer  the instance of producere to edit. pass a new instance for a new producer.
     * @param listAdapter the adaptere at ProducerListFragment
     * @return A new instance of fragment ProducerFormFragment.
     */
    public static ProducerFormFragment newInstance(Producer producer,  ProducerListAdapter listAdapter, int position) {
        ProducerFormFragment fragment = new ProducerFormFragment();
        fragment.mParentListAdapter = listAdapter;
        fragment.mProducerToEdit = producer;
        fragment.adapterPosition = position;
        fragment.mEditMode = true;

        return fragment;
    }

    public static ProducerFormFragment newInstance(Producer producer, ProducerListAdapter producerListAdapter) {
        // the 0 passed is just a filler, it won't be used when editmode is false
        ProducerFormFragment fragment = newInstance(producer, producerListAdapter, 0);
        fragment.mEditMode = false;
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
        View v = inflater.inflate(R.layout.fragment_producer_form, container, false);
        initializeViews(v);
        return v;
    }

    void initializeViews(View v) {
        mEditName = v.findViewById(R.id.edit_producer_form_name);
        mEditEmail = v.findViewById(R.id.edit_producer_form_email);
        mEditWebsite = v.findViewById(R.id.edit_producer_form_website);
        getActivity().setTitle(!mEditMode?R.string.producer_add_title:R.string.producer_edit_title);
        mBtnCancel = v.findViewById(R.id.button_producer_form_cancel);
        mBtnSubmit = v.findViewById(R.id.button_producer_form_submit);
        setButtonListeners();
        retrieveProducerContent();
    }

    void setButtonListeners() {
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }


    /**
     * if view wil be needed
     */
    void retrieveProducerContent() {
        if(mEditMode) {
            setEditViewContent();
        }
    }

    void setEditViewContent() {
        if(mEditMode) {
            mEditName.setText(mProducerToEdit.getName());
            mEditEmail.setText(mProducerToEdit.getEmail());
            mEditWebsite.setText(mProducerToEdit.getWebsite());
        }
    }

    void submit() {
        JSONObject requestParams = new JSONObject();
        try {
            requestParams.put("producer_fullname", mEditName.getText());
            requestParams.put("website", mEditWebsite.getText());
            requestParams.put("email", mEditEmail.getText());
            requestParams.put("id", mProducerToEdit.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AuthenticatedJSONObjectRequest req = new AuthenticatedJSONObjectRequest(
                Request.Method.POST,
                getResources().getString(R.string.api_url) + "producers",
                requestParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject producerJson = new JSONObject(String.valueOf(response.getJSONObject("producer")));
                            Toast.makeText(getContext(), response.getString("message"), Toast.LENGTH_SHORT).show();

                            Producer.buildInstanceFromJSON(mProducerToEdit, producerJson);
                            if(mEditMode) {
                                mParentListAdapter.notifyItemChanged(adapterPosition);
                            } else
                            {
                                mParentListAdapter.addProducer(mProducerToEdit);
                            }
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
}