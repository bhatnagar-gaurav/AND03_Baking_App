package com.gauravbhatnagar.bakingappudacity.view.ui.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.gauravbhatnagar.bakingappudacity.R;
import com.gauravbhatnagar.bakingappudacity.api.model.Recipe;
import com.gauravbhatnagar.bakingappudacity.repo.PreferencesRepository;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class RecipeIngredientsWidget extends AppWidgetProvider {

    @Inject
    PreferencesRepository preferencesRepository;

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Recipe recipe) {
        Intent intent = IngredientsListWidgetService.createIntent(context, appWidgetId);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_recipe_ingrediants);
        views.setTextViewText(R.id.txv_recipe_name, recipe.getName());
        views.setRemoteAdapter(R.id.lv_ingredient, intent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AndroidInjection.inject(this, context);
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int appWidgetId : appWidgetIds) {
            Recipe recipe = preferencesRepository.getIngredientsWidgetRecipe(appWidgetId);
            if (recipe == null) continue;
            updateAppWidget(context, appWidgetManager, appWidgetId, recipe);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            preferencesRepository.deleteIngredientsWidgetRecipe(appWidgetId);
        }
    }

}
