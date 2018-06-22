package com.krunal3kapadiya.popularmovies.data.api

import com.krunal3kapadiya.popularmovies.data.model.CastResponse
import com.krunal3kapadiya.popularmovies.data.model.MovieResponse
import com.krunal3kapadiya.popularmovies.data.model.ReviewsResponse
import com.krunal3kapadiya.popularmovies.data.model.TrailerResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiInterface {

    @GET("movie/popular")
    fun getPopularMoviesList(@Query("api_key") apiKey: String): Call<MovieResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovies(@Query("api_key") apiKey: String): Call<MovieResponse>


    @GET("movie/now_playing")
    fun getNowPlayingMovies(@Query("api_key") apiKey: String): Call<MovieResponse>

    @GET("movie/{id}/videos")
    fun getMovieTrailers(@Path("id") id: Long, @Query("api_key") apiKey: String): Call<TrailerResponse>

    @GET("movie/{id}/reviews")
    fun getMovieReviews(@Path("id") id: Long, @Query("api_key") apiKey: String): Call<ReviewsResponse>

    @GET("movie/{id}/casts")
    fun getCastList(@Path("id") id: Long, @Query("api_key") apiKey: String): Call<CastResponse>

    @GET("movie/upcoming")
    fun getUpComingMovies(@Query("api_key") apiKey: String): Call<MovieResponse>

    fun getMovieByYear(apI_KEY: String): Call<MovieResponse>
}
