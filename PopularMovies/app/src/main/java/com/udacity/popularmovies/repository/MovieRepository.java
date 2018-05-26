package com.udacity.popularmovies.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.udacity.popularmovies.data.MovieDao;
import com.udacity.popularmovies.sync.MovieSyncUtils;

import java.util.List;

import static com.udacity.popularmovies.sync.MovieSyncTask.POPULAR_MOVIES;
import static com.udacity.popularmovies.sync.MovieSyncTask.TOP_RATED_MOVIES;

public class MovieRepository {

    public static LiveData<List<MovieDao.BaseMovie>> getMovies(Context context, MovieDao movieDao, String preference) {

        MovieSyncUtils.startImmediateSync(context, preference);

        switch (preference) {
            case POPULAR_MOVIES:
                return movieDao.getPopularMovies();

            case TOP_RATED_MOVIES:
                return movieDao.getTopRatedMovies();

            default:
                throw new UnsupportedOperationException(String.format("Operation %s is unsupported!", preference));
        }
    }
}
