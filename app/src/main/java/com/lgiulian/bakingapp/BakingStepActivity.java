package com.lgiulian.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import timber.log.Timber;

import static com.lgiulian.bakingapp.ExoPlayerFragment.MEDIA_URL_KEY;
import static com.lgiulian.bakingapp.RecipeStepInstructionsFragment.STEP_INSTRUCTIONS_KEY;

public class BakingStepActivity extends AppCompatActivity {
    private String mMediaUrl;
    private String mInstructions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baking_step);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mMediaUrl = extras.getString(MEDIA_URL_KEY);
            mInstructions = extras.getString(STEP_INSTRUCTIONS_KEY);
            Timber.d("step instructions: %s", mInstructions);
            String recipeName = extras.getString(RecipeActivity.RECIPE_NAME_KEY, getString(R.string.app_name));
            getSupportActionBar().setTitle(recipeName);
        }

        if (savedInstanceState == null) {
            ExoPlayerFragment playerFragment = new ExoPlayerFragment();
            playerFragment.setMediaUrl(mMediaUrl);

            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.video_player, playerFragment)
                    .commit();

        }

        if (findViewById(R.id.step_instructions) != null) {
            if (savedInstanceState == null) {
                RecipeStepInstructionsFragment stepInstructionsFragment = new RecipeStepInstructionsFragment();
                stepInstructionsFragment.setStepDescription(mInstructions);

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.step_instructions, stepInstructionsFragment)
                        .commit();
            }
        }
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
}
