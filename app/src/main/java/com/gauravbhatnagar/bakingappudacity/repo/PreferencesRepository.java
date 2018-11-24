package com.gauravbhatnagar.bakingappudacity.repo;

import android.content.SharedPreferences;

import com.gauravbhatnagar.bakingappudacity.api.model.Recipe;
import com.google.gson.Gson;

import java.lang.reflect.Type;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.gauravbhatnagar.bakingappudacity.utils.AppConstants.WIDGET_PREFIX;

@Singleton
public class PreferencesRepository {

    private SharedPreferences prefs;
    private Gson gson;
    private Type type;

    @Inject
    public PreferencesRepository(SharedPreferences prefs, Gson gson, Type type) {
        this.prefs = prefs;
        this.gson = gson;
        this.type = type;
    }

    public void saveIngredientsWidgetRecipe(int appWidgetId, Recipe recipe) {
        prefs.edit()
                .putString(WIDGET_PREFIX + appWidgetId, gson.toJson(recipe, type))
                .apply();
    }

    public Recipe getIngredientsWidgetRecipe(int appWidgetId) {
        String recipeJson = prefs.getString(WIDGET_PREFIX + appWidgetId, null);
        if (recipeJson == null) return null;
        return gson.fromJson(recipeJson, type);
    }

    public void deleteIngredientsWidgetRecipe(int appWidgetId) {
        prefs.edit()
                .remove(WIDGET_PREFIX + appWidgetId)
                .apply();
    }
}
