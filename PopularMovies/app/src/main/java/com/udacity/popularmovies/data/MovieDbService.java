package com.udacity.popularmovies.data;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieDbService {

    @GET("/3/movie/{sort}")
    Call<PopularMoviesWrapper> getPopularMovies(@Path("sort") String sortOrder, @Query("api_key") String apiKey);
}
