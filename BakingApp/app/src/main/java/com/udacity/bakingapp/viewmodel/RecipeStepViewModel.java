package com.udacity.bakingapp.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;

import com.udacity.bakingapp.entity.Step;
import com.udacity.bakingapp.repository.RecipeRepository;

public class RecipeStepViewModel extends ViewModel {

    private MutableLiveData<Step> mStep;

    public RecipeStepViewModel(@NonNull Context context, long recipeId, long stepId) {
        mStep = RecipeRepository.getStepById(context, recipeId, stepId);
    }

    public MutableLiveData<Step> getStep() {
        return mStep;
    }
}
