package com.krunal3kapadiya.popularmovies.genres

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class GenresListResponse(@SerializedName("genres")
                              val genres: List<Genres>)

data class Genres(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
            id = parcel.readInt(),
            name = parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Genres> {
        override fun createFromParcel(parcel: Parcel): Genres {
            return Genres(parcel)
        }

        override fun newArray(size: Int): Array<Genres?> {
            return arrayOfNulls(size)
        }
    }
}