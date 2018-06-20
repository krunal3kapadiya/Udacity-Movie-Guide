package com.krunal3kapadiya.popularmovies

import android.content.Context
import android.net.ConnectivityManager

object Constants {
    var BASE_IMAGE_URL = "http://image.tmdb.org/t/p"
    var API_KEY = "[YOUR API]"
    var POSTER_SIZE = "/w185"
    var POSTER_SIZE_500 = "/w500"

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}
