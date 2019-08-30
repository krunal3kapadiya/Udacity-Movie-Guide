package com.krunal3kapadiya.popularmovies

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.krunal3kapadiya.popularmovies.dashBoard.actors.ActorsResponse
import com.krunal3kapadiya.popularmovies.data.api.MovieApi
import com.krunal3kapadiya.popularmovies.data.api.MovieApiClient
import com.krunal3kapadiya.popularmovies.data.model.MovieResponse
import com.krunal3kapadiya.popularmovies.data.model.TVResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SearchViewModel : ViewModel() {
    val searchMovie = MediatorLiveData<MovieResponse>()
    val searchTV = MediatorLiveData<TVResponse>()
    val searchActor = MediatorLiveData<ActorsResponse>()

    fun searchMovies(string: String, tabPosition: Int, page: Int) {
        val movieClient = MovieApiClient.client!!
                .create(MovieApi::class.java)

        when (tabPosition) {
            0 -> {
                val genres: Observable<MovieResponse>? = movieClient.searchMovie(
                        BuildConfig.TMDB_API_KEY,
                        string,
                        page,
                        false)
                genres?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
                        ?.subscribe(
                                {
                                    searchMovie.postValue(it)
                                })
                        {
                            Log.e("MovieResponseException", it.message)
                        }

            }
            1 -> {
                val genres: Observable<TVResponse>? = movieClient.searchTVShows(
                        BuildConfig.TMDB_API_KEY,
                        string,
                        page)
                genres?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
                        ?.subscribe(
                                {
                                    searchTV.postValue(it)
                                })
                        {
                            Log.e("MovieResponseException", it.message)
                        }
            }
            2 -> {
                val genres: Observable<ActorsResponse>? = movieClient.searchPerson(
                        BuildConfig.TMDB_API_KEY,
                        string,
                        page)
                genres?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
                        ?.subscribe(
                                {
                                    searchActor.postValue(it)
                                })
                        {
                            Log.e("MovieResponseException", it.message)
                        }
            }
        }
    }
}