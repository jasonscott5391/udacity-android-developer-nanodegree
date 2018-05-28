package com.udacity.popularmovies.repository;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.udacity.popularmovies.data.MovieDao;
import com.udacity.popularmovies.sync.MovieSyncUtils;

import java.util.List;

public class MovieRepository {
    private static final String TAG = MovieRepository.class.getSimpleName();
    private static MutableLiveData<List<MovieDao.BaseMovie>> sMovieList;

    public static void init(Context context, String preference) {
        Log.d(TAG, String.format("getMovies() - preference:%s", preference));
        sMovieList = new MutableLiveData<>();
        MovieSyncUtils.startImmediateSync(context, preference);
    }

    public static MutableLiveData<List<MovieDao.BaseMovie>> getMovies() {
        return sMovieList;
    }

    public static void updateMovies(Context context, String preference) {
        Log.d(TAG, String.format("getMovies() - preference:%s", preference));
        MovieSyncUtils.startImmediateSync(context, preference);
    }
}
