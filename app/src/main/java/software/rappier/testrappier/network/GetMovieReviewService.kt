package software.rappier.testrappier.network

import software.rappier.testrappier.model.MovieReviewPageResult

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GetMovieReviewService {
    @GET("movie/{id}/reviews")
    fun getReviews(@Path("id") movieId: Int, @Query("api_key") userkey: String): Call<MovieReviewPageResult>
}
