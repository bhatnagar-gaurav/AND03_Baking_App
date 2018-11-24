package com.gauravbhatnagar.bakingappudacity.di.modules;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.gauravbhatnagar.bakingappudacity.di.interfaces.ViewModelKey;
import com.gauravbhatnagar.bakingappudacity.factory.ViewModelFactory;
import com.gauravbhatnagar.bakingappudacity.view.ui.MainViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    abstract ViewModel bindMainViewModel(MainViewModel mainViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
