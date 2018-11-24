package com.gauravbhatnagar.bakingappudacity.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gauravbhatnagar.bakingappudacity.R;
import com.gauravbhatnagar.bakingappudacity.api.model.Step;
import com.gauravbhatnagar.bakingappudacity.interfaces.StepSelectedListener;

import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.MyViewHolder> {

    private List<Step> stepList;
    private Context context;
    private StepSelectedListener selectedListener;

    public StepsAdapter(Context context) {
        this.context = context;
    }

    public void addSteps(List<Step> stepList) {
        this.stepList = stepList;
        notifyDataSetChanged();
    }

    public void addStepClickListener(StepSelectedListener stepSelectedListener) {
        this.selectedListener = stepSelectedListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StepsAdapter.MyViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.list_item_step, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bindTo(stepList.get(position));
    }

    @Override
    public int getItemCount() {
        if (stepList == null) {
            return 0;
        } else {
            return stepList.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txvStepDescription;

        MyViewHolder(View view) {
            super(view);
            txvStepDescription = view.findViewById(R.id.txv_step_description);
        }

        void bindTo(Step step) {
            txvStepDescription.setText(step.getShortDescription());
            txvStepDescription.setOnClickListener(view -> selectedListener.stepSelected(step));
        }
    }
}
