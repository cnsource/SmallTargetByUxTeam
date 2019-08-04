package com.uxteam.starget.plan_page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uxteam.starget.R;
import com.uxteam.starget.app_utils.DateUtils;
import com.uxteam.starget.bmob_sys_pkg.Target;

import java.util.List;

public class PlanPageRecAdt extends RecyclerView.Adapter<PlanPageRecVH> {
    private Context context;
    private List<Target> targets;
    private int ViewSign;

    public PlanPageRecAdt(Context context, List<Target> targets) {
        this.context = context;
        this.targets = targets;
    }

    @NonNull
    @Override
    public PlanPageRecVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.plan_page_targetitem,parent,false);
        PlanPageRecVH pageRecVH=new PlanPageRecVH(view);
        return pageRecVH;
    }

    @Override
    public void onBindViewHolder(@NonNull PlanPageRecVH holder, int position) {
            holder.startTime.setText(targets.get(position).getCreatedAt());
            if (ViewSign==0){
                 holder.supervisor.append(targets.get(position).getSupervisor().getNickName());
            }else {
                 holder.supervisor.setVisibility(View.GONE);
            }
            holder.targetContent.setText(targets.get(position).getTargetContent());
            holder.endTime.setText(DateUtils.getNextDay());
    }
    public  void setViewSign(int ViewSign){
        this.ViewSign=ViewSign;
    }
    @Override
    public int getItemCount() {
        return targets.size();
    }
}
