package com.lgiulian.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lgiulian.bakingapp.model.Recipe;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements RecipeListAdapter.OnRecipeClickListener {
    public static final String RECIPE_KEY = "RECIPE_KEY";
    private RecipeListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recipes_rv);
        GridLayoutManager layoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.recipes_list_columns));
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new RecipeListAdapter(this, R.layout.grid_item_layout, Recipe.getAllRecipes(this));
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onRecipeSelected(int position) {
        Timber.d("clicked on recipe at position: %d", position);
        Recipe selectedRecipe = Recipe.getAllRecipes(this).get(position);
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra(RECIPE_KEY, selectedRecipe);
        startActivity(intent);
    }

}
