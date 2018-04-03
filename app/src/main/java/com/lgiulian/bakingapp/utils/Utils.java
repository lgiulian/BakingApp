package com.lgiulian.bakingapp.utils;

import android.content.Context;

import com.lgiulian.bakingapp.R;
import com.lgiulian.bakingapp.model.Ingredient;
import com.lgiulian.bakingapp.model.Recipe;

public class Utils {

    public static String getIngredientsPrettyFormat(Context context, Recipe recipe) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(context.getString(R.string.ingredients_label) + ": ");
        int counter = 0;
        for(Ingredient ingredient: recipe.getIngredients()) {
            String delimiter = "";
            if (counter < recipe.getIngredients().size() - 1) {
                delimiter = ", ";
            }
            strBuilder.append(ingredient.getIngredient() + delimiter);
            counter++;
        }
        return strBuilder.toString();
    }

}
