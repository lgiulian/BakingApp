package com.lgiulian.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;

import com.lgiulian.bakingapp.model.BakingStep;
import com.lgiulian.bakingapp.model.Recipe;

import java.util.List;

import timber.log.Timber;

import static com.lgiulian.bakingapp.ExoPlayerFragment.MEDIA_URL_KEY;
import static com.lgiulian.bakingapp.RecipeStepInstructionsFragment.STEP_INSTRUCTIONS_KEY;

public class RecipeActivity extends AppCompatActivity implements RecipeDetailsFragment.OnRecipeStepClickListener {
    public static final String RECIPE_NAME_KEY = "RECIPE_NAME_KEY";
    private boolean mTwoPane;
    private Recipe mRecipe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable(MainActivity.RECIPE_KEY);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mRecipe = extras.getParcelable(MainActivity.RECIPE_KEY);
            if (mRecipe != null && !TextUtils.isEmpty(mRecipe.getName())) {
                getSupportActionBar().setTitle(mRecipe.getName());
            }
        }

        if (savedInstanceState == null) {
            RecipeDetailsFragment detailsFragment = new RecipeDetailsFragment();
            detailsFragment.setRecipe(mRecipe);

            // Add the fragment to its container using a FragmentManager and a Transaction
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.recipe_details_fragment, detailsFragment)
                    .commit();
        }

        if (findViewById(R.id.video_player) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                ExoPlayerFragment playerFragment = new ExoPlayerFragment();
                playerFragment.setMediaUrl(mRecipe.getSteps().get(0).getVideoURL());

                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .add(R.id.video_player, playerFragment)
                        .commit();
                RecipeStepInstructionsFragment stepInstructionsFragment = new RecipeStepInstructionsFragment();
                stepInstructionsFragment.setStepDescription(mRecipe.getSteps().get(0).getDescription());

                fragmentManager.beginTransaction()
                        .add(R.id.step_instructions, stepInstructionsFragment)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MainActivity.RECIPE_KEY, mRecipe);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRecipeStepSelected(int position) {
        Timber.d("clicked step at position: %d", position);
        List<BakingStep> steps = mRecipe.getSteps();
        BakingStep step = steps.get(position);

        if (mTwoPane) {
            ExoPlayerFragment exoPlayerFragment = new ExoPlayerFragment();
            exoPlayerFragment.setMediaUrl(step.getVideoURL());

            getSupportFragmentManager().beginTransaction()
                .replace(R.id.playerView, exoPlayerFragment)
                .commit();

            RecipeStepInstructionsFragment stepInstructionsFragment = new RecipeStepInstructionsFragment();
            stepInstructionsFragment.setStepDescription(step.getDescription());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_instructions, stepInstructionsFragment)
                    .commit();
        } else {
            Bundle b = new Bundle();
            b.putString(MEDIA_URL_KEY, step.getVideoURL());
            b.putString(STEP_INSTRUCTIONS_KEY, step.getDescription());
            b.putString(RECIPE_NAME_KEY, mRecipe.getName());
            Intent intent = new Intent(this, BakingStepActivity.class);
            intent.putExtras(b);
            startActivity(intent);
        }
    }
}
