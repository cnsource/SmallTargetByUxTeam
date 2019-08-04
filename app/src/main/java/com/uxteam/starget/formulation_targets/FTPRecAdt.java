package com.uxteam.starget.formulation_targets;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull FTPRecVH holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
