package com.krunal3kapadiya.popularmovies.dashBoard.actors

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.krunal3kapadiya.popularmovies.BuildConfig
import com.krunal3kapadiya.popularmovies.data.api.MovieApi
import com.krunal3kapadiya.popularmovies.data.api.MovieApiClient
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class ActorsViewModel : ViewModel() {

    fun getActorsList(): MediatorLiveData<List<Result>> {
        val movieArrayList = MediatorLiveData<List<Result>>()
        val movieClient = MovieApiClient.client!!
                .create(MovieApi::class.java)
        val genres: Observable<ActorsResponse>? = movieClient.getActorsList(BuildConfig.TMDB_API_KEY)
        genres?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                        {
                            movieArrayList.postValue(it.result)
                        })
                {
                    Log.e("MovieResponseException", it.message)
                }
        return movieArrayList
    }

    fun getActorsDetail(actorId: Int): MediatorLiveData<ActorsDetailResponse> {
        val actorsDetail = MediatorLiveData<ActorsDetailResponse>()
        val movieClient = MovieApiClient.client!!
                .create(MovieApi::class.java)
        val genres: Observable<ActorsDetailResponse>? = movieClient.getActorsDetails(BuildConfig.TMDB_API_KEY, actorId)
        genres?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                        {
                            actorsDetail.postValue(it)
                        })
                {
                    Log.e("MovieResponseException", it.message)
                }
        return actorsDetail
    }
}