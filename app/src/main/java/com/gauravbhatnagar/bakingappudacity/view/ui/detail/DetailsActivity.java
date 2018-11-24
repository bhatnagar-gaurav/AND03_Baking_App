package com.gauravbhatnagar.bakingappudacity.view.ui.detail;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.gauravbhatnagar.bakingappudacity.R;
import com.gauravbhatnagar.bakingappudacity.api.model.Step;
import com.gauravbhatnagar.bakingappudacity.interfaces.StepButtonClickListener;
import com.gauravbhatnagar.bakingappudacity.interfaces.StepSelectedListener;
import com.gauravbhatnagar.bakingappudacity.view.ui.MainViewModel;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import okhttp3.OkHttpClient;

import static com.gauravbhatnagar.bakingappudacity.utils.AppConstants.RECIPE_ID;
import static com.gauravbhatnagar.bakingappudacity.utils.AppConstants.SELECTED_STEP;

public class DetailsActivity extends DaggerAppCompatActivity implements StepSelectedListener, StepButtonClickListener {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    OkHttpClient okHttpClient;

    private MainViewModel mainViewModel;

    private static final String TAG_STEPS_FRAGMENT = "steps_fragment";
    private static final String TAG_DETAIL_FRAGMENT = "detail_fragment";
    private static final String KEY_SELECTED_STEP = "key_selected_step";
    private int stepSize = -1;
    private int selectedStepIndex = -1;
    private int recipeId;
    private boolean isLayoutMultiPane;

    private FrameLayout fragmentContainer;
    private FrameLayout fragmentDetail;
    private TextView selectStep;

    public static Intent newIntent(Context context, int recipeId) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra("id", recipeId);
        return intent;
    }

    private void initView() {
        setContentView(R.layout.activity_details);
        fragmentContainer = findViewById(R.id.fragment_container);
        fragmentDetail = findViewById(R.id.fragment_detail);
        selectStep = findViewById(R.id.select_step);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);

        if (savedInstanceState != null) {
            recipeId = savedInstanceState.getInt(RECIPE_ID);
            selectedStepIndex = savedInstanceState.getInt(SELECTED_STEP);
        } else {
            recipeId = getIntent().getIntExtra("id", 0);
        }

        mainViewModel.getRecipeById(recipeId).observe(this, recipe -> {
            if (recipe != null) {
                mainViewModel.saveStepsLiveData(recipe);
                mainViewModel.saveIngredients(recipe);
                stepSize = recipe.getSteps().size();
                setupLayout();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(RECIPE_ID, recipeId);
        outState.putInt(SELECTED_STEP, selectedStepIndex);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            recipeId = savedInstanceState.getInt(RECIPE_ID);
            selectedStepIndex = savedInstanceState.getInt(SELECTED_STEP);
        }
    }

    private void setupLayout() {
        isLayoutMultiPane = fragmentDetail != null;

        getSupportFragmentManager().addOnBackStackChangedListener(this::fragmentTransaction);

        if (getSupportFragmentManager().findFragmentByTag(TAG_STEPS_FRAGMENT) == null) {
            showStepsListFragment();
        } else {
            Fragment oldFragment = getSupportFragmentManager().findFragmentByTag(TAG_DETAIL_FRAGMENT);
            if (oldFragment != null) {
                getSupportFragmentManager().popBackStack();
            }
        }
    }

    private void showStepsListFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(fragmentContainer.getId(), StepListFragment.newInstance(), TAG_STEPS_FRAGMENT)
                .commit();
    }

    private void replaceFragment(Fragment fragment) {
        int containerId = fragmentContainer.getId();
        if (isLayoutMultiPane) containerId = fragmentDetail.getId();
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(containerId, fragment, TAG_DETAIL_FRAGMENT)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void stepSelected(Step step) {
        mainViewModel.selectStep(step);
        mainViewModel.getCurrentStep().observe(this, currentStep -> {
            if (currentStep != null) {
                selectedStepIndex = currentStep;
            }
        });
        Fragment stepDetailsFragment = StepDetailsFragment.newInstance();
        replaceFragment(stepDetailsFragment);
    }

    @Override
    public void onPreviousStep() {
        if (selectedStepIndex - 1 < 0) {
            Log.d(KEY_SELECTED_STEP, "No previous step ");
        } else {
            selectedStepIndex = selectedStepIndex - 1;
            mainViewModel.getStepById(selectedStepIndex).observe(this, step -> {
                if (step != null) {
                    mainViewModel.selectStep(step);
                    Fragment stepDetailsFragment = StepDetailsFragment.newInstance();
                    replaceFragment(stepDetailsFragment);
                }
            });
        }
    }

    @Override
    public void onNextStep() {
        if (selectedStepIndex + 1 > stepSize - 1) {
            Log.d(KEY_SELECTED_STEP, "No next step ");
        } else {
            selectedStepIndex = selectedStepIndex + 1;
            mainViewModel.getStepById(selectedStepIndex).observe(this, step -> {
                if (step != null) {
                    mainViewModel.selectStep(step);
                    Fragment stepDetailsFragment = StepDetailsFragment.newInstance();
                    replaceFragment(stepDetailsFragment);
                }
            });
        }
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    private void fragmentTransaction() {
        if (isLayoutMultiPane) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_DETAIL_FRAGMENT);
            if (fragment == null) {
                selectStep.setVisibility(View.VISIBLE);
            } else {
                selectStep.setVisibility(View.GONE);
            }
        }
    }
}
