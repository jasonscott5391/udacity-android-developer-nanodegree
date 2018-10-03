package com.udacity.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import static com.udacity.bakingapp.IngredientWidgetService.ACTION_INGREDIENT_WIDGET;
import static com.udacity.bakingapp.MainActivity.INTENT_KEY_RECIPE_ID;
import static com.udacity.bakingapp.MainActivity.INTENT_KEY_RECIPE_NAME;

public class IngredientWidgetProvider extends AppWidgetProvider {

    public static void updateAppWidget(Context context,
                                AppWidgetManager appWidgetManager,
                                int[] appWidgetId,
                                long recipeId,
                                String recipeName,
                                String ingredientListString) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
        views.setTextViewText(R.id.ingredients_widget_recipe_name, recipeName);
        views.setTextViewText(R.id.ingredients_widget_recipe_ingredients, ingredientListString);

        Intent intent;
        if (recipeId == -1L) {
            intent = new Intent(context, MainActivity.class);
        } else {
            intent = new Intent(context, RecipeDetailActivity.class);
            intent.putExtra(INTENT_KEY_RECIPE_ID, recipeId);
            intent.putExtra(INTENT_KEY_RECIPE_NAME, recipeName);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.ingredients_widget_recipe_name, pendingIntent);
        views.setOnClickPendingIntent(R.id.ingredients_widget_recipe_ingredients, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Intent intent = new Intent(context, IngredientWidgetService.class);
        intent.setAction(ACTION_INGREDIENT_WIDGET);
        context.startService(intent);
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}

