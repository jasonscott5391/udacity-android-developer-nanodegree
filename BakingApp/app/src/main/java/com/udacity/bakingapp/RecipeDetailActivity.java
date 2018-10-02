package com.udacity.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import static com.udacity.bakingapp.MainActivity.INTENT_KEY_RECIPE_ID;
import static com.udacity.bakingapp.MainActivity.INTENT_KEY_RECIPE_NAME;
import static com.udacity.bakingapp.RecipeStepDetailFragment.INTENT_KEY_IS_DUAL_PANE;
import static com.udacity.bakingapp.RecipeStepsFragment.INTENT_KEY_STEP_ID;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeStepsFragment.OnRecipeStepSelectedListener, RecipeStepDetailFragment.OnNextPreviousRecipeStepSelected {

    private static final String TAG = RecipeDetailActivity.class.getSimpleName();

    private FragmentManager mFragmentManager;
    private boolean mIsDualPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ActionBar actionBar = getSupportActionBar();
        Bundle bundle = getIntent().getExtras();
        if (actionBar != null
                && bundle != null) {
            actionBar.setTitle(bundle.getString(INTENT_KEY_RECIPE_NAME,
                    getResources().getString(R.string.app_name)));
        }

        mFragmentManager = getSupportFragmentManager();

        mIsDualPane = findViewById(R.id.recipe_steps_detail_fragment_container) != null;

        if (!mIsDualPane) {
            if (savedInstanceState == null) {
                RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
                recipeStepsFragment.setArguments(getIntent().getExtras());

                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.recipe_detail_fragment_container, recipeStepsFragment);
                fragmentTransaction.addToBackStack(RecipeStepsFragment.TAG);
                fragmentTransaction.commit();
            }
        } else {
            if (savedInstanceState == null) {
                RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
                recipeStepsFragment.setArguments(getIntent().getExtras());

                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.recipe_detail_fragment_container, recipeStepsFragment);
                fragmentTransaction.addToBackStack(RecipeStepsFragment.TAG);

                RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
                Intent intent = getIntent();
                intent.putExtra(INTENT_KEY_IS_DUAL_PANE, mIsDualPane);
                recipeStepDetailFragment.setArguments(intent.getExtras());
                fragmentTransaction.add(R.id.recipe_steps_detail_fragment_container, recipeStepDetailFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
    }

    @Override
    public void onRecipeStepSelected(long recipeId, String recipeName, long stepId) {
        Log.d(TAG, String.format("onRecipeStepSelected - recipeId:%d,recipeName:%s,stepId:%d", recipeId, recipeName, stepId));
        commitRecipeStepDetailFragment(recipeId, recipeName, stepId);
    }

    @Override
    public void onNextPreviousRecipeStepSelected(long recipeId, String recipeName, long stepId) {
        Log.d(TAG, String.format("onNextPreviousRecipeStepSelected - recipeId:%d,recipeName:%s,stepId:%d", recipeId, recipeName, stepId));
        commitRecipeStepDetailFragment(recipeId, recipeName, stepId);
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed");
        if (!mIsDualPane
                && mFragmentManager.getBackStackEntryCount() > 1) {
            popRecipeStepDetailFragments();
        } else {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, String.format("onOptionsItemSelected - itemId:%s", item.getItemId()));
        if (item.getItemId() == android.R.id.home
                && !mIsDualPane
                && mFragmentManager.getBackStackEntryCount() > 1) {
            popRecipeStepDetailFragments();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void commitRecipeStepDetailFragment(long recipeId, String recipeName, long stepId) {
        RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
        Intent intent = getIntent();
        intent.putExtra(INTENT_KEY_RECIPE_ID, recipeId);
        intent.putExtra(INTENT_KEY_STEP_ID, stepId);
        intent.putExtra(INTENT_KEY_RECIPE_NAME, recipeName);
        recipeStepDetailFragment.setArguments(intent.getExtras());
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        int fragmentContainerId = mIsDualPane ? R.id.recipe_steps_detail_fragment_container : R.id.recipe_detail_fragment_container;
        fragmentTransaction.replace(fragmentContainerId, recipeStepDetailFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void popRecipeStepDetailFragments() {
        while (mFragmentManager.getBackStackEntryCount() > 1) {
            mFragmentManager.popBackStackImmediate();
        }
    }
}
