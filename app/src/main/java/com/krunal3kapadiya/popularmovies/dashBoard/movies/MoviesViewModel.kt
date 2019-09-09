package com.krunal3kapadiya.popularmovies.dashBoard.movies

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.util.Log
import com.krunal3kapadiya.popularmovies.BuildConfig
import com.krunal3kapadiya.popularmovies.base.BaseViewModel
import com.krunal3kapadiya.popularmovies.dao.MoviesDao
import com.krunal3kapadiya.popularmovies.data.api.MovieApi
import com.krunal3kapadiya.popularmovies.data.api.MovieApiClient
import com.krunal3kapadiya.popularmovies.data.model.Cast
import com.krunal3kapadiya.popularmovies.data.model.MovieResponse
import com.krunal3kapadiya.popularmovies.data.model.Movies
import com.krunal3kapadiya.popularmovies.data.moviedetails.MovieDetailResponse
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MoviesViewModel(private val moviesDao: MoviesDao) : BaseViewModel() {

    val errorMessage = MediatorLiveData<String>()
    val movieArrayList = MediatorLiveData<ArrayList<Movies>>()
    val mCastArrayList = MediatorLiveData<List<Cast>>()
    val isLoading = MediatorLiveData<Boolean>()

    /**
     * add and update posts
     */
    fun addOrUpdateMovies(movies: Movies): Completable {
        return Completable.fromAction {
            moviesDao.insert(movies)
        }
    }

    fun addToFavourite(movies: Movies) {
        disposable.add(addOrUpdateMovies(movies)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe())

    }

    fun getPopularMovieList(number: Int?, page: Int) {
        isLoading.postValue(true)
        val movieClient = MovieApiClient.client!!
                .create(MovieApi::class.java)
        var getPopMovie: Observable<MovieResponse>? = null
        when (number) {
            1 -> {
                getPopMovie = movieClient.getNowPlayingMovies(page, BuildConfig.TMDB_API_KEY)
            }
            2 -> {
                getPopMovie = movieClient.getPopularMoviesList(page, BuildConfig.TMDB_API_KEY)
            }
            3 -> {
                getPopMovie = movieClient.getUpComingMovies(page, BuildConfig.TMDB_API_KEY)
            }
            4 -> {
                getPopMovie = movieClient.getTopRatedMovies(page, BuildConfig.TMDB_API_KEY)
            }
            5 -> {
//                getPopMovie = movieClient.getMovieByYear(BuildConfig.TMDB_API_KEY)
            }

        }

        getPopMovie?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({
                    it.results?.let { movieArrayList.postValue(it) }
                    isLoading.postValue(false)
                }) {
                    errorMessage.postValue(it.message)
                    Log.e("MovieResponseException", it.message)
                    isLoading.postValue(false)
                }
    }

    fun getMovieDetail(movieId: Int): MediatorLiveData<MovieDetailResponse> {
        val data: MediatorLiveData<MovieDetailResponse> = MediatorLiveData()
        val movieClient = MovieApiClient.client!!
                .create(MovieApi::class.java)
        disposable.add(movieClient.getMovieDetails(
                movieId,
                BuildConfig.TMDB_API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    data.postValue(it)
                }, {
                    errorMessage.postValue(it.message)
                }))
        return data
    }

    fun getCast(cast_id: Int) {
        val movieClient = MovieApiClient.client!!
                .create(MovieApi::class.java)
        movieClient.getCastList(cast_id, BuildConfig.TMDB_API_KEY)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({
                    it.cast?.let { mCastArrayList.postValue(it) }
                }) {
                    Log.e("MovieResponseException", it.message)
                }
    }

    fun getMoviesById(id: Int): LiveData<Movies> {
        return moviesDao.getMoviesById(id)
    }

    fun removeMovies(id: Int) {
        disposable.add(removeFromFavourite(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe())
    }

    private fun removeFromFavourite(id: Int): Completable {
        return Completable.fromAction {
            moviesDao.deleteMoviesById(id)
        }
    }
}