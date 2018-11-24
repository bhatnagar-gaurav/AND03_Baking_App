package com.gauravbhatnagar.bakingappudacity.view.ui.detail;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gauravbhatnagar.bakingappudacity.R;
import com.gauravbhatnagar.bakingappudacity.interfaces.StepSelectedListener;
import com.gauravbhatnagar.bakingappudacity.view.adapter.IngredientAdapter;
import com.gauravbhatnagar.bakingappudacity.view.adapter.StepsAdapter;
import com.gauravbhatnagar.bakingappudacity.view.ui.MainViewModel;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepListFragment extends DaggerFragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private MainViewModel mainViewModel;

    private IngredientAdapter ingredientAdapter;
    private StepsAdapter stepsAdapter;
    private StepSelectedListener stepSelectedListener;
    private RecyclerView rvIngredients, rvSteps;

    public StepListFragment() {
        // Required empty public constructor
    }

    public static StepListFragment newInstance() {
        Bundle args = new Bundle();
        StepListFragment fragment = new StepListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void initViews(View view) {
        rvIngredients = view.findViewById(R.id.rv_ingredients);
        rvSteps = view.findViewById(R.id.rv_steps);
    }

    private void initData() {
        ingredientAdapter = new IngredientAdapter(getActivity());
        stepsAdapter = new StepsAdapter(getActivity());
        stepsAdapter.addStepClickListener(stepSelectedListener);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StepSelectedListener) {
            stepSelectedListener = (StepSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement StepSelectedListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(MainViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_step_list, container, false);
        initViews(view);
        initData();

        ingredientAdapter = new IngredientAdapter(getActivity());
        stepsAdapter = new StepsAdapter(getActivity());

        rvIngredients.setAdapter(ingredientAdapter);
        rvIngredients.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvIngredients.setHasFixedSize(true);
        rvIngredients.setNestedScrollingEnabled(true);

        rvSteps.setAdapter(stepsAdapter);
        rvSteps.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvSteps.setHasFixedSize(true);
        rvSteps.setNestedScrollingEnabled(true);

        stepsAdapter.addStepClickListener(stepSelectedListener);

        mainViewModel.getIngredients().observe(getActivity(), ingredientList -> {
            if (ingredientList != null && !ingredientList.isEmpty()) {
                ingredientAdapter.addIngredients(ingredientList);
            }
        });

        mainViewModel.getSteps().observe(getActivity(), stepList -> {
            if (stepList != null && !stepList.isEmpty()) {
                stepsAdapter.addSteps(stepList);
            }
        });

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        stepSelectedListener = null;
    }
}
