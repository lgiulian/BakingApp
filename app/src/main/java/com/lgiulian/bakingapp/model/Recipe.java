package com.lgiulian.bakingapp.model;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSourceInputStream;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.util.Util;
import com.lgiulian.bakingapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by iulian on 3/24/2018.
 */

public class Recipe {
    private static final String TAG = Recipe.class.getSimpleName();

    private int id;
    private String name;
    private List<Ingredient> ingredients;
    private List<BakingStep> steps;
    private int servings;
    private String image;

    /** Method that reads all the recipes from a json file
     * @param context
     * @return a list of recipes
     */
    public static ArrayList<Recipe> getAllRecipes(Context context){
        JsonReader reader;
        ArrayList<Recipe> recipes = new ArrayList<>();
        try {
            reader = readJSONFile(context);
            reader.beginArray();
            while (reader.hasNext()) {
                recipes.add(readEntry(reader));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return recipes;
    }

    /** Method used to read a single recipe from a json file
     * @param reader
     * @return a recipe
     */
    private static Recipe readEntry(JsonReader reader) {
        int id = 0;
        String recipeName = null;
        List<Ingredient> ingredients = null;
        List<BakingStep> steps = null;
        int servings = 0;
        String image = null;

        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                Log.d(TAG, "name = " + name);
                switch (name) {
                    case "id":
                        id = reader.nextInt();
                        break;
                    case "recipeName":
                        recipeName = reader.nextString();
                        break;
                    case "ingredients":
                        reader.beginObject();
                        ingredients = Ingredient.getAllIngredients(reader);
                        break;
                    case "steps":
                        reader.beginObject();
                        steps = BakingStep.getAllBakingSteps(reader);
                        break;
                    case "servings":
                        servings = reader.nextInt();
                        break;
                    case "image":
                        image = reader.nextString();
                        break;
                    default:
                        break;
                }
            }
            reader.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Recipe(id, recipeName, ingredients, steps, servings, image);
    }

    private static JsonReader readJSONFile(Context context) throws IOException {
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

        JsonReader reader = null;
        try {
            reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
        } finally {
            Util.closeQuietly(dataSource);
        }

        return reader;
    }

    public Recipe(int id, String name, List<Ingredient> ingredients, List<BakingStep> steps, int servings, String image) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
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
}
