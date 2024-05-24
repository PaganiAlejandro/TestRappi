package software.rappier.testrappier.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import software.rappier.testrappier.R;
import software.rappier.testrappier.model.MovieReview;
import software.rappier.testrappier.ui.viewholder.ReviewViewHolder;
import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder> {

    private final ArrayList<MovieReview> mMovieReviews;

    public ReviewAdapter(ArrayList<MovieReview> mMovieReviews) {
        this.mMovieReviews = mMovieReviews;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_review_card_view, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        MovieReview mMovieReview = this.mMovieReviews.get(position);
        holder.bind(mMovieReview);
    }

    @Override
    public int getItemCount() {
        return this.mMovieReviews.size();
    }

    @Override
    public void onViewRecycled(@NonNull ReviewViewHolder holder) {
        super.onViewRecycled(holder);
    }
}
