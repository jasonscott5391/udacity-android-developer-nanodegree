package com.udacity.bakingapp.repository;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.udacity.bakingapp.data.RecipeDatabase;
import com.udacity.bakingapp.entity.Ingredient;
import com.udacity.bakingapp.entity.Recipe;
import com.udacity.bakingapp.entity.Step;
import com.udacity.bakingapp.sync.RecipeSyncUtils;

import java.util.List;

public class RecipeRepository {

    private static final String TAG = RecipeRepository.class.getSimpleName();

    private static boolean sInitialized;
    private static MutableLiveData<List<Recipe>> sRecipeList;
    private static MutableLiveData<List<Ingredient>> sIngredientList;
    private static MutableLiveData<List<Step>> sStepList;

    public static void init(@NonNull final Context context) {
        Log.d(TAG, "init()");
        if (sInitialized) {
            return;
        }

        sInitialized = true;
        sRecipeList = new MutableLiveData<>();
        sIngredientList = new MutableLiveData<>();
        sStepList = new MutableLiveData<>();

        RecipeSyncUtils.startImmediateSync(context);
    }

    public static MutableLiveData<List<Recipe>> getRecipeList() {
        return sRecipeList;
    }

    public static void updateRecipes(@NonNull final Context context) {
        Log.d(TAG, "updateRecipes()");
        RecipeSyncUtils.startImmediateSync(context);
    }

    public static MutableLiveData<List<Ingredient>> getIngredientList(@NonNull final Context context, long recipeId) {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                RecipeDatabase recipeDatabase = RecipeDatabase.getInstance(context);
                sIngredientList.postValue(recipeDatabase.recipes().getIngredients(recipeId));
                return null;
            }
        }.execute();

        return sIngredientList;
    }

    public static MutableLiveData<List<Step>> getStepList(@NonNull final Context context, long recipeId) {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                RecipeDatabase recipeDatabase = RecipeDatabase.getInstance(context);
                sStepList.postValue(recipeDatabase.recipes().getSteps(recipeId));
                return null;
            }
        }.execute();

        return sStepList;
    }

}
