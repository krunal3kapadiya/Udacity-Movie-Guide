package com.krunal3kapadiya.popularmovies

import android.content.Context
import com.krunal3kapadiya.popularmovies.dao.ActorsDao
import com.krunal3kapadiya.popularmovies.dao.MoviesDao
import com.krunal3kapadiya.popularmovies.dao.MoviesDatabase
import com.krunal3kapadiya.popularmovies.dao.TvDao

object Injection {
    /**
     * list view model
     */
    @JvmStatic
    fun provideMoviesViewModel(context: Context): ListViewModelFactory {
        val moviesDao = provideMoviesDataSource(context)
        return ListViewModelFactory(moviesDao)
    }

    @JvmStatic
    fun provideTvViewModel(context: Context): ListViewModelFactory {
        val tvDao = provideTvDataSource(context)
        return ListViewModelFactory(tvDao)
    }

    /**
     * post data source
     */
    fun provideMoviesDataSource(context: Context): MoviesDao {
        val database = MoviesDatabase.getInstance(context)
        return database.moviesDao()
    }


    @JvmStatic
    fun provideFavouriteViewModel(context: Context): ListViewModelFactory {
        val moviesDao = provideMoviesDataSource(context)
        val tvDao = provideTvDataSource(context)
        val actorDao = provideActorDataSource(context)
        return ListViewModelFactory(moviesDao, tvDao, actorDao)
    }

    /**
     * post data source
     */
    fun provideTvDataSource(context: Context): TvDao {
        val database = MoviesDatabase.getInstance(context)
        return database.tvShowsDao()
    }

    fun provideActorDataSource(context: Context): ActorsDao {
        val database = MoviesDatabase.getInstance(context)
        return database.actorsDao()
    }
}
