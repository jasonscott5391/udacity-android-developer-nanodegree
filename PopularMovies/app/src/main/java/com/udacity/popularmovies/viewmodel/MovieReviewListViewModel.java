package com.udacity.popularmovies.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.udacity.popularmovies.entity.MovieReview;
import com.udacity.popularmovies.repository.MovieRepository;

import java.util.List;

public class MovieReviewListViewModel extends ViewModel {

    private MutableLiveData<List<MovieReview>> mMovieReviewList;

    public MovieReviewListViewModel(Context context, long movieId) {
        mMovieReviewList = MovieRepository.getMovieReviewByMovieId(context, movieId);
    }

    public MutableLiveData<List<MovieReview>> getMovieReviewList() {
        return mMovieReviewList;
    }
}
