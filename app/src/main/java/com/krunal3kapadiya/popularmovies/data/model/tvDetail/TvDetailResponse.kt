package com.krunal3kapadiya.popularmovies.data.model.tvDetail

import com.google.gson.annotations.SerializedName

/**
 * @author krunal kapadiya
 * @link https://krunal3kapadiya.app
 * @date 14,April,2019
 */

data class TvDetailResponse(
        @SerializedName("id") val id: Int,
        @SerializedName("backdrop_path") val backdrop_path: String,
        @SerializedName("adult") val adult: Boolean,
        @SerializedName("first_air_date") val first_air_date: String,
        @SerializedName("last_air_date") val last_air_date: String,
        @SerializedName("in_production") val in_production: Boolean,
        @SerializedName("name") val name: String,
        @SerializedName("number_of_episodes") val number_of_episodes: Int,
        @SerializedName("number_of_seasons") val number_of_seasons: Int,
        @SerializedName("original_language") val original_language: String,
        @SerializedName("original_name") val original_name: String,
        @SerializedName("overview") val overview: String,
        @SerializedName("popularity") val popularity: Double,
        @SerializedName("poster_path") val poster_path: String,
        @SerializedName("status") val status: String,
        @SerializedName("vote_average") val vote_average: Double,
        @SerializedName("vote_count") val vote_count: Int
)