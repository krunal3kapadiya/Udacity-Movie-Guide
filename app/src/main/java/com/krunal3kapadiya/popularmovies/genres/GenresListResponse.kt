package com.krunal3kapadiya.popularmovies.genres

import com.google.gson.annotations.SerializedName

data class GenresListResponse(@SerializedName("genres")
                              val genres: List<Genres>)

data class Genres(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String
)