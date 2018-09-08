package com.udacity.bakingapp;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.udacity.bakingapp.data.RecipeDatabase;
import com.udacity.bakingapp.entity.Ingredient;
import com.udacity.bakingapp.entity.Recipe;
import com.udacity.bakingapp.entity.Step;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class RecipeDatabaseTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private static final int TEST_NUM_RECIPES = 15;

    private RecipeDatabase mRecipeDatabase;

    @Before
    public void createDatabase() {
        mRecipeDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                RecipeDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDatabase() {
        mRecipeDatabase.close();
    }

    @Test
    public void testInsertRecipes() {
        assertEquals(mRecipeDatabase.recipes().getRecipeCount(), 0);
        insertRecipes();
        assertEquals(mRecipeDatabase.recipes().getRecipeCount(), TEST_NUM_RECIPES);
    }

    private void insertRecipes() {
        List<Recipe> recipeList = new ArrayList<>();
        for (int i = 0; i < TEST_NUM_RECIPES; i++) {
            Recipe recipe = new Recipe();
            recipe.recipeId = (long) i + 1;
            recipe.name = String.format("Test Recipe %s", i);
            recipe.servings = i + 8;
            List<Ingredient> ingredientList = new ArrayList<>();
            for (int j = 0; j < 7 - i; j++) {
                Ingredient ingredient = new Ingredient();
                ingredient.recipeId = recipe.recipeId;
                ingredient.quantity = Double.valueOf(String.format("15%s.0", i));
                ingredient.measure = "G";
                ingredient.ingredient = String.format("Test Ingredient %s", j + i);
                ingredientList.add(ingredient);
            }
            mRecipeDatabase.recipes().insertIngredients(ingredientList);
            List<Step> stepList = new ArrayList<>();
            for (int j = 0; j < 12 - i; j++) {
                Step step = new Step();
                step.stepId = (long) j + 1;
                step.recipeId = recipe.recipeId;
                step.shortDescription = String.format("Test Short Description %s", j);
                step.description = String.format("Test Description %s", j);
                step.thumbnailUrl = "";
                step.videoUrl = "";
                stepList.add(step);
            }
            mRecipeDatabase.recipes().insertSteps(stepList);
            recipeList.add(recipe);
        }

        mRecipeDatabase.recipes().insertRecipes(recipeList);
    }
}
