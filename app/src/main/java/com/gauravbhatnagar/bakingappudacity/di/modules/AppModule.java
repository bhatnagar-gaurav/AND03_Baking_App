package com.gauravbhatnagar.bakingappudacity.di.modules;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.gauravbhatnagar.bakingappudacity.api.ApiInterface;
import com.gauravbhatnagar.bakingappudacity.api.model.Recipe;
import com.gauravbhatnagar.bakingappudacity.db.BakingDb;
import com.gauravbhatnagar.bakingappudacity.db.dao.RecipeDao;
import com.gauravbhatnagar.bakingappudacity.repo.BakingRepository;
import com.gauravbhatnagar.bakingappudacity.repo.PreferencesRepository;
import com.gauravbhatnagar.bakingappudacity.utils.AppConstants;
import com.gauravbhatnagar.bakingappudacity.utils.AppExecutor;
import com.gauravbhatnagar.bakingappudacity.utils.MainThreadExecutor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
public class AppModule {

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    BakingRepository provideRepository(ApiInterface apiInterface, RecipeDao recipeDao, AppExecutor executor) {
        return new BakingRepository(apiInterface, recipeDao, executor);
    }

    @Provides
    @Singleton
    PreferencesRepository providePreferencesRepository(SharedPreferences preferences, Gson gson, Type type) {
        return new PreferencesRepository(preferences, gson, type);
    }

    @Provides
    AppExecutor provideAppExecutor() {
        return new AppExecutor(Executors.newSingleThreadExecutor(), new MainThreadExecutor());
    }

    @Provides
    @Singleton
    BakingDb provideDatabase(Application application) {
        return Room.databaseBuilder(application, BakingDb.class, AppConstants.DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @Singleton
    RecipeDao provideDao(BakingDb database) {
        return database.recipeDao();
    }

    @Provides
    @Singleton
    ApiInterface provideApiInterface(Retrofit retrofit) {
        return retrofit.create(ApiInterface.class);
    }

    @Provides
    Retrofit provideRetrofit(OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(AppConstants.URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder().create();
    }

    @Provides
    @Singleton
    Type provideType() {
        return new TypeToken<Recipe>() {}.getType();
    }

    @Provides
    OkHttpClient okHttpClient(HttpLoggingInterceptor httpLoggingInterceptor) {
        return new OkHttpClient()
                .newBuilder()
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @Provides
    HttpLoggingInterceptor httpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

}
