package films;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.laramoviesandroid.R;
import com.example.laramoviesandroid.Singletons.GlobalMembers;
import com.example.laramoviesandroid.models.Film;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import films.FilmListAdapter;

public class FilmListFragment extends Fragment {
    protected RecyclerView mRecyclerView;
    private Context mContext;
    private ArrayList<Film> mFilms;
    private FilmListAdapter mFilmAdapter;
    private FragmentManager mParentFragmentManager;

    public FilmListFragment(FragmentManager fm) {
        super(R.layout.fragment_film_list);
        this.mParentFragmentManager = fm;
        Log.i(null, "Film List Fragment created");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_film_list, container, false);
        this.mFilms = new ArrayList<Film>();
//        this.sampleFilms();
        this.setMemberVariables(v);
        this.getFilmsAndRender();
        getActivity().setTitle(R.string.film_list_title);
        return v;

    }

    protected void getFilmsAndRender(){
        String fullRequestURI = getString(R.string.api_url) + "films";
        URL requestURLObject;
        try {
            requestURLObject= new URL(fullRequestURI);
        } catch (MalformedURLException e) {
            e.printStackTrace();

        }
        Log.i("getfilmsuri", "request URI: "+fullRequestURI);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                fullRequestURI,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        retrieval successful
                        Log.d("listquery", response.toString());
                        try {
                            JSONArray filmsJSON = response.getJSONArray("films");

                            for(int i = 0; i < filmsJSON.length(); i++) {
                                JSONObject currentFilm = filmsJSON.getJSONObject(i);
                                Log.i("current_film", "current film: " + currentFilm.toString());
//                                parse genre first, to handle null values;
                                String genre;
                                int genreId;
                                try {
                                    genre = currentFilm.getJSONObject("genre").getString("genre");
                                } catch (JSONException e) {
                                    genre = "None";
                                }
                                Log.i(null, "genre id:" + Integer.toString(currentFilm.getInt("genre_id")));

                                Film newFilm = Film.builder(currentFilm.getInt("id"))
                                        .setTitle(currentFilm.getString("film_title"))
                                        .setDuration(currentFilm.getInt("duration"))
                                        .setGenre(currentFilm.getInt("genre_id"))
                                        .setGenre(genre)
                                        .setReleaseDate(
                                                new SimpleDateFormat("yyyy-MM-dd")
                                                        .parse(currentFilm.getString("release_date"))
                                        )
                                        .setPosterURL(currentFilm.getString("poster"))
                                        .setAdditionalInfo(currentFilm.getString("additional_info"))
                                        .setStory(currentFilm.getString("story"))
                                        .build();

                                newFilm.setJson(currentFilm);
                                mFilms.add(newFilm);
                                mFilmAdapter.notifyDataSetChanged();

                                Log.i (null, "new film title:"+ newFilm.getTitle());
                            }

                        } catch (JSONException | ParseException e ) {
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
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String accessToken = GlobalMembers.getInstance().accessToken;
                params.put("Authorization", "Bearer " + accessToken);
                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(mContext);
        requestQueue.add(request);
    }
    protected void setMemberVariables(View v){
        mContext = getContext();
        mRecyclerView = (RecyclerView) v.findViewById(R.id.rv_film_list);
        mFilmAdapter = new FilmListAdapter(mFilms, mParentFragmentManager);

        // set recycler view configuration
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        mRecyclerView.setAdapter(mFilmAdapter);

    }

}
