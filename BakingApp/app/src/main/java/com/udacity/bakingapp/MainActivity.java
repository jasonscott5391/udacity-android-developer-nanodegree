package com.udacity.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;

import com.udacity.bakingapp.entity.Ingredient;
import com.udacity.bakingapp.entity.Recipe;
import com.udacity.bakingapp.entity.Step;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int NUM_COLUMNS = 3;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecipeAdapter mRecipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mRecyclerView = findViewById(R.id.recipe_recycler_view);


        if (determineDeviceSmallestWidth() < 500) {
            mLayoutManager = new LinearLayoutManager(this);
        } else {
            mLayoutManager = new GridLayoutManager(this, determineNumColumns());
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        List<Recipe> recipeList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            Recipe recipe = new Recipe();
            recipe.id = (long) i + 1;
            recipe.name = String.format("Test Recipe %s", i);
            recipe.servings = i + 8;
            recipe.ingredientList = new ArrayList<>();
            for (int j = 0; j < 7 - i; j++) {
                Ingredient ingredient = new Ingredient();
                ingredient.quantity = Double.valueOf(String.format("15%s.0", i));
                ingredient.measure = "G";
                ingredient.ingredient = String.format("Test Ingredient %s", j + i);
                recipe.ingredientList.add(ingredient);
            }
            recipe.stepList = new ArrayList<>();
            for (int j = 0; j < 12 - i; j++) {
                Step step = new Step();
                step.id = (long) j + 1;
                step.shortDescription = String.format("Test Short Description %s", j);
                step.description = String.format("Test Description %s", j);
                step.thumbnailUrl = "";
                step.videoUrl = "";
                recipe.stepList.add(step);
            }
            recipeList.add(recipe);
        }

        mRecipeAdapter = new RecipeAdapter(this, this, recipeList);
        mRecyclerView.setAdapter(mRecipeAdapter);
    }

    private float determineDeviceSmallestWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int widthPixels = displayMetrics.widthPixels;
        int heightPixels = displayMetrics.heightPixels;
        float scale = displayMetrics.density;

        float widthDp = widthPixels / scale;
        float heightDp = heightPixels / scale;

        return Math.min(widthDp, heightDp);
    }

    private int determineNumColumns() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return displayMetrics.widthPixels / 500;
    }

    @Override
    public void onClick(long id) {
        Log.d(TAG, String.format("onClick - id:%s", id));
    }
}
