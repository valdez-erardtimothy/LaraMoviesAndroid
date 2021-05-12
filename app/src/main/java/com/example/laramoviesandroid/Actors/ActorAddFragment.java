package com.example.laramoviesandroid.Actors;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import com.example.laramoviesandroid.utilities.ImageUtilities;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ActorAddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActorAddFragment extends Fragment {
    private EditText mName, mNotes;
    private Button mSubmit, mCancel, mChooseImage;
    private ImageView mIvPortrait;

    public ActorAddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ActorAddFragment.
     */
    public static ActorAddFragment newInstance() {
        ActorAddFragment fragment = new ActorAddFragment();
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
        View view = inflater.inflate(R.layout.fragment_actor_add, container, false);
        getActivity().setTitle(R.string.actor_add_title);
        mName = view.findViewById(R.id.edit_actor_add_name);
        mNotes = view.findViewById(R.id.edit_actor_add_notes);
        mSubmit = view.findViewById(R.id.button_actor_add_submit);
        mCancel = view.findViewById(R.id.button_actor_add_cancel);
        mChooseImage = view.findViewById(R.id.button_actor_add_choose_image);
        mIvPortrait = view.findViewById(R.id.image_actor_add_portrait);
        setButtonEventListeners();
        return view;
    }

    void setButtonEventListeners() {
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }

        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        mChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImageFromGallery();
            }
        });
    }

    void submit() {
        JSONObject requestParams = new JSONObject();
//        Bitmap portraitBmp = ((BitmapDrawable) mIvPortrait.getDrawable()).getBitmap();
        mIvPortrait.buildDrawingCache();
        Bitmap portraitBmp = mIvPortrait.getDrawingCache();
//        return;
        try {
            requestParams.put("actor_fullname", mName.getText());
            requestParams.put("actor_notes", mNotes.getText());
            requestParams.put("portrait_base64", ImageUtilities.bmpToBase64(portraitBmp));
            requestParams.put("id", 0); // means to add it
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
                            Actor newActor = Actor.buildFromJSON(response.getJSONObject("actor"));
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

    void cancel() {
        getActivity().onBackPressed();
    }

    void chooseImageFromGallery() {
        Intent chooseImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(chooseImageIntent, ActorEditFragment.GET_PORTRAIT_EDIT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(null, "Image received on Actor Edit");
        if(requestCode == ActorEditFragment.GET_PORTRAIT_EDIT && resultCode == RESULT_OK && data != null) {
            Toast.makeText(getContext(), "Image Selected!", Toast.LENGTH_SHORT).show();
            Uri selectedImage = data.getData();

            Picasso.get().load(selectedImage).into(mIvPortrait);
        }
    }

}