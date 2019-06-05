package software.rappier.testrappier.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.io.Serializable

class MovieReview : Serializable {
    @SerializedName("author")
    @Expose
    var author: String? = null
    @SerializedName("content")
    @Expose
    var content: String? = null
    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("url")
    @Expose
    var url: String? = null
}
