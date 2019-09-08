package com.krunal3kapadiya.popularmovies.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.krunal3kapadiya.popularmovies.data.model.Movies

/**
 * @author krunal kapadiya
 * @link https://krunal3kapadiya.app
 * @date 14,April,2019
 */

@Dao
interface MoviesDao {
    /**
     * insertion
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movies: Movies)

    /**
     * fetch all moviess
     */
    @Query("SELECT * FROM movies")
    fun getAll(): LiveData<List<Movies>>

    /**
     * fetch comment by ID
     */
    @Query("SELECT * FROM movies WHERE id = :id")
    fun getMoviesById(id: Int): LiveData<Movies>

    /**
     * fetch comment by ID
     */
    @Query("DELETE FROM movies WHERE id = :id")
    fun deleteMoviesById(id: Int)
}