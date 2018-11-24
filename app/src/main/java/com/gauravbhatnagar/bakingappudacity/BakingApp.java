package com.gauravbhatnagar.bakingappudacity;

import android.content.Context;

import com.gauravbhatnagar.bakingappudacity.di.component.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

public class BakingApp extends DaggerApplication {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent
                .builder()
                .application(BakingApp.this)
                .build();
    }
}
