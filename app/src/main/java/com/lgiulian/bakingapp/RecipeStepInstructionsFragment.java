package com.lgiulian.bakingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RecipeStepInstructionsFragment extends Fragment {
    private static final String STEP_INSTRUCTIONS_KEY = "STEP_INSTRUCTIONS_KEY";

    private String mStepInstructions;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            mStepInstructions = savedInstanceState.getString(STEP_INSTRUCTIONS_KEY);
        }
        View rootView = inflater.inflate(R.layout.fragment_step_instructions, container, false);
        TextView stepInstructionsTextView = rootView.findViewById(R.id.step_instructions);
        stepInstructionsTextView.setText(mStepInstructions);

        return rootView;
    }

    public void setStepDescription(String stepDescription) {
        this.mStepInstructions = stepDescription;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(STEP_INSTRUCTIONS_KEY, mStepInstructions);
    }
}
