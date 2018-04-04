package com.lgiulian.bakingapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lgiulian.bakingapp.model.BakingStep;

import java.util.List;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.RecipeStepViewHolder> {

    private List<BakingStep> mStepsData;
    private final RecipeDetailsFragment.OnRecipeStepClickListener mClickHandler;

    public RecipeStepsAdapter(RecipeDetailsFragment.OnRecipeStepClickListener clickHandler) {
        this.mClickHandler = clickHandler;
    }

    public void setStepsData(List<BakingStep> stepsData) {
        this.mStepsData = stepsData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeStepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_recipe_step_layout, parent, false);
        return new RecipeStepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepViewHolder holder, int position) {
        BakingStep step = mStepsData.get(position);
        holder.mRecipeStepShortDesc.setText(step.getShortDescription());
    }

    @Override
    public int getItemCount() {
        return mStepsData != null? mStepsData.size(): 0;
    }

    class RecipeStepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView mRecipeStepShortDesc;

        public RecipeStepViewHolder(View itemView) {
            super(itemView);
            mRecipeStepShortDesc = itemView.findViewById(R.id.recipe_step_short_desc);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mClickHandler.onRecipeStepSelected(position);
        }
    }
}
