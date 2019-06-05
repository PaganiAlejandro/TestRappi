package software.rappier.testrappier.model

import com.google.gson.annotations.SerializedName

import java.io.Serializable
import java.util.ArrayList

class MovieTrailerPageResult : Serializable {
    @SerializedName("id")
    var id: Int = 0
    @SerializedName("results")
    var trailerResult: ArrayList<MovieTrailer>? = null
}
