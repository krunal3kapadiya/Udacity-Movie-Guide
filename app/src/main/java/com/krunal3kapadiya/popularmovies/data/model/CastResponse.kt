package com.krunal3kapadiya.popularmovies.data.model

import com.google.gson.annotations.SerializedName

data class CastResponse(
        @SerializedName("id")
        val id: Int,
        @SerializedName("cast")
        val cast: List<Cast>? = null
)