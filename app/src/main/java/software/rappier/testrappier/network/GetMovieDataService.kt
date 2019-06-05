package software.rappier.testrappier.network

import software.rappier.testrappier.model.MoviePageResult

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GetMovieDataService {
    @GET("movie/popular")
    fun getPopularMovies(@Query("page") page: Int, @Query("api_key") userkey: String): Call<MoviePageResult>

    @GET("movie/top_rated")
    fun getTopRatedMovies(@Query("page") page: Int, @Query("api_key") userkey: String): Call<MoviePageResult>

    @GET("movie/upcoming")
    fun getUpcomingMovies(@Query("page") page: Int, @Query("api_key") userkey: String): Call<MoviePageResult>
}
