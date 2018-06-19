package com.udacity.popularmovies.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

public class MovieReviewListViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Context mContext;
    private final long mMovieId;

    public MovieReviewListViewModelFactory(Context context, long movieId) {
        this.mContext = context;
        this.mMovieId = movieId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MovieReviewListViewModel(mContext, mMovieId);
    }
}
