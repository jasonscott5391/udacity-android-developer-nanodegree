package com.udacity.bakingapp.sync;

import android.util.Log;

import com.udacity.bakingapp.data.RecipeDao;
import com.udacity.bakingapp.entity.Ingredient;
import com.udacity.bakingapp.entity.Recipe;
import com.udacity.bakingapp.entity.Step;
import com.udacity.bakingapp.repository.RecipeRepository;
import com.udacity.bakingapp.service.RecipeService;
import com.udacity.bakingapp.service.RecipeWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeSyncTask {

    private static final String TAG = RecipeSyncTask.class.getSimpleName();
    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net";
    private static final RecipeService sRecipeService = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RecipeService.class);

    static synchronized void syncRecipes(RecipeDao recipeDao) {

        Log.d(TAG, "syncRecipes - Preparing request for recipe");

        try {

            Response<List<RecipeWrapper>> response = sRecipeService.getRecipes().execute();
            Log.d(TAG, String.format("syncRecipes - Recipe response: code:%s, body:%s, errorBody:%s", response.code(), response.body(), response.errorBody()));

            List<RecipeWrapper> recipeWrapperList = response.body();
            if (recipeWrapperList == null) {
                throw new IOException("recipeWrapperList is null!");
            }

            List<Recipe> recipeList = new ArrayList<>();
            for (RecipeWrapper recipeWrapper : recipeWrapperList) {
                // Build Recipe from Wrapper.
                Recipe recipe = new Recipe();
                recipe.recipeId = recipeWrapper.recipeId;
                recipe.name = recipeWrapper.name;
                recipe.servings = recipeWrapper.servings;
                recipe.image = recipeWrapper.image;
                recipeList.add(recipe);

                Long recipeId = recipe.recipeId;

                // Assign recipe ID to each ingredient.
                List<Ingredient> ingredientList = recipeWrapper.ingredientList;
                recipe.stepCount = ingredientList.size();
                for (Ingredient ingredient : ingredientList) {
                    ingredient.recipeId = recipeId;
                }
                long[] recipeIngredientRowsInserted = recipeDao.insertIngredients(ingredientList);
                Log.d(TAG, String.format("syncRecipes - recipeIngredientRowsInserted.length: %s", recipeIngredientRowsInserted.length));
                Log.d(TAG, String.format("syncRecipes - recipeIngredientRowsInserted: %s", Arrays.toString(recipeIngredientRowsInserted)));

                // Assign recipe ID to each step.
                List<Step> stepList = recipeWrapper.stepList;
                for (Step step : stepList) {
                    step.recipeId = recipeId;
                }
                long[] recipeStepRowsInserted = recipeDao.insertSteps(stepList);
                Log.d(TAG, String.format("syncRecipes - recipeStepRowsInserted.length: %s", recipeStepRowsInserted.length));
                Log.d(TAG, String.format("syncRecipes - recipeStepRowsInserted: %s", Arrays.toString(recipeStepRowsInserted)));
            }

            long[] recipeRowsInserted = recipeDao.insertRecipes(recipeList);
            Log.d(TAG, String.format("syncRecipes - recipeRowsInserted.length: %s", recipeRowsInserted.length));
            Log.d(TAG, String.format("syncRecipes - recipeRowsInserted: %s", Arrays.toString(recipeRowsInserted)));

            RecipeRepository.getRecipeList().postValue(recipeDao.getRecipes());

        } catch (IOException e) {
            RecipeRepository.getRecipeList().postValue(null);
            Log.e(TAG, e.getMessage());
        }

    }
}
