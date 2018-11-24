package com.gauravbhatnagar.bakingappudacity.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gauravbhatnagar.bakingappudacity.R;
import com.gauravbhatnagar.bakingappudacity.api.model.Recipe;
import com.gauravbhatnagar.bakingappudacity.interfaces.RecipeItemClickListener;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.MyViewHolder> {

    private List<Recipe> recipeList;
    private Context context;
    private final RecipeItemClickListener itemClickListener;

    public RecipeAdapter(Context context) {
        this.context = context;
        try {
            this.itemClickListener = ((RecipeItemClickListener) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement the interfaces");
        }
    }

    public void addRecipes(List<Recipe> recipeList) {
        this.recipeList = recipeList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.list_item_recipe, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bindTo(recipeList.get(position));
    }

    @Override
    public int getItemCount() {
        if (recipeList == null) {
            return 0;
        } else {
            return recipeList.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txvRecipe;
        private TextView txvServings;
        private CardView cvRecipe;

        MyViewHolder(View view) {
            super(view);
            txvRecipe = view.findViewById(R.id.txv_recipe);
            txvServings = view.findViewById(R.id.txv_serving);
            cvRecipe = view.findViewById(R.id.cv_recipe);
        }

        void bindTo(Recipe recipe) {
            txvRecipe.setText(recipe.getName());
            txvServings.setText(String.valueOf(recipe.getServings()));
            cvRecipe.setOnClickListener(view -> itemClickListener.onRecipeItemClick(recipe.getId()));
        }
    }
}
