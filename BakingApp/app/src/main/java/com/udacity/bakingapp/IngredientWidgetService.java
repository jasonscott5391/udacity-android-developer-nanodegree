package com.udacity.bakingapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.udacity.bakingapp.data.RecipeDatabase;
import com.udacity.bakingapp.entity.Ingredient;

import java.util.List;

import static com.udacity.bakingapp.MainActivity.INTENT_KEY_RECIPE_ID;
import static com.udacity.bakingapp.MainActivity.INTENT_KEY_RECIPE_NAME;

public class IngredientWidgetService extends IntentService {

    protected static final String ACTION_INGREDIENT_WIDGET = "action_ingredient_widget";

    public IngredientWidgetService(String name) {
        super(name);
    }

    public IngredientWidgetService() {
        super(IngredientWidgetService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null
                && intent.getAction().equalsIgnoreCase(ACTION_INGREDIENT_WIDGET)) {

            SharedPreferences sharedPreferences = getSharedPreferences(IngredientWidgetService.class.getSimpleName(), MODE_PRIVATE);
            long recipeId = sharedPreferences.getLong(INTENT_KEY_RECIPE_ID, -1L);
            String recipeName = sharedPreferences.getString(INTENT_KEY_RECIPE_NAME, getString(R.string.app_name));

            StringBuilder ingredientListStringBuilder = new StringBuilder();
            if (recipeId == -1) {
                // Set string to open a recipe in order to pin it's ingredients here.
                ingredientListStringBuilder.append("Please open a recipe in order to pin it's ingredient here!");
            } else {
                // Query database for ingredients list.
                RecipeDatabase recipeDatabase = RecipeDatabase.getInstance(getApplicationContext());
                List<Ingredient> ingredientList = recipeDatabase.recipes().getIngredients(recipeId);

                for (Ingredient ingredient : ingredientList) {
                    if (ingredientList.indexOf(ingredient) == ingredientList.size() - 1) {
                        ingredientListStringBuilder.append(String.format("- %s %s %s", ingredient.quantity, ingredient.measure, ingredient.ingredient));
                    } else {
                        ingredientListStringBuilder.append(String.format("- %s %s %s\n", ingredient.quantity, ingredient.measure, ingredient.ingredient));
                    }
                }

            }

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientWidgetProvider.class));
            IngredientWidgetProvider.updateAppWidget(this,
                    appWidgetManager,
                    appWidgetIds,
                    recipeId,
                    recipeName,
                    ingredientListStringBuilder.toString());
        }
    }
}
