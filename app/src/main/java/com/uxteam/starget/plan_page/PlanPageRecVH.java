package com.uxteam.starget.plan_page;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uxteam.starget.R;

public class PlanPageRecVH extends RecyclerView.ViewHolder {
    public TextView startTime;
    public TextView supervisor;
    public TextView targetContent;
    public TextView endTime;

    public PlanPageRecVH(@NonNull View itemView) {
        super(itemView);
        bindView(itemView);
    }

    private void bindView(View itemView) {
        startTime = itemView.findViewById(R.id.starttime);
        supervisor = itemView.findViewById(R.id.supervisor);
        targetContent = itemView.findViewById(R.id.target_Content);
        endTime = itemView.findViewById(R.id.endtime);
    }
}
