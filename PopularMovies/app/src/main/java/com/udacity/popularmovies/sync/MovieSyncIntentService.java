package com.udacity.popularmovies.sync;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.util.Log;

import com.udacity.popularmovies.data.MovieDatabase;

public class MovieSyncIntentService extends IntentService {

    public static final String PREFERENCE_KEY = "preference";
    private static final String MOVIE_SYNC_INTENT_SERVICE = MovieSyncIntentService.class.getSimpleName();
    private static final String TAG = MovieSyncIntentService.class.getSimpleName();

    public MovieSyncIntentService() {
        super(MOVIE_SYNC_INTENT_SERVICE);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        assert intent != null;

        MovieDatabase movieDatabase = MovieDatabase.getInstance(this.getApplication());

        String preference = intent.getStringExtra(PREFERENCE_KEY);

        Log.d(TAG, String.format("onHandleIntent - intent:%s, preference:%s", intent, preference));

        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (!isConnected) {
            return;
        }

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
