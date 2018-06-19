package com.udacity.popularmovies.sync;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.udacity.popularmovies.data.MovieDatabase;

import static com.udacity.popularmovies.sync.MovieSyncIntentService.PREFERENCE_KEY;

public class MovieSyncUtils {

    private static final String TAG = MovieSyncUtils.class.getSimpleName();

    public static void startImmediateSync(@NonNull final Context context, String preference) {
        Log.d(TAG, String.format("startImmediateSync - preference:%s", preference));

        Intent intentToSyncImmediately = new Intent(context, MovieSyncIntentService.class);
        intentToSyncImmediately.putExtra(PREFERENCE_KEY, preference);
        context.startService(intentToSyncImmediately);
    }

    public static void syncMovieVideos(Context context, long movieId) {
        MovieDatabase movieDatabase = MovieDatabase.getInstance(context);
        MovieSyncTask.syncMovieVideos(movieDatabase.movies(), movieId);
    }
}
