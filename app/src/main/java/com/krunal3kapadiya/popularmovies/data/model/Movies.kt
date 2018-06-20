package com.krunal3kapadiya.popularmovies.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Movies(@SerializedName("id") var id: Int = 0,
             @SerializedName("original_title") var name: String? = null,
             @SerializedName("poster_path") var url: String? = null,
             @SerializedName("overview") var overView: String? = null,
             @SerializedName("vote_average") var rating: String? = null,
             @SerializedName("release_date") var releaseDate: String? = null,
             @SerializedName("backdrop_path") var backDropPath: String? = null) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(id)
        dest?.writeString(name)
        dest?.writeString(url)
        dest?.writeString(overView)
        dest?.writeString(rating)
        dest?.writeString(releaseDate)
        dest?.writeString(backDropPath)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Movies> {
        override fun createFromParcel(parcel: Parcel): Movies {
            return Movies(parcel)
        }

        override fun newArray(size: Int): Array<Movies?> {
            return arrayOfNulls(size)
        }
    }
}