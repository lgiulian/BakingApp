package com.lgiulian.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lgiulian.bakingapp.model.BakingStep;
import com.lgiulian.bakingapp.model.Recipe;
import com.lgiulian.bakingapp.utils.Utils;

import java.util.List;

/**
 * Created by iulian on 3/26/2018.
 */

public class RecipeDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<BakingStep>> {
    private static final int STEPS_LOADER_ID = 111;

    private OnRecipeStepClickListener mCallback;
    private Recipe mRecipe;

    private RecyclerView mRecipeStepsRV;
    private RecipeStepsAdapter mRecipeStepsAdapter;
    private TextView mErrorMessageTv;
    private ProgressBar mLoadingIndicatorPb;

    public interface OnRecipeStepClickListener {
        void onRecipeStepSelected(int position);
    }

    public RecipeDetailsFragment() {
    }

    public void setRecipe(Recipe recipe) {
        this.mRecipe = recipe;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnRecipeStepClickListener) context;
        } catch (ClassCastException ex) {
            throw new ClassCastException(context.toString() + " must implement OnRecipeStepClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable(MainActivity.RECIPE_KEY);
        }
        View rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);

        TextView ingredientTV = rootView.findViewById(R.id.tv_recipe_ingredients);
        if (mRecipe != null) {
            ingredientTV.setText(Utils.getIngredientsPrettyFormat(getContext(), mRecipe));
        }
        
        mErrorMessageTv = rootView.findViewById(R.id.tv_error_message);
        mLoadingIndicatorPb = rootView.findViewById(R.id.pb_loading_indicator);

        mRecipeStepsRV = rootView.findViewById(R.id.rv_recipe_steps);
        mRecipeStepsRV.setHasFixedSize(false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecipeStepsRV.setLayoutManager(layoutManager);

        mRecipeStepsAdapter = new RecipeStepsAdapter(mCallback);

        mRecipeStepsRV.setAdapter(mRecipeStepsAdapter);

        getActivity().getSupportLoaderManager().initLoader(STEPS_LOADER_ID, null, this);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(MainActivity.RECIPE_KEY, mRecipe);
    }

    @NonNull
    @Override
    public Loader<List<BakingStep>> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<List<BakingStep>>(getContext()) {
            private List<BakingStep> mSteps;

            @Nullable
            @Override
            public List<BakingStep> loadInBackground() {
                return mRecipe.getSteps();
            }

            @Override
            protected void onStartLoading() {
                if (mSteps != null) {
                    deliverResult(mSteps);
                } else {
                    mLoadingIndicatorPb.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public void deliverResult(@Nullable List<BakingStep> data) {
                mSteps = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<BakingStep>> loader, List<BakingStep> data) {
        mLoadingIndicatorPb.setVisibility(View.INVISIBLE);
        mRecipeStepsAdapter.setStepsData(data);
        if (data == null) {
            showErrorMessage();
        } else {
            showStepsList();
        }
    }

    private void showStepsList() {
        mRecipeStepsRV.setVisibility(View.VISIBLE);
        mErrorMessageTv.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage() {
        mRecipeStepsRV.setVisibility(View.INVISIBLE);
        mErrorMessageTv.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<BakingStep>> loader) {

    }

}
