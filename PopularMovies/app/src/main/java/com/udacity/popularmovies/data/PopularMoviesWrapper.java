package com.udacity.popularmovies.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PopularMoviesWrapper {

    @SerializedName("results")
    private List<PopularMovie> popularMovieList;

    public List<PopularMovie> getPopularMovieList() {
        return popularMovieList;
    }

    public void setPopularMovieList(List<PopularMovie> popularMovieList) {
        this.popularMovieList = popularMovieList;
    }
}
