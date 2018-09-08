package com.udacity.bakingapp;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;

import com.udacity.bakingapp.repository.RecipeRepository;
import com.udacity.bakingapp.viewmodel.RecipeListViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int MIN_GRID_DEVICE_WIDTH = 500;

    private static int sCurrentPosition = 0;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecipeAdapter mRecipeAdapter;
    private RecipeListViewModel mRecipeListViewModel;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (determineDeviceSmallestWidth() < MIN_GRID_DEVICE_WIDTH) {
            mLayoutManager = new LinearLayoutManager(this);
        } else {
            mLayoutManager = new GridLayoutManager(this, determineNumColumns());
        }

        mRecyclerView = findViewById(R.id.recipe_recycler_view);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mRecipeAdapter = new RecipeAdapter(this, this, new ArrayList<>());
        mRecyclerView.setAdapter(mRecipeAdapter);

        mRecipeListViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);
        mRecipeListViewModel.getRecipeList().observe(MainActivity.this, recipeWrapperList -> {
            Log.d(TAG, "onCreate - Updating list of recipes.");
            sCurrentPosition = 0;
            mRecipeAdapter.swapRecipes(recipeWrapperList);
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            Log.d(TAG, "onCreate - Swipe to refresh gesture.");
            RecipeRepository.updateRecipes(this);
        });
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
        return displayMetrics.widthPixels / MIN_GRID_DEVICE_WIDTH;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_recipes:
                RecipeRepository.updateRecipes(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(long id) {
        Log.d(TAG, String.format("onClick - recipeId:%s", id));
    }
}
