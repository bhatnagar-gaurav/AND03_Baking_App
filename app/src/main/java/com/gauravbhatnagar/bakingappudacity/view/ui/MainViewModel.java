package com.gauravbhatnagar.bakingappudacity.view.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.gauravbhatnagar.bakingappudacity.api.ApiResponse;
import com.gauravbhatnagar.bakingappudacity.api.model.Ingredient;
import com.gauravbhatnagar.bakingappudacity.api.model.Recipe;
import com.gauravbhatnagar.bakingappudacity.api.model.Step;
import com.gauravbhatnagar.bakingappudacity.repo.BakingRepository;

import java.util.List;

import javax.inject.Inject;

public class MainViewModel extends ViewModel {

    private BakingRepository bakingRepo;
    private MutableLiveData<ApiResponse<List<Recipe>>> recipeLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> stepSizeLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> currentStepLiveData = new MutableLiveData<>();
    private MutableLiveData<Step> stepLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Step>> stepListLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Ingredient>> ingredientsLiveData = new MutableLiveData<>();

    @Inject
    MainViewModel(BakingRepository bakingRepo) {
        this.bakingRepo = bakingRepo;
        bakingRepo.fetchRecipes().observeForever(recipeList -> recipeLiveData.setValue(recipeList));
    }

    public MutableLiveData<ApiResponse<List<Recipe>>> getRecipeLiveData() {
        return recipeLiveData;
    }

    public LiveData<Recipe> getRecipeById(int id) {
        return bakingRepo.getRecipeById(id);
    }

    public LiveData<List<Recipe>> getRecipeFromDb() {
        return bakingRepo.getRecipesFromDb();
    }

    // We press something from master fragment
    public void selectStep(Step step) {
        stepLiveData.setValue(step);
        currentStepLiveData.setValue(step.getId());
    }

    // Get that data in the detail fragment
    public LiveData<Step> getSelectedStep() {
        return stepLiveData;
    }

    // Save Step when we get to Details Activity
    public void saveStepsLiveData(Recipe recipe) {
        stepListLiveData.setValue(recipe.getSteps());
        stepSizeLiveData.setValue(recipe.getSteps().size());
    }

    public void saveIngredients(Recipe recipe) {
        ingredientsLiveData.setValue(recipe.getIngredients());
    }

    public LiveData<List<Step>> getSteps() {
        return stepListLiveData;
    }

    public LiveData<Integer> getStepsSize() {
        return stepSizeLiveData;
    }

    public LiveData<Integer> getCurrentStep() {
        return currentStepLiveData;
    }

    public LiveData<List<Ingredient>> getIngredients() {
        return ingredientsLiveData;
    }

    public LiveData<Step> getStepById(int id) {
        MutableLiveData<Step> stepById = new MutableLiveData<>();
        if (stepListLiveData.getValue().size() >= id) {
            stepById.setValue(stepListLiveData.getValue().get(id));
        } else {
            stepById.setValue(null);
        }
        return stepById;
    }
}
