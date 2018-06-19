package com.udacity.popularmovies.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.udacity.popularmovies.entity.MovieVideo;
import com.udacity.popularmovies.repository.MovieRepository;

import java.util.List;

public class MovieVideoListViewModel extends ViewModel {

    private MutableLiveData<List<MovieVideo>> mMovieVideoList;

    public MovieVideoListViewModel(Context context, long movieId) {
        mMovieVideoList = MovieRepository.getMovieVideosByMovieId(context, movieId);
    }

    public MutableLiveData<List<MovieVideo>> getMovieVideoList() {
        return mMovieVideoList;
    }
}
