package com.udacity.bakingapp.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

public class RecipeStepViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Context mContext;
    private final long mRecipeId;
    private final long mStepId;

    public RecipeStepViewModelFactory(@NonNull final Context context, final long recipeId, final long stepId) {
        this.mContext = context;
        this.mRecipeId = recipeId;
        this.mStepId = stepId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new RecipeStepViewModel(mContext, mRecipeId, mStepId);
    }
}
