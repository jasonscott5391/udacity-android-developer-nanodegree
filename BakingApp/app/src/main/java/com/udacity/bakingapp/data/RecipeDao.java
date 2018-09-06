package com.udacity.bakingapp.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.udacity.bakingapp.entity.Ingredient;
import com.udacity.bakingapp.entity.Recipe;
import com.udacity.bakingapp.entity.RecipeWrapper;
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
            + Recipe.RECIPES_TABLE_NAME
            + " WHERE " + Recipe.COLUMN_RECIPE_ID + " = :recipeId")
    @Transaction
    RecipeWrapper getRecipeById(long recipeId);

    @Query("SELECT * FROM "
            + Recipe.RECIPES_TABLE_NAME)
    @Transaction
    List<RecipeWrapper> getRecipes();
}
