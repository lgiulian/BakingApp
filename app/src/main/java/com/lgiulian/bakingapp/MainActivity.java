package com.lgiulian.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.lgiulian.bakingapp.model.Recipe;
import com.lgiulian.bakingapp.utils.ApiUtils;
import com.lgiulian.bakingapp.utils.RecipeService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements RecipeListAdapter.OnRecipeClickListener {
    public static final String RECIPE_KEY = "RECIPE_KEY";
    private RecipeListAdapter mAdapter;
    private RecipeService mService;
    private List<Recipe> mRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mService = ApiUtils.getRecipeService();

        RecyclerView recyclerView = findViewById(R.id.recipes_rv);
        GridLayoutManager layoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.recipes_list_columns));
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new RecipeListAdapter(this, this, R.layout.grid_item_layout, new ArrayList<Recipe>());
        recyclerView.setAdapter(mAdapter);

        loadRecipes();
    }

    private void loadRecipes() {
        mService.getRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                    Timber.d("recipes received from network");
                    mRecipes = response.body();
                    mAdapter.setData(mRecipes);
                } else {
                    Timber.d("ERROR getting recipes received from network");
                    mRecipes = Recipe.getAllRecipes(MainActivity.this);
                    mAdapter.setData(mRecipes);
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Timber.d("ERROR getting recipes received from network in onFailure() with message: %s", t.getMessage());
                mRecipes = Recipe.getAllRecipes(MainActivity.this);
                mAdapter.setData(mRecipes);
            }
        });
    }

    @Override
    public void onRecipeSelected(int position) {
        Timber.d("clicked on recipe at position: %d", position);
        Recipe selectedRecipe = mRecipes.get(position);
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra(RECIPE_KEY, selectedRecipe);
        startActivity(intent);
    }

}
