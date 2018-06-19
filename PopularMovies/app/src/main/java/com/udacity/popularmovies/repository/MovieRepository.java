package com.udacity.popularmovies.repository;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.udacity.popularmovies.entity.Movie;
import com.udacity.popularmovies.data.MovieDao;
import com.udacity.popularmovies.data.MovieDatabase;
import com.udacity.popularmovies.entity.MovieReview;
import com.udacity.popularmovies.entity.MovieVideo;
import com.udacity.popularmovies.sync.MovieSyncTask;
import com.udacity.popularmovies.sync.MovieSyncUtils;

import java.util.List;

public class MovieRepository {
    private static final String TAG = MovieRepository.class.getSimpleName();
    private static MutableLiveData<List<MovieDao.BaseMovie>> sMovieList;
    private static MutableLiveData<Movie> sMovie;
    private static MutableLiveData<List<MovieVideo>> sMovieVideoList;
    private static MutableLiveData<List<MovieReview>> sMovieReviewList;
    private static boolean sInitialized;

    public static void init(@NonNull final Context context, String preference) {

        if (sInitialized) {
            return;
        }

        sInitialized = true;

        Log.d(TAG, String.format("init() - preference:%s", preference));
        sMovieList = new MutableLiveData<>();
        sMovie = new MutableLiveData<>();
        sMovieVideoList = new MutableLiveData<>();
        sMovieReviewList = new MutableLiveData<>();
        MovieSyncUtils.startImmediateSync(context, preference);
    }

    public static MutableLiveData<List<MovieDao.BaseMovie>> getMovies() {
        return sMovieList;
    }

    public static MutableLiveData<List<MovieVideo>> getMovieVideos() {
        return sMovieVideoList;
    }

    public static MutableLiveData<List<MovieReview>> getMovieReviews() {
        return sMovieReviewList;
    }

    public static void updateMovies(Context context, String preference) {
        Log.d(TAG, String.format("getMovies() - preference:%s", preference));
        MovieSyncUtils.startImmediateSync(context, preference);
    }

    public static MutableLiveData<Movie> getMovieById(Context context, long id, String preference) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                MovieDatabase movieDatabase = MovieDatabase.getInstance(context);
                switch (preference) {
                    case MovieSyncTask.POPULAR_MOVIES:
                        sMovie.postValue(movieDatabase.movies().getPopularMovieById(id));
                        break;

                    case MovieSyncTask.TOP_RATED_MOVIES:
                        sMovie.postValue(movieDatabase.movies().getTopRatedMovieById(id));
                        break;

                    default:
                        throw new UnsupportedOperationException(String.format("Operation %s is unsupported!", preference));
                }

                return null;
            }

        }.execute();

        return sMovie;
    }

    public static MutableLiveData<List<MovieVideo>> getMovieVideosByMovieId(Context context, long movieId) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                MovieSyncUtils.syncMovieVideos(context, movieId);
                return null;
            }

        }.execute();

        return sMovieVideoList;
    }

    public static MutableLiveData<List<MovieReview>> getMovieReviewByMovieId(Context context, long movieId) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                MovieSyncUtils.syncMovieReviews(context, movieId);
                return null;
            }

        }.execute();

        return sMovieReviewList;
    }
}
