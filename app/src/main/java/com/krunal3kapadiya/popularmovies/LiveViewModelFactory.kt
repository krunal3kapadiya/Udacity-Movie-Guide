package com.krunal3kapadiya.popularmovies

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.krunal3kapadiya.popularmovies.dao.ActorsDao
import com.krunal3kapadiya.popularmovies.dao.MoviesDao
import com.krunal3kapadiya.popularmovies.dao.TvDao
import com.krunal3kapadiya.popularmovies.dashBoard.movies.MoviesViewModel
import com.krunal3kapadiya.popularmovies.dashBoard.tvShows.TvViewModel
import com.krunal3kapadiya.popularmovies.favourites.FavouriteViewModel

/**
 * @author krunal kapadiya
 * @link https://krunal3kapadiya.app
 * @date 14,April,2019
 */

class ListViewModelFactory(private val dataSource: Any,
                           private val secondDataSource: Any?,
                           private val thirdDataSource: Any?) :
        ViewModelProvider.Factory {

    constructor(dataSource: Any) : this(dataSource, null, null)

    /**
     * providing list view model
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoviesViewModel::class.java)) {
            return MoviesViewModel(dataSource as MoviesDao) as T
        } else if (modelClass.isAssignableFrom(FavouriteViewModel::class.java)) {
            return FavouriteViewModel(dataSource as MoviesDao,
                    secondDataSource as TvDao,
                    thirdDataSource as ActorsDao) as T
        }else if (modelClass.isAssignableFrom(TvViewModel::class.java)) {
            return TvViewModel(dataSource as TvDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}