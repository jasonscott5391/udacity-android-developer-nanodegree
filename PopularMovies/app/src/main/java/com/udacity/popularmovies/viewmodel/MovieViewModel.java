package com.udacity.popularmovies.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.udacity.popularmovies.R;
import com.udacity.popularmovies.data.Movie;
import com.udacity.popularmovies.repository.MovieRepository;

public class MovieViewModel extends AndroidViewModel {

    public MovieViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Movie> getMovie(long id) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplication());
        String sortKey = this.getApplication().getString(R.string.pref_movie_sort_key);
        String defaultSort = this.getApplication().getString(R.string.pref_movie_sort_popular);
        String sortPreference = sharedPreferences.getString(sortKey, defaultSort);

        return MovieRepository.getMovieById(this.getApplication(), id, sortPreference);
    }
}
