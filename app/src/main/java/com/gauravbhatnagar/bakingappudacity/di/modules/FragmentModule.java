package com.gauravbhatnagar.bakingappudacity.di.modules;

import com.gauravbhatnagar.bakingappudacity.view.ui.detail.StepDetailsFragment;
import com.gauravbhatnagar.bakingappudacity.view.ui.detail.StepListFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract StepListFragment contributeStepListFragment();

    @ContributesAndroidInjector
    abstract StepDetailsFragment contributeStepDetailsFragment();
}
