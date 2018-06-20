package com.krunal3kapadiya.popularmovies.data.model

import android.os.Parcel
import com.google.gson.annotations.SerializedName

class Reviews protected constructor(`in`: Parcel) {
    @SerializedName("id")
    var id: String? = null
    @SerializedName("auther")
    var auther: String? = null
    @SerializedName("content")
    var content: String? = null
    @SerializedName("url")
    var reviewURL: String? = null

    init {
        id = `in`.readString()
        auther = `in`.readString()
        content = `in`.readString()
        reviewURL = `in`.readString()
    }
}
