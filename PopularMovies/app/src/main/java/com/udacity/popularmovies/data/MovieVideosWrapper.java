package com.udacity.popularmovies.data;

import com.google.gson.annotations.SerializedName;
import com.udacity.popularmovies.entity.MovieVideo;

import java.util.List;

public class MovieVideosWrapper {

    @SerializedName("results")
    private List<MovieVideo> movieVideoList;

    public List<MovieVideo> getMovieVideoList() {
        return movieVideoList;
    }

    public void setMovieVideoList(List<MovieVideo> movieVideoList) {
        this.movieVideoList = movieVideoList;
    }
}
