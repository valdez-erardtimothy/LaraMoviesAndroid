package com.example.laramoviesandroid.Actors;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.laramoviesandroid.R;
import com.example.laramoviesandroid.authentication.AuthenticatedJSONObjectRequest;
import com.example.laramoviesandroid.models.Actor;
import com.example.laramoviesandroid.models.FilmActor;
import com.example.laramoviesandroid.utilities.ImageUtilities;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActorEditFragment extends Fragment {
    public static int GET_PORTRAIT_EDIT = 0xFFFF ;
    EditText mEditName;
    EditText mEditNotes;
    ImageView mIvPortrait;
    Button mBtnEdit;
    Button mBtnCancel;
    Button mBtnChooseImage;

    RecyclerView mRvActorFilmography;
    FloatingActionButton mFabAddActorFilm;

    Actor mActorToEdit;
    ActorFilmographyAdapter mFilmographyAdapter;


    boolean mImageChanged = false; // marks the image for upload

    public ActorEditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_actor_edit, container, false);
        mEditName = view.findViewById(R.id.input_actor_edit_name);
        mEditNotes = view.findViewById(R.id.input_actor_edit_notes);
        mBtnEdit = view.findViewById(R.id.button_actor_edit_submit);
        mFabAddActorFilm = view.findViewById(R.id.fab_actor_edit_add_film);
        mIvPortrait = view.findViewById(R.id.image_actor_edit_portrait);
        mBtnCancel = view.findViewById(R.id.button_actor_edit_cancel);
        mBtnChooseImage  = view.findViewById(R.id.button_actor_edit_choose_portrait);
        mRvActorFilmography = view.findViewById(R.id.rv_actor_edit_filmography);
        mRvActorFilmography.setLayoutManager(new LinearLayoutManager(getContext()));
//        mRvActorFilmography.setHasFixedSize(true);
        mFilmographyAdapter = new ActorFilmographyAdapter(mActorToEdit.getFilmography(), true, getChildFragmentManager());

        mRvActorFilmography.setAdapter(mFilmographyAdapter);
        getActivity().setTitle(R.string.actor_edit_title);
        this.setButtonListeners();
        this.setViewContents();
        return view;
    }


    void setViewContents() {
        mEditName.setText(mActorToEdit.getName());
        mEditNotes.setText(mActorToEdit.getNotes());
        Picasso.get().load(mActorToEdit.getPortraitUrl()).into(mIvPortrait);

        buildAndRenderFilmography();
    }

    void buildAndRenderFilmography() {
        AuthenticatedJSONObjectRequest req = new AuthenticatedJSONObjectRequest(
                Request.Method.GET,
                getResources().getString(R.string.api_url) + "actors/" + Integer.toString(mActorToEdit.getId()),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // reconstruct to ensure proper parsing
                            JSONObject responseActor = new JSONObject(String.valueOf(response.getJSONObject("actor")));
                            mActorToEdit.buildFilmography(responseActor);
                            mFilmographyAdapter.notifyDataSetChanged();
                        } catch(JSONException e)  {
                            e.printStackTrace();
                        }
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }

    void setButtonListeners(){
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        mBtnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(chooseImageIntent, ActorEditFragment.GET_PORTRAIT_EDIT);
            }
        });

        mBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitEdit();
            }
        });
        mFabAddActorFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActorFilmFormDialogFragment.newInstance(
                        new FilmActor().setActorId(mActorToEdit.getId())
                        .setActorName(mActorToEdit.getName())
                        .setFilmId(0),
                        mFilmographyAdapter
                ).show(getChildFragmentManager(),null);
            }
        });
    }

    /**
     * https://stackoverflow.com/questions/38352148/get-image-from-the-gallery-and-show-in-imageview
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(null, "Image received on Actor Edit");
        if(requestCode == ActorEditFragment.GET_PORTRAIT_EDIT && resultCode == RESULT_OK && data != null) {
            Toast.makeText(getContext(), "Image Selected!", Toast.LENGTH_SHORT).show();
            Uri selectedImage = data.getData();
            mImageChanged = true;
//            String[] filePathColumn = { MediaStore.Images.Media.DATA };
//            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
//                    filePathColumn, null, null, null);
//            cursor.moveToFirst();
//
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            String picturePath = cursor.getString(columnIndex);
//            cursor.close();

            Picasso.get().load(selectedImage).into(mIvPortrait);
        }
    }

    void submitEdit() {
        JSONObject requestParams = new JSONObject();

//        Bitmap portraitBmp = ((BitmapDrawable) mIvPortrait.getDrawable()).getBitmap();
        mIvPortrait.buildDrawingCache();
        Bitmap portraitBmp = mIvPortrait.getDrawingCache();
//        return;
        try {
            requestParams.put("actor_fullname", mEditName.getText());
            requestParams.put("actor_notes", mEditNotes.getText());
            if(mImageChanged == true) {
                requestParams.put("portrait_base64", ImageUtilities.bmpToBase64(portraitBmp));

            }
            requestParams.put("id", mActorToEdit.getId());
            Log.d(null, "edit actor request parameters: "+ requestParams.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AuthenticatedJSONObjectRequest request = new AuthenticatedJSONObjectRequest(
                Request.Method.POST,
                getResources().getString(R.string.api_url) + "actors",
                requestParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(getContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                            Actor.buildFromJSON(response.getJSONObject("actor"), mActorToEdit);
//                            get back to list upon edit
                            getActivity().onBackPressed();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

        );
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param toEdit the Actor to edit
     * @return A new instance of fragment {@link ActorEditFragment}.
     */
    public static ActorEditFragment newInstance(Actor toEdit) {
        ActorEditFragment fragment = new ActorEditFragment();
        fragment.mActorToEdit = toEdit;
        return fragment;
    }
}