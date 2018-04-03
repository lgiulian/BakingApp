package com.lgiulian.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by iulian on 3/24/2018.
 */

public class Ingredient implements Parcelable {
    private static final String TAG = Ingredient.class.getSimpleName();

    private double quantity;
    private String measure;
    private String ingredient;

    private Ingredient(Parcel in) {
        this.quantity = in.readDouble();
        this.measure = in.readString();
        this.ingredient = in.readString();
    }

    public Ingredient() {}

    /** Method reads all the ingredients from a json array
     * @return the list of ingredients
     */
    public static ArrayList<Ingredient> getAllIngredients(JSONArray ingredientsArray){
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        try {
            if (ingredientsArray != null) {
                for (int i = 0; i < ingredientsArray.length(); i++) {
                    Ingredient ingredient = getIngredientFromJsonObject(ingredientsArray.getJSONObject(i));
                    ingredients.add(ingredient);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ingredients;
    }

    private static Ingredient getIngredientFromJsonObject(JSONObject recipeJsonObject) throws ParseException, JSONException {
        Ingredient ingredient = new Ingredient();
        ingredient.setQuantity(recipeJsonObject.optDouble("quantity"));
        ingredient.setMeasure(recipeJsonObject.optString("measure"));
        ingredient.setIngredient(recipeJsonObject.optString("ingredient"));

        Log.v(TAG, ingredient.toString());

        return ingredient;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "quantity=" + quantity +
                ", measure='" + measure + '\'' +
                ", ingredient='" + ingredient + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(quantity);
        dest.writeString(measure);
        dest.writeString(ingredient);
    }

    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {

        @Override
        public Ingredient createFromParcel(Parcel source) {
            return new Ingredient(source);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}
