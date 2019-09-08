package com.krunal3kapadiya.popularmovies.dashBoard.actors

import android.arch.lifecycle.MediatorLiveData
import android.util.Log
import com.krunal3kapadiya.popularmovies.base.BaseViewModel
import com.krunal3kapadiya.popularmovies.BuildConfig
import com.krunal3kapadiya.popularmovies.data.api.MovieApi
import com.krunal3kapadiya.popularmovies.data.api.MovieApiClient
import com.krunal3kapadiya.popularmovies.data.model.CastResponse
import com.krunal3kapadiya.popularmovies.data.model.actors.Result
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ActorsViewModel : BaseViewModel() {
    val isLoading = MediatorLiveData<Boolean>()

    fun getActorsList(page: Int): MediatorLiveData<List<Result>> {
        isLoading.postValue(true)
        val movieArrayList = MediatorLiveData<List<Result>>()
        val movieClient = MovieApiClient.client!!
                .create(MovieApi::class.java)
        val genres: Observable<ActorsResponse>? = movieClient.getActorsList(page, BuildConfig.TMDB_API_KEY)
        genres?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                        {
                            isLoading.postValue(false)
                            movieArrayList.postValue(it.result)
                        })
                {
                    isLoading.postValue(false)
                    Log.e("MovieResponseException", it.message)
                }
        return movieArrayList
    }

    fun getActorsDetail(actorId: Int): MediatorLiveData<ActorsDetailResponse> {
        val actorsDetail = MediatorLiveData<ActorsDetailResponse>()
        val movieClient = MovieApiClient.client!!
                .create(MovieApi::class.java)
        val genres: Observable<ActorsDetailResponse>? = movieClient.getActorsDetails(actorId, BuildConfig.TMDB_API_KEY)
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

    fun getActorsMovieShows(actorId: Int): MediatorLiveData<CastResponse> {
        val actorsDetail = MediatorLiveData<CastResponse>()
        val movieClient = MovieApiClient.client!!
                .create(MovieApi::class.java)
        val genres: Observable<CastResponse>? = movieClient.getActorsMovieCredits(actorId, BuildConfig.TMDB_API_KEY)
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

    fun getActorsTVShows(actorId: Int): MediatorLiveData<CastResponse> {
        val actorsDetail = MediatorLiveData<CastResponse>()
        val movieClient = MovieApiClient.client!!
                .create(MovieApi::class.java)
        val genres: Observable<CastResponse>? = movieClient.getActorsTvCredits(actorId, BuildConfig.TMDB_API_KEY)
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