package com.krunal3kapadiya.popularmovies.dashBoard.movies

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import com.krunal3kapadiya.popularmovies.Constants
import com.krunal3kapadiya.popularmovies.data.api.MovieApiClient
import com.krunal3kapadiya.popularmovies.data.api.MovieApi
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


    fun getPopularMovieList(number: Int?) {
        val movieClient = MovieApiClient.client!!
                .create(MovieApi::class.java)
        var getPopMovie: Observable<MovieResponse>? = null
        when (number) {
            1 -> {
                getPopMovie = movieClient.getNowPlayingMovies(Constants.API_KEY)
            }
            2 -> {
                getPopMovie = movieClient.getPopularMoviesList(Constants.API_KEY)
            }
            3 -> {
                getPopMovie = movieClient.getUpComingMovies(Constants.API_KEY)
            }
            4 -> {
                getPopMovie = movieClient.getTopRatedMovies(Constants.API_KEY)
            }
            5 -> {
//                getPopMovie = movieClient.getMovieByYear(Constants.API_KEY)
            }

        }

        getPopMovie?.observeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({
                    it.results?.let { movieArrayList.postValue(it) }
                }, {})
    }
}