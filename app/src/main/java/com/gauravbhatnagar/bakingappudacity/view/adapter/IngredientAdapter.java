package com.gauravbhatnagar.bakingappudacity.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.gauravbhatnagar.bakingappudacity.R;
import com.gauravbhatnagar.bakingappudacity.api.model.Ingredient;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.MyViewHolder> {

    private List<Ingredient> ingredientList;
    private Context context;

    public IngredientAdapter(Context context) {
        this.context = context;
    }

    public void addIngredients(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new IngredientAdapter.MyViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.list_item_ingrediant, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bindTo(ingredientList.get(position));
    }

    @Override
    public int getItemCount() {
        if (ingredientList == null) {
            return 0;
        } else {
            return ingredientList.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txvIngredient;

        MyViewHolder(View view) {
            super(view);
            txvIngredient = view.findViewById(R.id.txv_ingredient);
        }

        void bindTo(Ingredient ingredient) {
            txvIngredient.setText(String.format("●  %s (%s %s)", ingredient.getIngredient(), ingredient.getQuantity(), ingredient.getMeasure()));
        }
    }
}
