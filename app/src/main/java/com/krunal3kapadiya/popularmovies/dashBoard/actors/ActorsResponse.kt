package com.krunal3kapadiya.popularmovies.dashBoard.actors

import com.google.gson.annotations.SerializedName

data class ActorsResponse(@SerializedName("page")
                          val page: Int,
                          @SerializedName("total_results")
                          val totalResults: Int,
                          @SerializedName("total_pages")
                          val totalPages: Int,
                          @SerializedName("results")
                          val result: List<Result>)

data class Result(
        @SerializedName("popularity")
        val popularity: Double,
        @SerializedName("id")
        val id: Int,
        @SerializedName("profile_path")
        val profilePath: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("known_for")
        val knownFor: List<KnownFor>,
        @SerializedName("adult")
        val adult: Boolean
)

data class KnownFor(
        @SerializedName("vote_average")
        val voteAverage: Double,
        @SerializedName("vote_count")
        val voteCount: Int,
        @SerializedName("id")
        val id: Int,
        @SerializedName("video")
        val video: Boolean,
        @SerializedName("media_type")
        val mediaType: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("popularity")
        val popularity: Double,
        @SerializedName("poster_path")
        val posterPath: String,
        @SerializedName("original_language")
        val originalLanguage: String,
        @SerializedName("original_title")
        val originalTitle: String,
        @SerializedName("genre_ids")
        val genreIds: List<Int>,
        @SerializedName("backdrop_path")
        val backdropPath: String,
        @SerializedName("adult")
        val adult: Boolean,
        @SerializedName("overview")
        val overview: String,
        @SerializedName("release_date")
        val releaseDate: String
)