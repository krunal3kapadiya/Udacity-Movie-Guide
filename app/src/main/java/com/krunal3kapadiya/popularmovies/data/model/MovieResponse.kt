package com.krunal3kapadiya.popularmovies.data.model

import com.google.gson.annotations.SerializedName
import java.util.*

class MovieResponse {
    @SerializedName("results")
    var results: ArrayList<Movies>? = null
}
