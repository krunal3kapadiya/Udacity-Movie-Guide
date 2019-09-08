package com.krunal3kapadiya.popularmovies.data.model

import com.google.gson.annotations.SerializedName

data class TVResponse(
        @SerializedName("page")
        val page: Int,
        @SerializedName("total_results")
        val totalResults: Int,
        @SerializedName("total_pages")
        val totalPages: Int,
        @SerializedName("results")
        val tvResults: List<TvResult>? = null
)