package com.lgiulian.bakingapp.utils;

public class ApiUtils {
    public static final String RECIPE_BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";

    public static RecipeService getRecipeService() {
        return RetrofitClient.getClient(RECIPE_BASE_URL).create(RecipeService.class);
    }
}
