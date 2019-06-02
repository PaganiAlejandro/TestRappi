package software.rappier.testrappier.ui.viewholder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import software.rappier.testrappier.R;
import software.rappier.testrappier.model.Movie;
import software.rappier.testrappier.ui.utils.MovieClickListener;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static software.rappier.testrappier.ui.activity.MainActivity.getMeasuredPosterHeight;
import static software.rappier.testrappier.ui.activity.MainActivity.getScreenWidth;
import static software.rappier.testrappier.ui.activity.MainActivity.movieImagePathBuilder;

@SuppressWarnings("ALL")
public class MovieViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.iv_movie_poster) ImageView mMoviePoster;
    @BindView(R.id.cv_movie_card) CardView mMovieCard;

    public MovieViewHolder(final View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(final Movie movie, final MovieClickListener movieClickListener) {
        mMovieCard.setLayoutParams(new ViewGroup.LayoutParams(getScreenWidth()/2, getMeasuredPosterHeight(getScreenWidth()/2)));

        Picasso.with(mMoviePoster.getContext()).load(movieImagePathBuilder(movie.getPosterPath())).placeholder(R.drawable.placeholder).fit().centerCrop().into(mMoviePoster);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieClickListener.onMovieClick(movie);
            }
        });
    }
}
