package com.gauravbhatnagar.bakingappudacity.di.modules;


import com.gauravbhatnagar.bakingappudacity.view.ui.widget.IngredientsListWidgetService;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ServiceModule {

    @ContributesAndroidInjector
    abstract IngredientsListWidgetService contributeIngredientsListWidgetService();
}
