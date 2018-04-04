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
    private final Context mContext;
    private final int mLayoutResourceId;

    public RecipeListAdapter(Context context, int layoutResourceId, List<Recipe> recipes) {
        super(context, layoutResourceId, recipes);
        mContext = context;
        mLayoutResourceId = layoutResourceId;
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
            convertView = inflater.inflate(mLayoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.recipeTitle = convertView.findViewById(R.id.recipe_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (recipe != null) {
            holder.recipeTitle.setText(recipe.getName());
        }
        return convertView;
    }
}
