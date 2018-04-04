package com.lgiulian.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;

public class BakingStepAdapter extends AbstractFragmentStepAdapter {
    private static final String CURRENT_STEP_POSITION_KEY = "CURRENT_STEP_POSITION_KEY";

    private final int mStepsCounter;

    public BakingStepAdapter(@NonNull FragmentManager fm, @NonNull Context context, int stepsCounter) {
        super(fm, context);
        mStepsCounter = stepsCounter;
    }

    @Override
    public Step createStep(int position) {
        final StepperBlankFragment step = new StepperBlankFragment();
        Bundle b = new Bundle();
        b.putInt(CURRENT_STEP_POSITION_KEY, position);
        step.setArguments(b);
        return step;
    }

    @Override
    public int getCount() {
        return mStepsCounter;
    }
}
