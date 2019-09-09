package com.krunal3kapadiya.popularmovies.dashBoard.tvShows

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.krunal3kapadiya.popularmovies.BuildConfig
import com.krunal3kapadiya.popularmovies.base.BaseViewModel
import com.krunal3kapadiya.popularmovies.dao.TvDao
import com.krunal3kapadiya.popularmovies.data.api.MovieApi
import com.krunal3kapadiya.popularmovies.data.api.MovieApiClient
import com.krunal3kapadiya.popularmovies.data.model.Movies
import com.krunal3kapadiya.popularmovies.data.model.TvResult
import com.krunal3kapadiya.popularmovies.data.model.TVResponse
import com.krunal3kapadiya.popularmovies.data.model.tvDetail.TvDetailResponse
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TvViewModel(private val tvDao: TvDao) : BaseViewModel() {
    val errorMessage = MediatorLiveData<String>()
    val movieArrayList = MediatorLiveData<List<TvResult>>()
    val isLoading = MediatorLiveData<Boolean>()

    fun getPopularTvList(number: Int?, page: Int) {
        isLoading.postValue(true)
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
                    isLoading.postValue(false)
                    it.tvResults?.let { movieArrayList.postValue(it) }
                }) {
                    isLoading.postValue(false)
                    errorMessage.postValue(it.message)
                }
    }

    fun getTvDetail(tvId: Int): MediatorLiveData<TvDetailResponse> {
        val data = MediatorLiveData<TvDetailResponse>()
        val movieClient = MovieApiClient.client!!
                .create(MovieApi::class.java)
        disposable.add(movieClient.tvDetails(
                tv_id = tvId,
                apiKey = BuildConfig.TMDB_API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    data.postValue(it)
                }, {
                    errorMessage.postValue(it.message)
                }))
        return data
    }
    fun removeTvShow(id: Int) {
        disposable.add(removeFromFavourite(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe())
    }
    fun getTvById(id: Int): LiveData<TvResult> {
        return tvDao.getTvShowsById(id)
    }

    private fun removeFromFavourite(id: Int): Completable {
        return Completable.fromAction {
            tvDao.deleteTvById(id)
        }
    }

    fun addOrUpdateMovies(tvResult: TvResult): Completable {
        return Completable.fromAction {
            tvDao.insert(tvResult)
        }
    }

    fun addToFavourite(tvResult: TvResult) {
        disposable.add(addOrUpdateMovies(tvResult)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe())

    }
}