package com.krunal3kapadiya.popularmovies.dashBoard.movies

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import android.widget.Toast
import com.krunal3kapadiya.popularmovies.BuildConfig
import com.krunal3kapadiya.popularmovies.Constants
import com.krunal3kapadiya.popularmovies.data.api.MovieApiClient
import com.krunal3kapadiya.popularmovies.data.api.MovieApi
import com.krunal3kapadiya.popularmovies.data.model.Cast
import com.krunal3kapadiya.popularmovies.data.model.MovieResponse
import com.krunal3kapadiya.popularmovies.data.model.Movies
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesViewModel : ViewModel() {

    val errorMessage = MediatorLiveData<Boolean>()
    val movieArrayList = MediatorLiveData<ArrayList<Movies>>()
    val mCastArrayList = MediatorLiveData<List<Cast>>()


    fun getPopularMovieList(number: Int?, page: Int) {
        val movieClient = MovieApiClient.client!!
                .create(MovieApi::class.java)
        var getPopMovie: Observable<MovieResponse>? = null
        when (number) {
            1 -> {
                getPopMovie = movieClient.getNowPlayingMovies(page, BuildConfig.TMDB_API_KEY)
            }
            2 -> {
                getPopMovie = movieClient.getPopularMoviesList(page,BuildConfig.TMDB_API_KEY)
            }
            3 -> {
                getPopMovie = movieClient.getUpComingMovies(page,BuildConfig.TMDB_API_KEY)
            }
            4 -> {
                getPopMovie = movieClient.getTopRatedMovies(page,BuildConfig.TMDB_API_KEY)
            }
            5 -> {
//                getPopMovie = movieClient.getMovieByYear(BuildConfig.TMDB_API_KEY)
            }

        }

        getPopMovie?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({
                    it.results?.let { movieArrayList.postValue(it) }
                }) {
                    Log.e("MovieResponseException", it.message)
                }
    }

    fun getCast(cast_id: Int) {
        val movieClient = MovieApiClient.client!!
                .create(MovieApi::class.java)
        movieClient.getCastList(cast_id, BuildConfig.TMDB_API_KEY)
                ?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({
                    it.cast?.let { mCastArrayList.postValue(it) }
                }) {
                    Log.e("MovieResponseException", it.message)
                }
    }
}