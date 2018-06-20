package com.krunal3kapadiya.popularmovies.data.api;


import com.krunal3kapadiya.popularmovies.data.model.MovieResponse;
import com.krunal3kapadiya.popularmovies.data.model.ReviewsResponse;
import com.krunal3kapadiya.popularmovies.data.model.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Krunal on 8/5/2017.
 */

public interface MovieApiInterface {

    @GET("movie/popular")
    Call<MovieResponse> getPopularMoviesList(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<TrailerResponse> getMovieTrailers(@Path("id") long id, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<ReviewsResponse> getMovieReviews(@Path("id") long id, @Query("api_key") String apiKey);
}
