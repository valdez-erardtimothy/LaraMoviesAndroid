package com.example.laramoviesandroid.producers;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.laramoviesandroid.MainActivity;
import com.example.laramoviesandroid.R;
import com.example.laramoviesandroid.models.Producer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProducerListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProducerListFragment extends Fragment {
    private ProducerListAdapter mProducersAdapter;
    private RecyclerView mRvProducers;
    private FloatingActionButton mFabAddProducerButton;
    private MainActivity mainActivity;
    public ProducerListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProducerListFragment.
     */
    public static ProducerListFragment newInstance() {
        ProducerListFragment fragment = new ProducerListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainActivity = (MainActivity)getActivity();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_producer_list, container, false);
        mProducersAdapter = new ProducerListAdapter(getFragmentManager(), getContext(), mainActivity);
        getActivity().setTitle(R.string.producer_list_title);
        mRvProducers = view.findViewById(R.id.rv_producer_list);
        mRvProducers.setHasFixedSize(true);
        mRvProducers.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvProducers.setAdapter(mProducersAdapter);

        mFabAddProducerButton = view.findViewById(R.id.fab_producer_list_add);
        mFabAddProducerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.launchNewFragment(
                        ProducerFormFragment.newInstance(
                                new Producer(), mProducersAdapter
                        ),
                        true
                );
            }
        });
        return view;
    }
}