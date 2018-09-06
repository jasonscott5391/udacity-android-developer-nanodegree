package com.udacity.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.bakingapp.entity.Recipe;
import com.udacity.bakingapp.entity.RecipeWrapper;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private final Context mContext;
    private final RecipeClickHandler mRecipeClickHandler;
    private List<RecipeWrapper> mRecipeWrapperList;

    public RecipeAdapter(Context context, RecipeClickHandler recipeClickHandler, List<RecipeWrapper> recipeList) {
        this.mContext = context;
        this.mRecipeWrapperList = recipeList;
        this.mRecipeClickHandler = recipeClickHandler;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recipe_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        RecipeWrapper recipeWrapper = mRecipeWrapperList.get(position);
        holder.mRecipeNameTextView.setText(recipeWrapper.name);
        holder.mRecipeNumServingsTextView.setText(String.format("%s Servings", recipeWrapper.servings));
        holder.mRecipeNumStepsTextView.setText(String.format("%s Steps", recipeWrapper.stepList.size()));
    }

    @Override
    public int getItemCount() {
        if (mRecipeWrapperList == null) {
            return 0;
        }
        return mRecipeWrapperList.size();
    }

    public interface RecipeClickHandler {
        void onClick(long id);
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView mRecipeNameTextView;
        final ImageView mRecipeThumbnailImageView;
        final TextView mRecipeNumServingsTextView;
        final TextView mRecipeNumStepsTextView;


        public RecipeViewHolder(View itemView) {
            super(itemView);
            mRecipeNameTextView = itemView.findViewById(R.id.recipe_name);
            mRecipeThumbnailImageView = itemView.findViewById(R.id.recipe_thumbnail);
            mRecipeNumServingsTextView = itemView.findViewById(R.id.recipe_num_servings);
            mRecipeNumStepsTextView = itemView.findViewById(R.id.recipe_num_steps);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mRecipeClickHandler.onClick(mRecipeWrapperList.get(getAdapterPosition()).recipeId);
        }
    }
}
