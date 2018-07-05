/*
Copyright 2018 Krunal Kapadiya

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/


package com.krunal3kapadiya.popularmovies.data.api

import com.krunal3kapadiya.popularmovies.dashBoard.actors.ActorsDetailResponse
import com.krunal3kapadiya.popularmovies.dashBoard.actors.ActorsResponse
import com.krunal3kapadiya.popularmovies.data.model.*
import com.krunal3kapadiya.popularmovies.genres.GenresListResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Krunal on 27/06/18.
 *
 * @author <a href="https://github.com/krunal3kapadiya">krunal3kapadiya</a>
 */
internal interface MovieApi {

    /*---
    Movies List
    ---*/
    @GET("movie/top_rated")
    fun getTopRatedMovies(@Query("api_key") apiKey: String): Observable<MovieResponse>

    @GET("movie/popular")
    fun getPopularMoviesList(@Query("api_key") apiKey: String): Observable<MovieResponse>

    @GET("movie/{id}/videos")
    fun getMovieTrailers(@Path("id") id: Long, @Query("api_key") apiKey: String): Observable<TrailerResponse>

    @GET("movie/{id}/reviews")
    fun getMovieReviews(@Path("id") id: Long, @Query("api_key") apiKey: String): Observable<ReviewsResponse>

    @GET("movie/now_playing")
    fun getNowPlayingMovies(@Query("api_key") apiKey: String): Observable<MovieResponse>

    @GET("search/movie")
    fun searchMovie(@Query("api_key") apiKey: String,
                    @Query("query") query: String,
                    @Query("page") page: Int,
                    @Query("include_adult") includeAdult: Boolean): Observable<SearchResponse>

    @GET("movie/upcoming")
    fun getUpComingMovies(@Query("api_key") apiKey: String): Observable<MovieResponse>

    /*---
    TV List
    ---*/
    @GET("tv/on_the_air")
    fun tvOnAir(@Query("api_key") apiKey: String): Observable<MovieResponse>

    @GET("tv/airing_today")
    fun tvAiringToday(@Query("api_key") apiKey: String): Observable<MovieResponse>

    @GET("tv/popular")
    fun tvPopular(@Query("api_key") apiKey: String): Observable<MovieResponse>

    @GET("tv/top_rated")
    fun tvTopRated(@Query("api_key") apiKey: String): Observable<MovieResponse>

    @GET("movie/{id}/casts")
    fun getCastList(@Path("id") id: Long, @Query("api_key") apiKey: String): Observable<CastResponse>

    fun getMovieByYear(apI_KEY: String): Observable<MovieResponse>

    @GET("genre/tv/list")
    fun getGenresList(@Query("api_key") apiKey: String): Observable<GenresListResponse>

    @GET("person/popular")
    fun getActorsList(@Query("api_key") apiKey: String): Observable<ActorsResponse>

    @GET("person/{person_id}")
    fun getActorsDetails(
            @Path("person_id") personId: Int,
            @Query("api_key") apiKey: String
    ): Observable<ActorsDetailResponse>
}
