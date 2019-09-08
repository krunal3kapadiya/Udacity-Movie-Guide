package com.krunal3kapadiya.popularmovies.dao

import android.arch.persistence.room.Dao

/**
 * @author krunal kapadiya
 * @link https://krunal3kapadiya.app
 * @date 14,April,2019
 */
@Dao
interface TvDao {
//    /**
//     * insertion
//     */
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insert(tvShows: com.krunal3kapadiya.popularmovies.data.model.TvResult)
//
//    /**
//     * fetch all tvShows
//     */
//    @Query("SELECT * FROM tv")
//    fun getAll(): LiveData<List<com.krunal3kapadiya.popularmovies.data.model.TvResult>>
//
//    /**
//     * fetch comment by ID
//     */
//    @Query("SELECT * FROM tv WHERE id = :id")
//    fun getTvShowsById(id: Int): LiveData<com.krunal3kapadiya.popularmovies.data.model.TvResult>
}
