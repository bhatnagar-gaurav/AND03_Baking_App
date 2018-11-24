package com.gauravbhatnagar.bakingappudacity.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.gauravbhatnagar.bakingappudacity.api.model.Recipe;
import com.gauravbhatnagar.bakingappudacity.db.dao.RecipeDao;

@Database(entities = {Recipe.class}, version = 1, exportSchema = false)
public abstract class BakingDb extends RoomDatabase {

    public abstract RecipeDao recipeDao();
}
