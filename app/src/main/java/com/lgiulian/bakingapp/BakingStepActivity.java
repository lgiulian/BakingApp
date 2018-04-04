package com.lgiulian.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.lgiulian.bakingapp.model.BakingStep;
import com.lgiulian.bakingapp.model.Recipe;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.List;

import timber.log.Timber;

public class BakingStepActivity extends AppCompatActivity implements StepperLayout.StepperListener {
    private String mMediaUrl;
    private String mInstructions;

    private Recipe mRecipe;
    private int mCurrentStep;

    private boolean mTwoFraments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baking_step);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mRecipe = extras.getParcelable(MainActivity.RECIPE_KEY);
            mCurrentStep = extras.getInt(RecipeActivity.STEP_SELECTED_KEY);
            List<BakingStep> steps = mRecipe.getSteps();
            BakingStep step = steps.get(mCurrentStep);

            mMediaUrl = step.getVideoURL();
            mInstructions = step.getDescription();
            Timber.d("step instructions: %s", mInstructions);
            getSupportActionBar().setTitle(mRecipe.getName());
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
            mTwoFraments = true;
            if (savedInstanceState == null) {
                RecipeStepInstructionsFragment stepInstructionsFragment = new RecipeStepInstructionsFragment();
                stepInstructionsFragment.setStepDescription(mInstructions);

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.step_instructions, stepInstructionsFragment)
                        .commit();
            }
            StepperLayout stepperLayout = findViewById(R.id.stepperLayout);
            BakingStepAdapter bakingStepAdapter = new BakingStepAdapter(getSupportFragmentManager(), this, mRecipe.getSteps().size());
            stepperLayout.setAdapter(bakingStepAdapter);
            stepperLayout.setListener(this);
            stepperLayout.setCurrentStepPosition(mCurrentStep);
        } else {
            mTwoFraments = false;
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

    @Override
    public void onCompleted(View completeButton) {
        NavUtils.navigateUpFromSameTask(this);
    }

    @Override
    public void onError(VerificationError verificationError) {

    }

    @Override
    public void onStepSelected(int newStepPosition) {
        Timber.d("new step position: %d", newStepPosition);
        mCurrentStep = newStepPosition;
        List<BakingStep> steps = mRecipe.getSteps();
        BakingStep step = steps.get(mCurrentStep);
        mMediaUrl = step.getVideoURL();
        mInstructions = step.getDescription();

        ExoPlayerFragment exoPlayerFragment = new ExoPlayerFragment();
        exoPlayerFragment.setMediaUrl(step.getVideoURL());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.playerView, exoPlayerFragment)
                .commit();

        if (mTwoFraments) {
            RecipeStepInstructionsFragment stepInstructionsFragment = new RecipeStepInstructionsFragment();
            stepInstructionsFragment.setStepDescription(step.getDescription());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_instructions, stepInstructionsFragment)
                    .commit();
        }
    }

    @Override
    public void onReturn() {

    }
}
