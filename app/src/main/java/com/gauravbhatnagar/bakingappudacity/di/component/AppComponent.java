package com.gauravbhatnagar.bakingappudacity.di.component;

import android.app.Application;

import com.gauravbhatnagar.bakingappudacity.BakingApp;
import com.gauravbhatnagar.bakingappudacity.di.modules.ActivityModule;
import com.gauravbhatnagar.bakingappudacity.di.modules.AppModule;
import com.gauravbhatnagar.bakingappudacity.di.modules.BroadcastReceiverModule;
import com.gauravbhatnagar.bakingappudacity.di.modules.FragmentModule;
import com.gauravbhatnagar.bakingappudacity.di.modules.ServiceModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules =
        {
                ActivityModule.class,
                FragmentModule.class,
                ServiceModule.class,
                BroadcastReceiverModule.class,
                AppModule.class,
                AndroidSupportInjectionModule.class
        }
)
interface AppComponent extends AndroidInjector<BakingApp> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}
