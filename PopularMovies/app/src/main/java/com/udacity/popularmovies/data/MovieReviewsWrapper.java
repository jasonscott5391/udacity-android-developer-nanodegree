package com.udacity.popularmovies.data;

import com.google.gson.annotations.SerializedName;
import com.udacity.popularmovies.entity.MovieReview;

import java.util.List;

public class MovieReviewsWrapper {

    @SerializedName("results")
    private List<MovieReview> movieReviewList;

    public List<MovieReview> getMovieReviewList() {
        return movieReviewList;
    }

    public void setMovieReviewList(List<MovieReview> movieReviewList) {
        this.movieReviewList = movieReviewList;
    }
}
