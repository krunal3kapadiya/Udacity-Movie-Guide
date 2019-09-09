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
        @SerializedName("name") val name: String?,
        @SerializedName("poster_path") val posterPath: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
            id = parcel.readInt(),
            name = parcel.readString(),
            posterPath = parcel.readString())

    override fun writeToParcel(dest: Parcel?, p1: Int) {
        dest?.writeInt(id)
        dest?.writeString(name)
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