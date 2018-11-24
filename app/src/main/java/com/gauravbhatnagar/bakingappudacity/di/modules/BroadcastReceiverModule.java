package com.gauravbhatnagar.bakingappudacity.di.modules;

import com.gauravbhatnagar.bakingappudacity.view.ui.widget.RecipeIngredientsWidget;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BroadcastReceiverModule {

    @ContributesAndroidInjector
    abstract RecipeIngredientsWidget contributeRecipeIngredientsWidget();
}
