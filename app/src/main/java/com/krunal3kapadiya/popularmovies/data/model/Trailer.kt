package com.krunal3kapadiya.popularmovies.data.model

import com.google.gson.annotations.SerializedName

class Trailer {
    @SerializedName("id")
    var id: String? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("key")
    var key: String? = null
}
