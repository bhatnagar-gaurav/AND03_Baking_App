package com.gauravbhatnagar.bakingappudacity.repo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.gauravbhatnagar.bakingappudacity.api.ApiInterface;
import com.gauravbhatnagar.bakingappudacity.api.ApiResponse;
import com.gauravbhatnagar.bakingappudacity.api.model.Recipe;
import com.gauravbhatnagar.bakingappudacity.db.dao.RecipeDao;
import com.gauravbhatnagar.bakingappudacity.utils.AppExecutor;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class BakingRepository {
    private final ApiInterface apiInterface;
    private final RecipeDao dao;
    private final AppExecutor executor;

    @Inject
    public BakingRepository(ApiInterface apiInterface, RecipeDao dao, AppExecutor executor) {
        this.apiInterface = apiInterface;
        this.dao = dao;
        this.executor = executor;
    }

    public LiveData<ApiResponse<List<Recipe>>> fetchRecipes() {
        final MutableLiveData<ApiResponse<List<Recipe>>> liveData = new MutableLiveData<>();
        Call<List<Recipe>> call = apiInterface.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(new ApiResponse<>(response.body()));
                    if (!response.body().isEmpty()) {
                        liveData.observeForever(
                                recipeList -> executor.diskIO().execute(() -> dao.insertRecipes(response.body()))
                        );
                    } else {
                        liveData.setValue(null);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                liveData.setValue(new ApiResponse<>(t));
            }
        });
        return liveData;
    }

    public LiveData<List<Recipe>> getRecipesFromDb() {
        return dao.getRecipeListFromDb();
    }

    public LiveData<Recipe> getRecipeById(int id) {
        return dao.getRecipeById(id);
    }
}
