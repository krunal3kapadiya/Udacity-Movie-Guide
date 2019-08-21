package com.krunal3kapadiya.popularmovies.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class TVResponse(
        @SerializedName("page")
        val page: Int,
        @SerializedName("total_results")
        val totalResults: Int,
        @SerializedName("total_pages")
        val totalPages: Int,
        @SerializedName("results")
        val results: List<Result>? = null
)

data class Result(
        @SerializedName("original_name")
        val originalName: String,
        @SerializedName("genre_ids")
        val genreIds: List<Int>? = null,
        @SerializedName("name")
        val name: String,
        @SerializedName("popularity")
        val popularity: Double,
        @SerializedName("origin_country")
        val originCountry: List<String>? = null,
        @SerializedName("vote_count")
        val voteCount: Int,
        @SerializedName("first_air_date")
        val firstAirDate: String?,
        @SerializedName("backdrop_path")
        val backdropPath: String?,
        @SerializedName("original_language")
        val originalLanguage: String?,
        @SerializedName("id")
        val id: Int,
        @SerializedName("vote_average")
        val voteAverage: Double,
        @SerializedName("overview")
        val overview: String?,
        @SerializedName("poster_path")
        val posterPath: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            null,
            parcel.readString(),
            parcel.readDouble(),
            parcel.createStringArrayList(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readDouble(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(dest: Parcel?, p1: Int) {
        dest?.writeString(originalName)
        dest?.writeIntArray(genreIds?.toIntArray())
        dest?.writeString(name)
        dest?.writeDouble(popularity)
        dest?.writeStringList(originCountry)
        dest?.writeInt(voteCount)
        dest?.writeString(firstAirDate)
        dest?.writeString(backdropPath)
        dest?.writeString(originalLanguage)
        dest?.writeInt(id)
        dest?.writeDouble(voteAverage)
        dest?.writeString(overview)
        dest?.writeString(posterPath)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Result> {
        override fun createFromParcel(parcel: Parcel): Result {
            return Result(parcel)
        }

        override fun newArray(size: Int): Array<Result?> {
            return arrayOfNulls(size)
        }
    }

}