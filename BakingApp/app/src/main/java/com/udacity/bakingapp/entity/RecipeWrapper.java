package com.udacity.bakingapp.entity;

import android.arch.persistence.room.Relation;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecipeWrapper extends Recipe {

    private static final String SERIALIZED_RECIPE_INGREDIENTS_LIST = "ingredients";
    private static final String SERIALIZED_RECIPE_STEPS_LIST = "steps";

    @Relation(parentColumn = COLUMN_RECIPE_ID, entityColumn = COLUMN_RECIPE_ID)
    @SerializedName(SERIALIZED_RECIPE_INGREDIENTS_LIST)
    public List<Ingredient> ingredientList;

    @Relation(parentColumn = COLUMN_RECIPE_ID, entityColumn = COLUMN_RECIPE_ID)
    @SerializedName(SERIALIZED_RECIPE_STEPS_LIST)
    public List<Step> stepList;

}
