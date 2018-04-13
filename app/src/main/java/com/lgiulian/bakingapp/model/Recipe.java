package com.lgiulian.bakingapp.model;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSourceInputStream;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lgiulian.bakingapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by iulian on 3/24/2018.
 */

public class Recipe implements Parcelable {
    private static final String TAG = Recipe.class.getSimpleName();

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("ingredients")
    @Expose
    private List<Ingredient> ingredients;
    @SerializedName("steps")
    @Expose
    private List<BakingStep> steps;
    @SerializedName("servings")
    @Expose
    private int servings;
    @SerializedName("image")
    @Expose
    private String image;

    private Recipe(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        ingredients = new ArrayList<>();
        in.readTypedList(ingredients, Ingredient.CREATOR);
        steps = new ArrayList<>();
        in.readTypedList(steps, BakingStep.CREATOR);
        this.servings = in.readInt();
        this.image = in.readString();
    }

    public Recipe() {}

    /** Method that reads all the recipes from a json file
     * @param context the context
     * @return a list of recipes
     */
    public static ArrayList<Recipe> getAllRecipes(Context context){
        String json;
        ArrayList<Recipe> recipes = new ArrayList<>();
        try {
            json = readJSONFile(context);
            JSONArray recipesArray = new JSONArray(json);
            for (int i = 0; i < recipesArray.length(); i++) {
                Recipe recipe = getRecipeFromJsonObject(recipesArray.getJSONObject(i));
                recipes.add(recipe);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return recipes;
    }

    private static Recipe getRecipeFromJsonObject(JSONObject recipeJsonObject) throws JSONException {
        Recipe recipe = new Recipe();
        recipe.setId(recipeJsonObject.optInt("id"));
        recipe.setName(recipeJsonObject.optString("name"));
        recipe.setServings(recipeJsonObject.optInt("servings"));
        recipe.setImage(recipeJsonObject.optString("image"));
        recipe.setIngredients(Ingredient.getAllIngredients(recipeJsonObject.getJSONArray("ingredients")));
        recipe.setSteps(BakingStep.getAllBakingSteps(recipeJsonObject.getJSONArray("steps")));

        Log.v(TAG, recipe.toString());

        return recipe;
    }

    private static String readJSONFile(Context context) throws IOException {
        AssetManager assetManager = context.getAssets();
        String uri = null;

        try {
            for (String asset : assetManager.list("")) {
                if (asset.equalsIgnoreCase("baking.json")) {
                    uri = "asset:///" + asset;
                }
            }
        } catch (IOException e) {
            Toast.makeText(context, R.string.recipe_list_load_error, Toast.LENGTH_LONG).show();
        }

        String userAgent = Util.getUserAgent(context, "BakingApp");
        DataSource dataSource = new DefaultDataSource(context, null, userAgent, false);
        DataSpec dataSpec = new DataSpec(Uri.parse(uri));
        InputStream inputStream = new DataSourceInputStream(dataSource, dataSpec);

        StringBuilder buf = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        String str;

        while ((str=in.readLine()) != null) {
            buf.append(str);
        }
        in.close();

        return buf.toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<BakingStep> getSteps() {
        return steps;
    }

    public void setSteps(List<BakingStep> steps) {
        this.steps = steps;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ingredients=" + ingredients +
                ", steps=" + steps +
                ", servings=" + servings +
                ", image='" + image + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeTypedList(ingredients);
        dest.writeTypedList(steps);
        dest.writeInt(servings);
        dest.writeString(image);
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }

    };
}
