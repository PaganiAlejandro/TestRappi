package software.rappier.testrappier.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import software.rappier.testrappier.R;
import software.rappier.testrappier.model.Movie;
import software.rappier.testrappier.model.MoviePageResult;
import software.rappier.testrappier.network.GetMovieDataService;
import software.rappier.testrappier.network.RetrofitInstance;
import software.rappier.testrappier.ui.adapter.MovieAdapter;;
import software.rappier.testrappier.ui.utils.EndlessRecyclerViewScrollListener;
import software.rappier.testrappier.ui.utils.MovieClickListener;

public class MainActivity extends AppCompatActivity {
    public static final String API_KEY = "7e414f14bfadabe518660083ae7fa77d";
    private static final int FIRST_PAGE = 1;
    private int totalPages;
    private int currentSortMode = 1;
    private Call<MoviePageResult> call;
    private List<Movie> movieResults;
    private MovieAdapter movieAdapter;
    int cacheSize = 10 * 1024 * 1024; // 10 MiB

    @BindView(R.id.rv_movies) RecyclerView recyclerView;
    @BindView(R.id.tv_no_internet_error) ConstraintLayout mNoInternetMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if(!isConected()){
            recyclerView.setVisibility(View.GONE);
            mNoInternetMessage.setVisibility(View.VISIBLE);
        }

        GridLayoutManager manager = new GridLayoutManager(this, 2);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });
        recyclerView.setLayoutManager(manager);
        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if ((page + 1) <= totalPages && currentSortMode != 3) {
                    loadPage(page + 1);
                }
            }
        };

        recyclerView.addOnScrollListener(scrollListener);

        loadPage(FIRST_PAGE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //SortID 1 -> Popularity
        //SortID 2 -> Top rated
        //SortID 3 -> Upcoming
        switch (item.getItemId()) {
            case R.id.sort_by_popularity:
                currentSortMode = 1;
                break;
            case R.id.sort_by_top:
                currentSortMode = 2;
                break;
            case R.id.sort_by_upcoming:
                currentSortMode = 3;
                break;
        }

        loadPage(FIRST_PAGE);

        return super.onOptionsItemSelected(item);
    }

    private void loadPage(final int page) {

        //CACHE
        Cache cache = new Cache(getCacheDir(), cacheSize);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Interceptor.Chain chain)
                            throws IOException {
                        Request request = chain.request();
                        if (!isNetworkAvailable()) {
                            int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale \
                            request = request
                                    .newBuilder()
                                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                                    .build();
                        }
                        return chain.proceed(request);
                    }
                })
                .build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/3/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        GetMovieDataService movieDataService = retrofit.create(GetMovieDataService.class);

        //old call to service
        //GetMovieDataService movieDataService = RetrofitInstance.getRetrofitInstance().create(GetMovieDataService.class);

        switch(currentSortMode){
            case 1:
                call = movieDataService.getPopularMovies(page, API_KEY);
                break;
            case 2:
                call = movieDataService.getTopRatedMovies(page, API_KEY);
                break;
            case 3:
                call = movieDataService.getUpcomingMovies(page, API_KEY);
                break;
        }

        call.enqueue(new Callback<MoviePageResult>() {
            @Override
            public void onResponse(@NonNull Call<MoviePageResult> call, @NonNull Response<MoviePageResult> response) {

                if(page == 1) {
                    assert response.body() != null;
                    movieResults = response.body().getMovieResult();
                    assert response.body() != null;
                    totalPages = response.body().getTotalPages();

                    movieAdapter = new MovieAdapter(movieResults, new MovieClickListener() {
                        @Override
                        public void onMovieClick(Movie movie) {
                            Intent intent = new Intent(MainActivity.this, MovieActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("movie", movie);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                    recyclerView.setAdapter(movieAdapter);
                } else {
                    assert response.body() != null;
                    List<Movie> movies = response.body().getMovieResult();
                    for(Movie movie : movies){
                        movieResults.add(movie);
                        movieAdapter.notifyItemInserted(movieResults.size() - 1);
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<MoviePageResult> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }
    public static int getMeasuredPosterHeight(int width) {
        return (int) (width * 1.5f);
    }

    public static String movieImagePathBuilder(String imagePath) {
        return "https://image.tmdb.org/t/p/" +
                "w500" +
                imagePath;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private boolean isConected(){
        boolean value = false;

        ConnectivityManager connectivity  = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info_wifi = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo info_datos = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ( String.valueOf(info_wifi.getState()).equals("CONNECTED")){
            value = true;
            Toast.makeText(this,"Conectado a WIFI",Toast.LENGTH_SHORT).show();
        }else{
            if ( String.valueOf(info_datos.getState()).equals("CONNECTED")){
                value = true;
                Toast.makeText(this,"Conectado a DATOS",Toast.LENGTH_SHORT).show();
            }else{
                value = false;
                Toast.makeText(this,"No tiene coneccion",Toast.LENGTH_SHORT).show();
            }
        }
        return value;
    }

    @OnClick(R.id.tv_no_internet_error_refresh)
    public void refreshActivity(){
        finish();
        startActivity(getIntent());
    }

}