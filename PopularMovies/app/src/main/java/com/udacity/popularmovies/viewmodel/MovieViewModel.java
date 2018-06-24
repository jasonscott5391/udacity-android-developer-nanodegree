package com.udacity.popularmovies.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.udacity.popularmovies.entity.Movie;
import com.udacity.popularmovies.repository.MovieRepository;

public class MovieViewModel extends AndroidViewModel {

    public MovieViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Movie> getMovie(long id) {
        return MovieRepository.getMovieById(this.getApplication(), id);
    }
}
