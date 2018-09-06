package com.udacity.bakingapp.entity;

import android.arch.persistence.room.Relation;

import java.util.List;

public class RecipeWrapper extends Recipe {

    @Relation(parentColumn = COLUMN_RECIPE_ID, entityColumn = COLUMN_RECIPE_ID)
    public List<Ingredient> ingredientList;

    @Relation(parentColumn = COLUMN_RECIPE_ID, entityColumn = COLUMN_RECIPE_ID)
    public List<Step> stepList;

}
