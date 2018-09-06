package com.udacity.bakingapp.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import static com.udacity.bakingapp.entity.Recipe.RECIPES_TABLE_NAME;

@Entity(tableName = RECIPES_TABLE_NAME)
public class Recipe {

    public static final String RECIPES_TABLE_NAME = "recipes";
    public static final String COLUMN_RECIPE_ID = "recipe_id";
    public static final String COLUMN_RECIPE_NAME = "name";
    public static final String COLUMN_RECIPE_SERVINGS = "servings";
    public static final String COLUMN_RECIPE_IMAGE = "image";

    @PrimaryKey
    @ColumnInfo(index = true, name = COLUMN_RECIPE_ID)
    public Long recipeId;

    @ColumnInfo(name = COLUMN_RECIPE_NAME)
    public String name;

    @ColumnInfo(name = COLUMN_RECIPE_SERVINGS)
    public int servings;

    @ColumnInfo(name = COLUMN_RECIPE_IMAGE)
    public String image;
}
