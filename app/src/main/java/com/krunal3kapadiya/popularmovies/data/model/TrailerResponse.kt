package com.krunal3kapadiya.popularmovies.data.model

import com.google.gson.annotations.SerializedName
import java.util.*

class TrailerResponse {
    @SerializedName("results")
    var trailerArrayList: ArrayList<Trailer>? = null
}
