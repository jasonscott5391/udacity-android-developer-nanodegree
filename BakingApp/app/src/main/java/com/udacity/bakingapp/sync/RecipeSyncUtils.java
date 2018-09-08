package com.udacity.bakingapp.sync;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

public class RecipeSyncUtils {

    private static final String TAG = RecipeSyncUtils.class.getSimpleName();

    public static void startImmediateSync(@NonNull final Context context) {
        Log.d(TAG, "startImmediateSync()");
        Intent intentToSyncImmediately = new Intent(context, RecipeSyncIntentService.class);
        context.startService(intentToSyncImmediately);
    }
}
