package com.udacity.bakingapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.udacity.bakingapp.entity.RecipeWrapper;
import com.udacity.bakingapp.repository.RecipeRepository;

import java.util.List;

public class RecipeListViewModel extends AndroidViewModel {

    private static final String TAG = RecipeListViewModel.class.getSimpleName();

    private MutableLiveData<List<RecipeWrapper>> mRecipeList;

    public RecipeListViewModel(@NonNull Application application) {
        super(application);

        Log.d(TAG, "RecipeListViewModel - Initializing repository.");
        Context context = this.getApplication();
        RecipeRepository.init(context);

        Log.d(TAG, "RecipeListViewModel - Assigning recipes from repository.");
        mRecipeList = RecipeRepository.getRecipeList();
    }

    public MutableLiveData<List<RecipeWrapper>> getRecipeList() {
        Log.d(TAG, "getRecipeList - Actively retrieving recipes.");
        return mRecipeList;
    }
}
