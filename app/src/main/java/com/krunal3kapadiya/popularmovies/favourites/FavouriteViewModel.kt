package com.krunal3kapadiya.popularmovies.favourites

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import com.krunal3kapadiya.popularmovies.dao.ActorsDao
import com.krunal3kapadiya.popularmovies.dao.MoviesDao
import com.krunal3kapadiya.popularmovies.dao.TvDao
import com.krunal3kapadiya.popularmovies.data.model.Cast
import com.krunal3kapadiya.popularmovies.data.model.Movies
import io.reactivex.disposables.CompositeDisposable

/**
 * @author krunal kapadiya
 * @link https://krunal3kapadiya.app
 * @date 14,April,2019
 */

class FavouriteViewModel(private val moviesDao: MoviesDao,
                         private val tvDao: TvDao,
                         private val actorsDao: ActorsDao) : ViewModel() {

    val errorMessage = MediatorLiveData<String>()
    val movieArrayList = MediatorLiveData<ArrayList<Movies>>()
    val mCastArrayList = MediatorLiveData<List<Cast>>()
    val isLoading = MediatorLiveData<Boolean>()
    private val disposable: CompositeDisposable = CompositeDisposable()

    fun getAllMovies(): LiveData<List<Movies>> {
        return moviesDao.getAll()
    }
//    fun getAllTvShows(): LiveData<List<TvResult>> {
//        return tvDao.getAll()
//    }
//    fun getAllActors(): LiveData<List<com.krunal3kapadiya.popularmovies.data.model.actors.TvResult>> {
//        return actorsDao.getAll()
//    }

}