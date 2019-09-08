package com.krunal3kapadiya.popularmovies.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * @author krunal kapadiya
 * @link https://krunal3kapadiya.app
 * @date 14,April,2019
 */

@Entity(tableName = "tv")
data class TvResult(
        @PrimaryKey
        @SerializedName("id") val id: Int,
        @SerializedName("original_name") val originalName: String?,
        @SerializedName("name") val name: String?,
        @SerializedName("vote_count") val voteCount: Int,
        @SerializedName("first_air_date") val firstAirDate: String?,
        @SerializedName("backdrop_path") val backdropPath: String?,
        @SerializedName("vote_average") val voteAverage: String?,
        @SerializedName("overview") val overview: String?,
        @SerializedName("poster_path") val posterPath: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
            id = parcel.readInt(),
            originalName = parcel.readString(),
            name = parcel.readString(),
            voteCount = parcel.readInt(),
            firstAirDate = parcel.readString(),
            backdropPath = parcel.readString(),
            voteAverage = parcel.readString(),
            overview = parcel.readString(),
            posterPath = parcel.readString())

    override fun writeToParcel(dest: Parcel?, p1: Int) {
        dest?.writeInt(id)
        dest?.writeString(originalName)
        dest?.writeString(name)
        dest?.writeInt(voteCount)
        dest?.writeString(firstAirDate)
        dest?.writeString(backdropPath)
        dest?.writeString(voteAverage)
        dest?.writeString(overview)
        dest?.writeString(posterPath)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TvResult> {
        override fun createFromParcel(parcel: Parcel): TvResult {
            return TvResult(parcel)
        }

        override fun newArray(size: Int): Array<TvResult?> {
            return arrayOfNulls(size)
        }
    }

}