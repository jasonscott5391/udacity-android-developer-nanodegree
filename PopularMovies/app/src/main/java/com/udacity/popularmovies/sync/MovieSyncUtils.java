package com.udacity.popularmovies.sync;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import static com.udacity.popularmovies.sync.MovieSyncIntentService.PREFERENCE_KEY;

public class MovieSyncUtils {
    public static void startImmediateSync(@NonNull final Context context, String preference) {
        Intent intentToSyncImmediately = new Intent(context, MovieSyncIntentService.class);
        intentToSyncImmediately.putExtra(PREFERENCE_KEY, preference);
        context.startService(intentToSyncImmediately);
    }
}
