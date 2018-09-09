package com.udacity.bakingapp.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.udacity.bakingapp.entity.Ingredient;
import com.udacity.bakingapp.entity.Recipe;
import com.udacity.bakingapp.entity.Step;

import java.util.List;

@Dao
public interface RecipeDao {

    @Query("SELECT COUNT(*) FROM " + Recipe.RECIPES_TABLE_NAME)
    int getRecipeCount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertRecipes(List<Recipe> recipeList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertIngredients(List<Ingredient> ingredientList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertSteps(List<Step> stepList);

    @Query("SELECT * FROM "
            + Recipe.RECIPES_TABLE_NAME)
    @Transaction
    List<Recipe> getRecipes();

    @Query("SELECT * FROM "
            + Ingredient.INGREDIENTS_TABLE_NAME
            + " WHERE " + Recipe.COLUMN_RECIPE_ID + " = :recipeId")
    List<Ingredient> getIngredients(long recipeId);

    @Query("SELECT * FROM "
            + Step.STEPS_TABLE_NAME
            + " WHERE " + Recipe.COLUMN_RECIPE_ID + " = :recipeId")
    List<Step> getSteps(long recipeId);
}
