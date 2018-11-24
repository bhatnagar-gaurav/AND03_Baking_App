package com.gauravbhatnagar.bakingappudacity.view.ui.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;


import com.gauravbhatnagar.bakingappudacity.R;
import com.gauravbhatnagar.bakingappudacity.api.model.Ingredient;
import com.gauravbhatnagar.bakingappudacity.repo.PreferencesRepository;

import java.util.List;

public class IngredientsViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private String packageName;
    private List<Ingredient> ingredientList;

    IngredientsViewsFactory(String packageName, Intent intent, PreferencesRepository prefRepo) {
        this.packageName = packageName;
        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        ingredientList = prefRepo.getIngredientsWidgetRecipe(appWidgetId).getIngredients();
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return ingredientList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Ingredient ingredient = ingredientList.get(position);

        final RemoteViews remoteView = new RemoteViews(packageName, R.layout.widget_recipe_ingrediants_row);
        remoteView.setTextViewText(R.id.txv_ingredient, ingredient.getIngredient());

        String quantity = ingredient.getQuantity() + " " + ingredient.getMeasure();
        remoteView.setTextViewText(R.id.txv_quantity, quantity);

        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
