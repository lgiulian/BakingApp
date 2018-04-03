package com.lgiulian.bakingapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lgiulian.bakingapp.model.Recipe;

import java.util.List;

public class RecipeListAdapter extends ArrayAdapter<Recipe> {
    private Context mContext;
    private List<Recipe> mData;
    private int mLayoutResourceId;

    public RecipeListAdapter(Context context, int layoutResourceId, List<Recipe> recipes) {
        super(context, layoutResourceId, recipes);
        mContext = context;
        mLayoutResourceId = layoutResourceId;
        mData = recipes;
    }

    static class ViewHolder {
        TextView recipeTitle;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Recipe recipe = getItem(position);
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.grid_item_layout, parent, false);
            holder = new ViewHolder();
            holder.recipeTitle = convertView.findViewById(R.id.recipe_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.recipeTitle.setText(recipe.getName());
        return convertView;
    }
}
