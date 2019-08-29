package com.krunal3kapadiya.popularmovies.genres

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.krunal3kapadiya.popularmovies.BuildConfig
import com.krunal3kapadiya.popularmovies.data.api.MovieApi
import com.krunal3kapadiya.popularmovies.data.api.MovieApiClient
import com.krunal3kapadiya.popularmovies.data.model.MovieResponse
import com.krunal3kapadiya.popularmovies.data.model.Movies
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GenresViewModel : ViewModel() {
    //    val genresList: MutableList<MediaStore.Audio.Genres>
    fun getGenresList(): MediatorLiveData<GenresListResponse> {
        val genresList = MediatorLiveData<GenresListResponse>()

        val movieClient = MovieApiClient.client!!
                .create(MovieApi::class.java)
        val genres: Observable<GenresListResponse>? = movieClient.getGenresList(BuildConfig.TMDB_API_KEY)
        genres?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                        {
                            genresList.postValue(it)
                        })
                {
                    Log.e("MovieResponseException", it.message)
                }
        return genresList
    }

    fun getGenresById(id: Int): MediatorLiveData<ArrayList<Movies>> {
        val genresList = MediatorLiveData<ArrayList<Movies>>()

        val movieClient = MovieApiClient.client!!
                .create(MovieApi::class.java)
        val genres: Observable<MovieResponse>? = movieClient.getGenresById(BuildConfig.TMDB_API_KEY,
                id)
        genres?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                        {
                            genresList.postValue(it.results)
                        })
                {
                    Log.e("MovieResponseException", it.message)
                }
        return genresList
    }

}