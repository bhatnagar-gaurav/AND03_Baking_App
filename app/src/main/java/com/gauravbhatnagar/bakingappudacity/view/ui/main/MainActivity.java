package com.gauravbhatnagar.bakingappudacity.view.ui.main;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.gauravbhatnagar.bakingappudacity.R;
import com.gauravbhatnagar.bakingappudacity.api.model.Recipe;
import com.gauravbhatnagar.bakingappudacity.interfaces.RecipeItemClickListener;
import com.gauravbhatnagar.bakingappudacity.repo.PreferencesRepository;
import com.gauravbhatnagar.bakingappudacity.view.adapter.RecipeAdapter;
import com.gauravbhatnagar.bakingappudacity.view.ui.MainViewModel;
import com.gauravbhatnagar.bakingappudacity.view.ui.detail.DetailsActivity;
import com.gauravbhatnagar.bakingappudacity.view.ui.widget.RecipeIngredientsWidget;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import okhttp3.OkHttpClient;

public class MainActivity extends DaggerAppCompatActivity implements RecipeItemClickListener {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    PreferencesRepository pref;
    @Inject
    OkHttpClient okHttpClient;

    private MainViewModel mainViewModel;
    private RecipeAdapter adapter;
    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private boolean isWidget = false;
    private boolean isTablet = false;

    private RecyclerView rvRecipes;

    private void initView() {
        setContentView(R.layout.activity_main);
        rvRecipes = findViewById(R.id.rv_recipes);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        isTablet = getResources().getBoolean(R.bool.isTablet);
        mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);
        adapter = new RecipeAdapter(this);

        processIntent();
        setUpRecipesRecyclerView();
    }

    private void setUpRecipesRecyclerView() {
        rvRecipes.setAdapter(adapter);
        if (!isTablet) {
            rvRecipes.setLayoutManager(new LinearLayoutManager(this));
        } else {
            rvRecipes.setLayoutManager(new GridLayoutManager(this, 2));
        }
        rvRecipes.setHasFixedSize(true);

        mainViewModel.getRecipeFromDb().observe(this, recipeList -> {
            if (recipeList != null && !recipeList.isEmpty()) {
                adapter.addRecipes(recipeList);
            } else {
                getRecipeFromApi();
            }
        });
    }

    private void processIntent() {
        if (getIntent().getExtras() != null) {
            checkIfWidget(getIntent().getExtras());
        }
    }

    private void checkIfWidget(Bundle extras) {
        appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        isWidget = appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID;
    }

    private void getRecipeFromApi() {
        mainViewModel.getRecipeLiveData().observe(this, apiResponse -> {
            if (apiResponse != null) {
                if (apiResponse.getResponse() != null) {
                    adapter.addRecipes(apiResponse.getResponse());
                } else {
                    Toast.makeText(this, R.string.recipie_not_found_message, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, R.string.no_internet_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRecipeItemClick(int id) {
        if (isWidget) {
            mainViewModel.getRecipeById(id).observe(this, recipe -> {
                if (recipe != null) {
                    updateWidget(recipe);
                }
            });
        } else {
            startActivity(DetailsActivity.newIntent(this, id));
        }
    }

    private void updateWidget(Recipe recipe) {
        pref.saveIngredientsWidgetRecipe(appWidgetId, recipe);
        RecipeIngredientsWidget.updateAppWidget(this, AppWidgetManager.getInstance(this), appWidgetId, recipe);
        Intent resultIntent = new Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }
}
