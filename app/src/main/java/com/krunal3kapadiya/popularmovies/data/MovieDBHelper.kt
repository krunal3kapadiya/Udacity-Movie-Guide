package com.krunal3kapadiya.popularmovies.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MovieDBHelper(context: Context) : SQLiteOpenHelper(context, DATABSE_NAME, null, DATABASE_VERSION) {

    //create database
    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_MOVIE_TABLE = "CREATE TABLE " +
                MovieContract.MovieEntry.TABLE_NAME + "(" +
                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER UNIQUE NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_BACKDROP + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_RATINGS + " INTEGER NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL," +
                MovieContract.MovieEntry.COLUMN_TRAILER + " TEXT," +
                MovieContract.MovieEntry.COLUMN_REVIEWS + " TEXT" +
                ");"
        db.execSQL(CREATE_MOVIE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // drop the table

        db.execSQL("DROP IF TABLE EXISTS " + MovieContract.MovieEntry.TABLE_NAME)
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '"
                + MovieContract.MovieEntry.TABLE_NAME + "'")

        //re-create database
        onCreate(db)
    }

    companion object {
        // name and version
        private val DATABSE_NAME = "movie.db"
        private val DATABASE_VERSION = 1
    }
}
