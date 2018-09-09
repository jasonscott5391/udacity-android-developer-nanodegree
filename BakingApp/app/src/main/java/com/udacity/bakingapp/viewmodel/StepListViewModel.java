package com.udacity.bakingapp.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;

import com.udacity.bakingapp.entity.Step;
import com.udacity.bakingapp.repository.RecipeRepository;

import java.util.List;

public class StepListViewModel extends ViewModel {

    private MutableLiveData<List<Step>> mStepList;

    public StepListViewModel(@NonNull Context context, long recipeId) {
        mStepList = RecipeRepository.getStepList(context, recipeId);
    }

    public MutableLiveData<List<Step>> getStepList() {
        return mStepList;
    }
}
