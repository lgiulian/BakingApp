package com.lgiulian.bakingapp.model;

import android.util.JsonReader;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by iulian on 3/24/2018.
 */

public class Ingredient {
    private double quantity;
    private String measure;
    private String ingredient;

    /** Method reads all the ingredients from a json reader
     * @param reader
     * @return the list of ingredients
     */
    public static ArrayList<Ingredient> getAllIngredients(JsonReader reader){
        ;
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        try {
            reader.beginArray();
            while (reader.hasNext()) {
                ingredients.add(readEntry(reader));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ingredients;
    }

    /** Method used to read a single ingredient from a json reader
     * @param reader
     * @return an ingredient
     */
    private static Ingredient readEntry(JsonReader reader) {
        double quantity = 0.0;
        String measure = null;
        String ingredient = null;

        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case "quantity":
                        quantity = reader.nextDouble();
                        break;
                    case "measure":
                        measure = reader.nextString();
                        break;
                    case "ingredient":
                        ingredient = reader.nextString();
                        break;
                    default:
                        break;
                }
            }
            reader.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Ingredient(quantity, measure, ingredient);
    }

    public Ingredient(double quantity, String measure, String ingredient) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
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
}
