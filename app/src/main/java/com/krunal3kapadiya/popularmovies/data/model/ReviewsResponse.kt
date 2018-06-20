package com.krunal3kapadiya.popularmovies.data.model

import com.google.gson.annotations.SerializedName
import java.util.*

class ReviewsResponse {
    @SerializedName("id")
    private val movieId: Long = 0
    @SerializedName("page")
    private val page: Int = 0
    @SerializedName("results")
    var reviewsArrayList: ArrayList<Reviews>? = null
    @SerializedName("total_pages")
    private val totalPages: Int = 0
}
