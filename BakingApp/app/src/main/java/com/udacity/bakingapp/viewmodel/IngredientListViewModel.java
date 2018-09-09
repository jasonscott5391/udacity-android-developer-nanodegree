package com.udacity.bakingapp.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;

import com.udacity.bakingapp.entity.Ingredient;
import com.udacity.bakingapp.repository.RecipeRepository;

import java.util.List;

public class IngredientListViewModel extends ViewModel {

    private MutableLiveData<List<Ingredient>> mIngredientList;

    public IngredientListViewModel(@NonNull Context context, long recipeId) {
        mIngredientList = RecipeRepository.getIngredientList(context, recipeId);
    }

    public MutableLiveData<List<Ingredient>> getIngredientList() {
        return mIngredientList;
    }
}
