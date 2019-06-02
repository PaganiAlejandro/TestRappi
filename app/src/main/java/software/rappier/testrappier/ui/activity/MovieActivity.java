package software.rappier.testrappier.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import software.rappier.testrappier.R;
import software.rappier.testrappier.model.Movie;
import software.rappier.testrappier.model.MovieReview;
import software.rappier.testrappier.model.MovieReviewPageResult;
import software.rappier.testrappier.model.MovieTrailer;
import software.rappier.testrappier.model.MovieTrailerPageResult;
import software.rappier.testrappier.network.GetMovieReviewService;
import software.rappier.testrappier.network.GetMovieTrailerService;
import software.rappier.testrappier.network.RetrofitInstance;
import software.rappier.testrappier.ui.adapter.ReviewAdapter;
import software.rappier.testrappier.ui.adapter.TrailerAdapter;
import software.rappier.testrappier.ui.utils.TrailerClickListener;

import static software.rappier.testrappier.ui.activity.MainActivity.API_KEY;
import static software.rappier.testrappier.ui.activity.MainActivity.movieImagePathBuilder;

@SuppressWarnings("ALL")
public class MovieActivity extends AppCompatActivity {
    @BindView(R.id.movie_activity_title) TextView mMovieTitle;
    @BindView(R.id.movie_activity_poster) ImageView mMoviePoster;
    @BindView(R.id.movie_activity_overview) TextView mMovieOverview;
    @BindView(R.id.movie_activity_release_date) TextView mMovieReleaseDate;
    @BindView(R.id.movie_activity_rating) TextView mMovieRating;
    @BindView(R.id.movie_activity_trailer_label) TextView mMovieTrailerLabel;
    @BindView(R.id.movie_activity_read_reviews) TextView mReviewsLabel;

    @BindView(R.id.rv_movie_trailers) RecyclerView mTrailerRecyclerView;

    private TrailerAdapter mTrailerAdapter;
    private ArrayList<MovieTrailer> mMovieTrailers;
    private ArrayList<MovieReview> mMovieReviews;
    private ReviewAdapter mReviewAdapter;
    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        ButterKnife.bind(this);

        mTrailerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTrailerRecyclerView.setNestedScrollingEnabled(false);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            mMovie = (Movie) bundle.getSerializable("movie");
            populateActivity(mMovie);
            if(isNetworkAvailable()){
                getReviews(mMovie.getId());
                getTrailers(mMovie.getId());
            }
        } else{
            mMovie = (Movie) savedInstanceState.getSerializable("movie");
            populateActivity(mMovie);
            if(isNetworkAvailable()){
               mMovieReviews = (ArrayList<MovieReview>) savedInstanceState.getSerializable("movie_reviews");
               populateReviews(mMovieReviews);

               mMovieTrailers = (ArrayList<MovieTrailer>) savedInstanceState.getSerializable("movie_trailers");
               populateTrailers(mMovieTrailers);
            }
        }
    }

    private void populateActivity(Movie mMovie){
        Picasso.with(this).load(movieImagePathBuilder(mMovie.getPosterPath())).into(mMoviePoster);
        mMovieTitle.setText(mMovie.getTitle());
        mMovieOverview.setText(mMovie.getOverview());
        mMovieReleaseDate.setText(mMovie.getReleaseDate());
        String userRatingText = String.valueOf(mMovie.getVoteAverage()) + "/10";
        mMovieRating.setText(userRatingText);
    }

    private void populateReviews(ArrayList<MovieReview> mMovieReviews){
        if(mMovieReviews.size() > 0){
            mReviewsLabel.setVisibility(View.VISIBLE);
        }
    }

    private void populateTrailers(ArrayList<MovieTrailer> mMovieTrailers){
        if(mMovieTrailers.size() > 0){
            mMovieTrailerLabel.setVisibility(View.VISIBLE);
            mTrailerRecyclerView.setVisibility(View.VISIBLE);
            mTrailerAdapter = new TrailerAdapter(mMovieTrailers, new TrailerClickListener() {
                @Override
                public void onMovieTrailerClick(MovieTrailer mMovieTrailer) {
                    Intent mTrailerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + mMovieTrailer.getKey()));
                    startActivity(mTrailerIntent);
                }
            });
            mTrailerRecyclerView.setAdapter(mTrailerAdapter);
        }
    }


    private void getTrailers(int movieId) {
        GetMovieTrailerService movieTrailerService = RetrofitInstance.getRetrofitInstance().create(GetMovieTrailerService.class);
        Call<MovieTrailerPageResult> call = movieTrailerService.getTrailers(movieId, API_KEY);


        call.enqueue(new Callback<MovieTrailerPageResult>() {
            @Override
            public void onResponse(Call<MovieTrailerPageResult> call, Response<MovieTrailerPageResult> response) {
                mMovieTrailers = response.body().getTrailerResult();
                populateTrailers(mMovieTrailers);
            }

            @Override
            public void onFailure(Call<MovieTrailerPageResult> call, Throwable t) {
                Toast.makeText(MovieActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getReviews(int movieId) {
        GetMovieReviewService mGetMovieReviewService = RetrofitInstance.getRetrofitInstance().create(GetMovieReviewService.class);
        Call<MovieReviewPageResult> call = mGetMovieReviewService.getReviews(movieId, API_KEY);


        call.enqueue(new Callback<MovieReviewPageResult>() {
            @Override
            public void onResponse(Call<MovieReviewPageResult> call, Response<MovieReviewPageResult> response) {
                mMovieReviews = response.body().getResults();
               populateReviews(mMovieReviews);
            }

            @Override
            public void onFailure(Call<MovieReviewPageResult> call, Throwable t) {
                Toast.makeText(MovieActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            }
        });
    }



    @OnClick(R.id.movie_activity_read_reviews)
    public void readReviews(){
        Intent readReviews = new Intent(this, ReviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("reviews", mMovieReviews);
        bundle.putString("movie_title", mMovie.getTitle());
        readReviews.putExtras(bundle);
        startActivity(readReviews);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("movie", mMovie);
        if(isNetworkAvailable()){
            outState.putSerializable("movie_reviews", mMovieReviews);
            outState.putSerializable("movie_trailers", mMovieTrailers);
        }
    }
}
