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
data class ActorResult(
        @PrimaryKey
        @SerializedName("id")
        val id: Int,
        @SerializedName("profile_path")
        val profilePath: String,
        @SerializedName("name")
        val name: String
)