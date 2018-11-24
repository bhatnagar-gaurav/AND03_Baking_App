package com.gauravbhatnagar.bakingappudacity.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.gauravbhatnagar.bakingappudacity.api.model.Recipe;

import java.util.List;

@Dao
public interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipes(List<Recipe> recipeList);

    @Query("SELECT * FROM recipes")
    LiveData<List<Recipe>> getRecipeListFromDb();

    @Query("SELECT * FROM recipes WHERE id = :id")
    LiveData<Recipe> getRecipeById(int id);
}
