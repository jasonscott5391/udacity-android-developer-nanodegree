package com.udacity.popularmovies.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.udacity.popularmovies.R;
import com.udacity.popularmovies.data.MovieDao;
import com.udacity.popularmovies.repository.MovieRepository;

import java.util.List;

public class MovieListViewModel extends AndroidViewModel {

    private static final String TAG = MovieListViewModel.class.getSimpleName();

    private MutableLiveData<List<MovieDao.BaseMovie>> mMoviesList;

    public MovieListViewModel(@NonNull Application application) {
        super(application);
        Context context = this.getApplication();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String sortKey = context.getString(R.string.pref_movie_sort_key);
        String defaultSort = context.getString(R.string.pref_movie_sort_popular);
        String sortPreference = sharedPreferences.getString(sortKey, defaultSort);

        // Initialize repository and get movies.
        MovieRepository.init(context, sortPreference);
        Log.d(TAG, "MovieListViewModel - Assigning movies from repository.");
        mMoviesList = MovieRepository.getMovies();
    }

    public MutableLiveData<List<MovieDao.BaseMovie>> getMoviesList() {
        Log.d(TAG, "getMoviesList - Actively retrieving movies.");
        return mMoviesList;
    }

}
