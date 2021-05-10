package com.example.laramoviesandroid;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.laramoviesandroid.Actors.ActorListFragment;
import com.example.laramoviesandroid.Singletons.GlobalMembers;
import com.example.laramoviesandroid.producers.ProducerListFragment;

import films.FilmListFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainMenuFragment extends Fragment {
    Button mButtonFilms;
    Button mButtonActors;
    Button mButtonProducers;
    Button mButtonLogout;
    MainActivity mParentActivity;


    public MainMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParentActivity = (MainActivity) getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);
        initializeMembers(view);
        getActivity().setTitle(R.string.main_menu_title);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    void initializeMembers(View view) {
        mButtonFilms = (Button) view.findViewById(R.id.btn_main_films);
        mButtonActors = (Button) view.findViewById(R.id.btn_main_actors);
        mButtonProducers = (Button) view.findViewById(R.id.btn_main_producers);
        mButtonLogout = (Button) view.findViewById(R.id.btn_main_logout);

        this.initializeButtonListeners();
    }

    void initializeButtonListeners() {
        mButtonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(null, "Logout called, current token:" + GlobalMembers.getInstance().accessToken);
                mParentActivity.logout();
            }
        });
        mButtonFilms.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mParentActivity.launchNewFragment(new FilmListFragment(mParentActivity.getSupportFragmentManager()), true);
            }
        });
        mButtonActors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mParentActivity.launchNewFragment(new ActorListFragment(), true);
            }
        });
        mButtonProducers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mParentActivity.launchNewFragment(ProducerListFragment.newInstance(), true);
            }
        });
    }
}