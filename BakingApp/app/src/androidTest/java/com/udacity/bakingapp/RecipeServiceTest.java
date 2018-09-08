package com.udacity.bakingapp;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.support.test.runner.AndroidJUnit4;

import com.udacity.bakingapp.entity.RecipeWrapper;
import com.udacity.bakingapp.service.RecipeService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static junit.framework.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class RecipeServiceTest {

    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net";

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private static RecipeService sRecipeService;

    @Before
    public void initService() {
        sRecipeService = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RecipeService.class);
    }

    @Test
    public void testGetRecipes() throws IOException {
        Response<List<RecipeWrapper>> response = sRecipeService.getRecipes().execute();

        List<RecipeWrapper> recipeWrapperList = response.body();
        assertNotNull(recipeWrapperList);
        for (RecipeWrapper recipeWrapper : recipeWrapperList) {
            assertNotNull(recipeWrapper.recipeId);
            assertNotNull(recipeWrapper.name);
            assertNotNull(recipeWrapper.ingredientList);
            assertNotNull(recipeWrapper.stepList);
        }
    }
}
