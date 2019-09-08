package com.krunal3kapadiya.popularmovies.data.model.actors

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * @author krunal kapadiya
 * @link https://krunal3kapadiya.app
 * @date 14,April,2019
 */

@Entity(tableName = "actors")
data class Result(
        @SerializedName("popularity")
        val popularity: Double,
        @PrimaryKey
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