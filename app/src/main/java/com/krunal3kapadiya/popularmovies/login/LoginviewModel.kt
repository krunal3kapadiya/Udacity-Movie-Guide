package com.krunal3kapadiya.popularmovies.login

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.krunal3kapadiya.popularmovies.BuildConfig
import com.krunal3kapadiya.popularmovies.data.api.MovieApi
import com.krunal3kapadiya.popularmovies.data.api.MovieApiClient
import com.krunal3kapadiya.popularmovies.data.model.RequestTokenResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


/**
 * @author krunal kapadiya
 * @link https://krunal3kapadiya.app
 * @date 14,April,2019
 */

class LoginviewModel : ViewModel() {

    var compositeDisposable = CompositeDisposable()

    fun getRequestToken(): MediatorLiveData<String> {
        val requestToken = MediatorLiveData<String>()
        val movieClient = MovieApiClient.client!!
                .create(MovieApi::class.java)

        /*val disposable = */movieClient.createRequestToken(BuildConfig.TMDB_API_KEY)
                .subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({
                    if (it.success) {
                        it.requestToken.let { it1 ->
                            requestToken.postValue(it1)
                        }
                    }

                }) {
                    Log.e("MovieResponseException", it.message)
                }
//        disposable?.let { compositeDisposable.add(it) }
//        compositeDisposable.dispose()
        return requestToken
    }

    fun apiCallForLogin(username: String, password: String) {

    }
}