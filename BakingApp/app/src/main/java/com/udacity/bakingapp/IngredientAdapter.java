package com.udacity.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.bakingapp.entity.Ingredient;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private final Context mContext;
    private List<Ingredient> mIngredientList;

    public IngredientAdapter(@NonNull final Context context, List<Ingredient> ingredientList) {
        this.mContext = context;
        this.mIngredientList = ingredientList;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.ingredient_item, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ingredient = mIngredientList.get(position);
        holder.mIngredientTextTextView.setText(
                String.format("%s %s - %s",
                        ingredient.quantity,
                        ingredient.measure,
                        ingredient.ingredient));
    }

    @Override
    public int getItemCount() {
        if (mIngredientList == null) {
            return 0;
        }
        return mIngredientList.size();
    }

    public void swapIngredients(List<Ingredient> ingredientList) {
        this.mIngredientList = ingredientList;
        notifyDataSetChanged();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        final TextView mIngredientTextTextView;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            mIngredientTextTextView = itemView.findViewById(R.id.recipe_ingredient_text);
        }
    }
}
