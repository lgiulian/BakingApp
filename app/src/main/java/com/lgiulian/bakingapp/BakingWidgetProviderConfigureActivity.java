package com.lgiulian.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lgiulian.bakingapp.model.Recipe;
import com.lgiulian.bakingapp.utils.ApiUtils;
import com.lgiulian.bakingapp.utils.RecipeService;
import com.lgiulian.bakingapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * The configuration screen for the {@link BakingWidgetProvider BakingWidgetProvider} AppWidget.
 */
public class BakingWidgetProviderConfigureActivity extends AppCompatActivity implements RecipeListAdapter.OnRecipeClickListener {

    private static final String PREFS_NAME = "com.lgiulian.bakingapp.BakingWidgetProvider";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    private RecipeListAdapter mAdapter;
    private RecipeService mService;
    private List<Recipe> mRecipes;

    public BakingWidgetProviderConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    private static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.baking_widget_provider_configure);

        mService = ApiUtils.getRecipeService();

        RecyclerView recyclerView = findViewById(R.id.recipes_list_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new RecipeListAdapter(this, this, R.layout.list_item_layout, new ArrayList<Recipe>());
        recyclerView.setAdapter(mAdapter);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

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
                    mRecipes = Recipe.getAllRecipes(BakingWidgetProviderConfigureActivity.this);
                    mAdapter.setData(mRecipes);
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Timber.d("ERROR getting recipes received from network in onFailure() with message: %s", t.getMessage());
                mRecipes = Recipe.getAllRecipes(BakingWidgetProviderConfigureActivity.this);
                mAdapter.setData(mRecipes);
            }
        });
    }

    @Override
    public void onRecipeSelected(int position) {
        Timber.d("clicked on recipe at position: %d", position);
        Recipe selectedRecipe = mRecipes.get(position);
        final Context context = BakingWidgetProviderConfigureActivity.this;

        // When the recipe is clicked, store the string locally
        String widgetText = selectedRecipe.getName() + "\n" + Utils.getIngredientsPrettyFormat(this, selectedRecipe);
        saveTitlePref(context, mAppWidgetId, widgetText);

        // It is the responsibility of the configuration activity to update the app widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        BakingWidgetProvider.updateAppWidget(context, appWidgetManager, mAppWidgetId);

        // Make sure we pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }
}

