package com.gauravbhatnagar.bakingappudacity.db.convertors;

import android.arch.persistence.room.TypeConverter;

import com.gauravbhatnagar.bakingappudacity.api.model.Ingredient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class IngredientConverter {

    @TypeConverter
    public String fromIngredientList(List<Ingredient> ingredientList) {
        if (ingredientList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Ingredient>>() {}.getType();
        return gson.toJson(ingredientList, type);
    }

    @TypeConverter
    public List<Ingredient> toIngredientList(String ingredientList) {
        if (ingredientList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Ingredient>>() {}.getType();
        return gson.fromJson(ingredientList, type);
    }
}
