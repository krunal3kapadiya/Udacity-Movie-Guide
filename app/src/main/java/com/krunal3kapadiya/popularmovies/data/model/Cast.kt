package com.krunal3kapadiya.popularmovies.data.model

import android.arch.persistence.room.Entity
import com.google.gson.annotations.SerializedName

/**
 * @author krunal kapadiya
 * @link https://krunal3kapadiya.app
 * @date 14,April,2019
 */
data class Cast(
        @SerializedName("cast_id")
        val cast_id: Int,
        @SerializedName("character")
        val character: String,
        @SerializedName("credit_id")
        val credit_id: String,
        @SerializedName("gender")
        val gender: Int,
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String? = null,
        @SerializedName("order")
        val order: Int,
        @SerializedName("profile_path")
        val profile_path: String,
        @SerializedName("poster_path")
        val poster_path: String,
        @SerializedName("title")
        val title: String? = null
)
