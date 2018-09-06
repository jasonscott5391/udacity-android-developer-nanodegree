package com.udacity.bakingapp.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.udacity.bakingapp.entity.Ingredient;
import com.udacity.bakingapp.entity.Recipe;
import com.udacity.bakingapp.entity.Step;

@Database(entities = {Recipe.class,
        Ingredient.class,
        Step.class}, version = 1, exportSchema = false)
public abstract class RecipeDatabase extends RoomDatabase {

    public abstract RecipeDao recipes();

    private static final String RECIPE_DATABASE_NAME = "recipes";

    private static RecipeDatabase sRecipeDatabase;

    public static synchronized RecipeDatabase getInstance(Context context) {

        if (sRecipeDatabase == null) {
            sRecipeDatabase = Room.databaseBuilder(context.getApplicationContext(),
                    RecipeDatabase.class,
                    RECIPE_DATABASE_NAME)
                    .build();
        }

        return sRecipeDatabase;

    }
}
