package software.rappier.testrappier.model

import com.google.gson.annotations.SerializedName

import java.io.Serializable

class Movie : Serializable {
    @SerializedName("vote_count")
    var voteCount: Int = 0
    @SerializedName("id")
    var id: Int = 0
    @SerializedName("video")
    var isVideo: Boolean = false
    @SerializedName("vote_average")
    var voteAverage: Double = 0.toDouble()
    @SerializedName("title")
    var title: String? = null
    @SerializedName("popularity")
    var popularity: Double = 0.toDouble()
    @SerializedName("poster_path")
    var posterPath: String? = null
    @SerializedName("original_language")
    var originalLanguage: String? = null
    @SerializedName("original_title")
    var originalTitle: String? = null
    @SerializedName("genre_ids")
    var genreId: List<Int>? = null
    @SerializedName("backdrop_path")
    var backdropPath: String? = null
    @SerializedName("adult")
    var isAdult: Boolean = false
    @SerializedName("overview")
    var overview: String? = null
    @SerializedName("release_date")
    var releaseDate: String? = null

}
