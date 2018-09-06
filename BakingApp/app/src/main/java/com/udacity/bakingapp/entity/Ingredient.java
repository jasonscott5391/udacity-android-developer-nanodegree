package com.udacity.bakingapp.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import static com.udacity.bakingapp.entity.Ingredient.INGREDIENTS_TABLE_NAME;
import static com.udacity.bakingapp.entity.Recipe.COLUMN_RECIPE_ID;

@Entity(tableName = INGREDIENTS_TABLE_NAME, indices = {@Index(value = {COLUMN_RECIPE_ID})})
public class Ingredient {

    public static final String INGREDIENTS_TABLE_NAME = "ingredients";
    public static final String COLUMN_INGREDIENT_ID = "ingredient_id";
    public static final String COLUMN_INGREDIENT_QUANTITY = "quantity";
    public static final String COLUMN_INGREDIENT_MEASURE = "measure";
    public static final String COLUMN_INGREDIENT_INGREDIENT = "ingredient";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_INGREDIENT_ID)
    public Long ingredientId;

    @ColumnInfo(name = COLUMN_RECIPE_ID)
    public Long recipeId;

    @ColumnInfo(name = COLUMN_INGREDIENT_QUANTITY)
    public Double quantity;

    @ColumnInfo(name = COLUMN_INGREDIENT_MEASURE)
    public String measure;

    @ColumnInfo(name = COLUMN_INGREDIENT_INGREDIENT)
    public String ingredient;
}
