package software.rappier.testrappier.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import software.rappier.testrappier.R;
import software.rappier.testrappier.model.MovieTrailer;
import software.rappier.testrappier.ui.utils.TrailerClickListener;
import software.rappier.testrappier.ui.viewholder.TrailerViewHolder;

import java.util.ArrayList;


public class TrailerAdapter extends RecyclerView.Adapter<TrailerViewHolder> {

    private final TrailerClickListener mTrailerClickListener;
    private final ArrayList<MovieTrailer> mMovieTrailers;

    public TrailerAdapter(ArrayList<MovieTrailer> mMovieTrailers, TrailerClickListener mTrailerClickListener) {
        this.mMovieTrailers = mMovieTrailers;
        this.mTrailerClickListener = mTrailerClickListener;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_trailer_card_view, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        MovieTrailer mMovieTrailer = this.mMovieTrailers.get(position);
        holder.bind(mMovieTrailer, mTrailerClickListener);
    }

    @Override
    public int getItemCount() {
        return this.mMovieTrailers.size();
    }

    @Override
    public void onViewRecycled(@NonNull TrailerViewHolder holder) {
        super.onViewRecycled(holder);
    }
}
