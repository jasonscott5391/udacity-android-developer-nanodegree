package com.udacity.bakingapp;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import static com.udacity.bakingapp.MainActivity.INTENT_KEY_RECIPE_NAME;

public class RecipeDetailActivity extends AppCompatActivity {

    private static final String TAG = RecipeDetailActivity.class.getSimpleName();

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

        if (findViewById(R.id.recipe_detail_fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
            recipeStepsFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_detail_fragment_container, recipeStepsFragment).commit();
        }
    }
}
