package films;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.laramoviesandroid.R;
import com.example.laramoviesandroid.models.Film;
import com.squareup.picasso.Picasso;


public class FilmViewDialogFragment extends DialogFragment {
    private Film mFilm; // send the film to show here
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_film_view_dialog, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_film_view_title);
        TextView tvGenre = (TextView) view.findViewById(R.id.tv_film_view_genre);
        TextView tvReleaseDate = (TextView) view.findViewById(R.id.tv_film_view_release_date);
        TextView tvDuration = (TextView) view.findViewById(R.id.tv_film_view_duration);
        TextView tvStory = (TextView) view.findViewById(R.id.tv_film_view_story);
        TextView tvAdditionalInfo = (TextView) view.findViewById(R.id.tv_film_view_additional_info);
        ImageView ivPoster = (ImageView) view.findViewById(R.id.image_film_view_poster);

        tvTitle.setText(mFilm.getTitle());
        tvGenre.setText(mFilm.getGenre());
        tvReleaseDate.setText(mFilm.getFormattedReleaseDate());
        tvDuration.setText(mFilm.getDurationString());
        tvStory.setText(mFilm.getStory());
        tvAdditionalInfo.setText(mFilm.getAdditionalInfo());
        Picasso.get().load(mFilm.getPosterURL()).into(ivPoster);
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
                        FilmViewDialogFragment.this.getDialog().cancel();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
    public void setFilm(Film film) {
        this.mFilm = film;
    }
}
