package com.udacity.bakingapp.service;

import com.google.gson.annotations.SerializedName;
import com.udacity.bakingapp.entity.Ingredient;
import com.udacity.bakingapp.entity.Step;

import java.util.List;

public class RecipeWrapper {

    private static final String SERIALIZED_RECIPE_INGREDIENTS_LIST = "ingredients";
    private static final String SERIALIZED_RECIPE_STEPS_LIST = "steps";
    private static final String SERIALIZED_RECIPE_ID = "id";

    @SerializedName(SERIALIZED_RECIPE_ID)
    public Long recipeId;

    public String name;

    public int servings;

    public String image;

    @SerializedName(SERIALIZED_RECIPE_INGREDIENTS_LIST)
    public List<Ingredient> ingredientList;

    @SerializedName(SERIALIZED_RECIPE_STEPS_LIST)
    public List<Step> stepList;

}
