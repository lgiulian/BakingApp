package com.lgiulian.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.lgiulian.bakingapp.model.Recipe;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    public static final String RECIPE_KEY = "RECIPE_KEY";
    private RecipeListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView grid = findViewById(R.id.recipes_grid_view);
        mAdapter = new RecipeListAdapter(this, R.layout.grid_item_layout, Recipe.getAllRecipes(this));
        grid.setAdapter(mAdapter);
        grid.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Timber.d("clicked on recipe at position: %d", position);
        Recipe selectedRecipe = mAdapter.getItem(position);
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra(RECIPE_KEY, selectedRecipe);
        startActivity(intent);
    }
}
