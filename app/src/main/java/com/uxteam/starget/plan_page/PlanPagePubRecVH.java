package com.uxteam.starget.plan_page;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uxteam.starget.R;

import cn.jiguang.imui.view.CircleImageView;

public class PlanPagePubRecVH extends RecyclerView.ViewHolder {
    public TextView startTime;
    public TextView supervisor;
    public TextView targetContent;
    public TextView endTime;
    public CircleImageView itemhead;
    public Button submitbtn;
    public ImageView targetimg;
    public PlanPagePubRecVH(@NonNull View itemView) {
        super(itemView);
        bindView(itemView);
    }

    private void bindView(View itemView) {
        targetimg= itemView.findViewById(R.id.targetimg);
        itemhead= itemView.findViewById(R.id.plan_target_item_head);
        submitbtn= itemView.findViewById(R.id.submitBtn);
        startTime = itemView.findViewById(R.id.starttime);
        supervisor = itemView.findViewById(R.id.supervisor);
        targetContent = itemView.findViewById(R.id.target_Content);
        endTime = itemView.findViewById(R.id.endtime);
    }
}
