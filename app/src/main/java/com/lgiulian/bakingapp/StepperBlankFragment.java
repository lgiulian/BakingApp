package com.lgiulian.bakingapp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

public class StepperBlankFragment extends Fragment implements Step {
    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}
