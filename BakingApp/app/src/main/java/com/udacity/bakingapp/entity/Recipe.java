package com.udacity.bakingapp.entity;

import java.util.List;

public class Recipe {
    public Long id;
    public String name;
    public List<Ingredient> ingredientList;
    public List<Step> stepList;
    public int servings;
    public String image;
}
