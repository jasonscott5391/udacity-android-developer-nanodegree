package com.udacity.popularmovies.service;


import com.udacity.popularmovies.data.MovieReviewsWrapper;
import com.udacity.popularmovies.data.MovieVideosWrapper;
import com.udacity.popularmovies.data.MoviesWrapper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieDbService {

    @GET("/3/movie/{sort}")
    Call<MoviesWrapper> getMovies(@Path("sort") String sortOrder, @Query("api_key") String apiKey, @Query("page") int page);

    @GET("/3/movie/{movie_id}/videos")
    Call<MovieVideosWrapper> getMovieVideos(@Path("movie_id") long movieId, @Query("api_key") String apiKey);

    @GET("/3/movie/{movie_id}/reviews")
    Call<MovieReviewsWrapper> getMovieReviews(@Path("movie_id") long movieId, @Query("api_key") String apiKey);
}
