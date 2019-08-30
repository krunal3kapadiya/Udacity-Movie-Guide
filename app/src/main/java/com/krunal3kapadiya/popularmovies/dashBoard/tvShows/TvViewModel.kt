package com.krunal3kapadiya.popularmovies.dashBoard.tvShows

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.krunal3kapadiya.popularmovies.BuildConfig
import com.krunal3kapadiya.popularmovies.data.api.MovieApi
import com.krunal3kapadiya.popularmovies.data.api.MovieApiClient
import com.krunal3kapadiya.popularmovies.data.model.Result
import com.krunal3kapadiya.popularmovies.data.model.TVResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TvViewModel : ViewModel() {
    val errorMessage = MediatorLiveData<Boolean>()
    val movieArrayList = MediatorLiveData<List<Result>>()


    fun getPopularTvList(number: Int?, page: Int) {
        val movieClient = MovieApiClient.client!!
                .create(MovieApi::class.java)
        var getTv: Observable<TVResponse>? = null
        when (number) {
            1 -> {
                getTv = movieClient.tvAiringToday(page, BuildConfig.TMDB_API_KEY)
            }
            2 -> {
                getTv = movieClient.tvOnAir(page, BuildConfig.TMDB_API_KEY)
            }
            3 -> {
                getTv = movieClient.tvPopular(page, BuildConfig.TMDB_API_KEY)
            }
            4 -> {
                getTv = movieClient.tvTopRated(page, BuildConfig.TMDB_API_KEY)
            }
        }

        getTv?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({
                    it.results?.let { movieArrayList.postValue(it) }
                }) {
                    Log.e("MovieResponseException", it.message)
                }
    }
}