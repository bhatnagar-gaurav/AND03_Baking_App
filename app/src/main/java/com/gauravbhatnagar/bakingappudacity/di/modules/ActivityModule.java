package com.gauravbhatnagar.bakingappudacity.di.modules;

import com.gauravbhatnagar.bakingappudacity.view.ui.detail.DetailsActivity;
import com.gauravbhatnagar.bakingappudacity.view.ui.main.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract DetailsActivity contributeDetailsActivity();

}
