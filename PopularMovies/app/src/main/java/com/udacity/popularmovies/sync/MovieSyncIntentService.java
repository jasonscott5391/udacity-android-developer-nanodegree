package com.udacity.popularmovies.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.udacity.popularmovies.data.MovieDatabase;

public class MovieSyncIntentService extends IntentService {

    public static final String PREFERENCE_KEY = "preference";
    private static final String MOVIE_SYNC_INTENT_SERVICE = "MovieSyncIntentService";

    public MovieSyncIntentService() {
        super(MOVIE_SYNC_INTENT_SERVICE);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        assert intent != null;

        MovieDatabase movieDatabase = MovieDatabase.getInstance(this.getApplication());

        String preference = intent.getStringExtra(PREFERENCE_KEY);
        switch (preference) {
            case MovieSyncTask.POPULAR_MOVIES:
                MovieSyncTask.syncPopularMovies(movieDatabase.movies());
                break;

            case MovieSyncTask.TOP_RATED_MOVIES:
                MovieSyncTask.syncTopRatedMovies(movieDatabase.movies());
                break;
            default:
                throw new UnsupportedOperationException(String.format("Operation %s is unsupported!", preference));
        }
    }
}
