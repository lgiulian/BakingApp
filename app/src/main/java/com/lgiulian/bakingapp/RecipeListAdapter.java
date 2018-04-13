package com.lgiulian.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lgiulian.bakingapp.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> {
    private final int mLayoutResourceId;

    private final Context mContext;
    private final OnRecipeClickListener mClickHandler;

    private List<Recipe> mData;

    public interface OnRecipeClickListener {
        void onRecipeSelected(int position);
    }

    public RecipeListAdapter(Context context, OnRecipeClickListener clickHandler, int layoutResourceId, List<Recipe> recipes) {
        mContext = context;
        mClickHandler = clickHandler;
        mLayoutResourceId = layoutResourceId;
        mData = recipes;
    }

    public void setData(List<Recipe> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rootView = inflater.inflate(mLayoutResourceId, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = mData.get(position);
        holder.recipeTitle.setText(recipe.getName());
        Picasso.with(mContext)
                .load(TextUtils.isEmpty(recipe.getImage())? null: recipe.getImage())
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .into(holder.recipeImage);
    }

    @Override
    public int getItemCount() {
        return mData != null? mData.size(): 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView recipeImage;
        TextView recipeTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            recipeImage = itemView.findViewById(R.id.recipe_image_view);
            recipeImage.setOnClickListener(this);
            recipeTitle = itemView.findViewById(R.id.recipe_name);
            recipeTitle.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            mClickHandler.onRecipeSelected(pos);
        }
    }
}
