package com.udacity.popularmovies.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.udacity.popularmovies.data.PopularMovie;
import com.udacity.popularmovies.data.PopularMovieDatabase;

import java.util.List;

public class PopularMoviesViewModel extends AndroidViewModel {

    private final LiveData<List<PopularMovie>> mPopularMovieList;

    private PopularMovieDatabase mPopularMovieDatabase;

    public PopularMoviesViewModel(@NonNull Application application) {
        super(application);

        mPopularMovieDatabase = PopularMovieDatabase.getInstance(this.getApplication());

        mPopularMovieList = mPopularMovieDatabase.popularMovie().selectAll();
    }

    public LiveData<List<PopularMovie>> getPopularMovieList() {
        return mPopularMovieList;
    }
}
