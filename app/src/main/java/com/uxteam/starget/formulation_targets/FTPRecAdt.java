package com.uxteam.starget.formulation_targets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uxteam.starget.R;
import com.uxteam.starget.app_utils.DateUtils;
import com.uxteam.starget.bmob_sys_pkg.Target;

import java.util.List;

public class FTPRecAdt extends RecyclerView.Adapter<FTPRecVH> {
    private Context context;
    private List<Target> targets;

    public FTPRecAdt(Context context, List<Target> targets) {
        this.context = context;
        this.targets = targets;
    }

    @NonNull
    @Override
    public FTPRecVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.formulation_target_item,parent,false);
        FTPRecVH vh=new FTPRecVH(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull FTPRecVH holder, final int position) {
            holder.endTime.setText("结束时间："+DateUtils.getNextDay());
            holder.supervisor.setSelection(targets.get(position).getSelect());
            holder.supervisor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                     targets.get(i).setSelect(i);
                     notifyItemChanged(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            holder.targetcontent.setText(targets.get(position).getTargetContent());
    }

    @Override
    public int getItemCount() {
        return targets.size();
    }
}
