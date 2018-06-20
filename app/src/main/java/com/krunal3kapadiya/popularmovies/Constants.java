package com.krunal3kapadiya.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Krunal on 7/27/2017.
 */

public class Constants {
    public static String BASE_IMAGE_URL = "http://image.tmdb.org/t/p";
    public static String API_KEY = "[YOUR API]";
    public static String POSTER_SIZE = "/w185";
    public static String POSTER_SIZE_500 = "/w500";

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
