package com.krunal3kapadiya.popularmovies

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.krunal3kapadiya.popularmovies.data.api.MovieApi
import com.krunal3kapadiya.popularmovies.data.api.MovieApiClient
import com.krunal3kapadiya.popularmovies.data.model.SearchResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SearchViewModel : ViewModel() {
    fun searchMovies(string: String): MediatorLiveData<SearchResponse> {
        val searchMovie = MediatorLiveData<SearchResponse>()
        val movieClient = MovieApiClient.client!!
                .create(MovieApi::class.java)
        val genres: Observable<SearchResponse>? = movieClient.searchMovie(BuildConfig.TMDB_API_KEY, string, 1, false)
        genres?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                        {
                            searchMovie.postValue(it)
                        })
                {
                    Log.e("MovieResponseException", it.message)
                }
        return searchMovie
    }
}