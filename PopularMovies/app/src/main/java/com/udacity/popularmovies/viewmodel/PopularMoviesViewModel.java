package com.udacity.popularmovies.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.udacity.popularmovies.data.PopularMovie;
import com.udacity.popularmovies.data.PopularMovieDatabase;
import com.udacity.popularmovies.data.PopularMoviesRepository;

import java.util.List;

public class PopularMoviesViewModel extends AndroidViewModel {

    private LiveData<List<PopularMovie>> mPopularMovieList;
    private PopularMoviesRepository mPopularMoviesRepository;

    public PopularMoviesViewModel(@NonNull Application application) {
        super(application);
        PopularMovieDatabase popularMovieDatabase = PopularMovieDatabase.getInstance(this.getApplication());
        mPopularMoviesRepository = PopularMoviesRepository.getInstance(popularMovieDatabase.popularMovie());
        mPopularMovieList = mPopularMoviesRepository.getPopularMovies();
    }

    public LiveData<List<PopularMovie>> getPopularMovieList() {
        return mPopularMovieList;
    }

}
