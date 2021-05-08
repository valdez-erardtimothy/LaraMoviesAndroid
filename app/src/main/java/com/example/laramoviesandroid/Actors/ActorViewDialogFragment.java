package com.example.laramoviesandroid.Actors;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.laramoviesandroid.FilmViewDialogFragment;
import com.example.laramoviesandroid.R;
import com.example.laramoviesandroid.models.Actor;
import com.example.laramoviesandroid.models.Film;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActorViewDialogFragment extends DialogFragment {
    private Actor mActor;
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_actor_view_dialog, null);
        TextView tvName = (TextView) view.findViewById(R.id.text_actor_view_name);
        TextView tvNotes = (TextView) view.findViewById(R.id.text_actor_notes);
        ImageView ivPortrait = (ImageView) view.findViewById(R.id.image_actor_portrait);

        tvName.setText(mActor.getName());
        tvNotes.setText(mActor.getNotes());
        Picasso.get().load(mActor.getPortraitUrl()).into(ivPortrait);
        builder.setView(view)
                .setPositiveButton(R.string.edit, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        redirect to edit
                    }
                })
                .setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                })
                .setNeutralButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActorViewDialogFragment.this.getDialog().cancel();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void setActor(Actor actor) {
        this.mActor = actor;
    }

}