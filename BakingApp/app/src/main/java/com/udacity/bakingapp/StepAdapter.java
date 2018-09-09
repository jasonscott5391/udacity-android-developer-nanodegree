package com.udacity.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.bakingapp.entity.Step;

import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    private final Context mContext;
    private List<Step> mStepList;
    private StepClickHandler mStepClickHandler;

    public StepAdapter(@NonNull final Context context, StepClickHandler stepClickHandler, List<Step> stepList) {
        this.mContext = context;
        this.mStepList = stepList;
        this.mStepClickHandler = stepClickHandler;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.step_item, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        Step step = mStepList.get(position);
        holder.mStepShortDescriptionTextView.setText(step.shortDescription);
    }

    @Override
    public int getItemCount() {
        if (mStepList == null) {
            return 0;
        }

        return mStepList.size();
    }

    public void swapSteps(List<Step> stepList) {
        this.mStepList = stepList;
        notifyDataSetChanged();
    }

    public interface StepClickHandler {
        void onClickStep(long stepId);
    }

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView mStepShortDescriptionTextView;

        public StepViewHolder(View itemView) {
            super(itemView);
            mStepShortDescriptionTextView = itemView.findViewById(R.id.recipe_step_short_description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mStepClickHandler.onClickStep(mStepList.get(getAdapterPosition()).stepId);
        }
    }
}
