package com.krunal3kapadiya.popularmovies.dao

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.krunal3kapadiya.popularmovies.data.model.Movies

/**
 * @author krunal kapadiya
 * @link https://krunal3kapadiya.app
 * @date 14,April,2019
 */

@Database(entities = [Movies::class], version = 1)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun moviesDao(): MoviesDao
    abstract fun tvShowsDao(): TvDao
    abstract fun actorsDao(): ActorsDao

    companion object {
        @Volatile
        private var INSTANCE: MoviesDatabase? = null

        /**
         * fetching single instance of movies database
         */
        fun getInstance(context: Context): MoviesDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
        }

        /**
         * creating database
         */
        private fun buildDatabase(context: Context) = Room.databaseBuilder(
                context.applicationContext,
                MoviesDatabase::class.java,
                "movies.db")
                .build()
    }
}