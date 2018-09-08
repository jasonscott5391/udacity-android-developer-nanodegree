package com.udacity.bakingapp.sync;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.util.Log;

import com.udacity.bakingapp.data.RecipeDatabase;

public class RecipeSyncIntentService extends IntentService {

    private static final String RECIPE_SYNC_INTENT_SERVICE = RecipeSyncIntentService.class.getSimpleName();
    private static final String TAG = RECIPE_SYNC_INTENT_SERVICE;

    public RecipeSyncIntentService() {
        super(RECIPE_SYNC_INTENT_SERVICE);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        assert intent != null;


        Log.d(TAG, String.format("onHandleIntent - intent:%s", intent));

        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }

        if (activeNetwork == null ||
                !activeNetwork.isConnectedOrConnecting()) {
            return;
        }

        RecipeDatabase recipeDatabase = RecipeDatabase.getInstance(this.getApplication());
        RecipeSyncTask.syncRecipes(recipeDatabase.recipes());
    }
}
