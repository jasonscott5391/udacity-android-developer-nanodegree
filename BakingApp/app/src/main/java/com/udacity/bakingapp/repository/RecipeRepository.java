package com.udacity.bakingapp.repository;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.udacity.bakingapp.entity.RecipeWrapper;
import com.udacity.bakingapp.sync.RecipeSyncUtils;

import java.util.List;

public class RecipeRepository {

    private static final String TAG = RecipeRepository.class.getSimpleName();

    private static boolean sInitialized;
    private static MutableLiveData<List<RecipeWrapper>> sRecipeList;


    public static void init(@NonNull final Context context) {
        Log.d(TAG, "init()");
        if (sInitialized) {
            return;
        }

        sInitialized = true;
        sRecipeList = new MutableLiveData<>();

        RecipeSyncUtils.startImmediateSync(context);
    }

    public static MutableLiveData<List<RecipeWrapper>> getRecipeList() {
        return sRecipeList;
    }

    public static void updateRecipes(@NonNull final Context context) {
        Log.d(TAG, "updateRecipes()");
        RecipeSyncUtils.startImmediateSync(context);
    }
}
