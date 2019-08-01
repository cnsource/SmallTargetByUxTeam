package com.uxteam.starget.plan_page;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PlanPageRecAdt extends RecyclerView.Adapter<PlanPageRecVH> {
    private Context context;

    public PlanPageRecAdt(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public PlanPageRecVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull PlanPageRecVH holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
