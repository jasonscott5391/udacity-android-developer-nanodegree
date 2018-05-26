package com.udacity.popularmovies.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.udacity.popularmovies.data.MovieDao;
import com.udacity.popularmovies.data.MovieDatabase;
import com.udacity.popularmovies.repository.MovieRepository;

import java.util.List;

public class MoviesViewModel extends AndroidViewModel {

    private LiveData<List<MovieDao.BaseMovie>> mMoviesList;

    public MoviesViewModel(@NonNull Application application) {
        super(application);
        MovieDatabase movieDatabase = MovieDatabase.getInstance(this.getApplication());
        // TODO (jasonscott) Get preferences here
        String preference = "popular";
//        String preference = "top_rated";
        mMoviesList = MovieRepository.getMovies(this.getApplication(),
                movieDatabase.movies(),
                preference);
    }

    public LiveData<List<MovieDao.BaseMovie>> getMoviesList() {
        return mMoviesList;
    }

}
