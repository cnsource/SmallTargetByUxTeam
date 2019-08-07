package com.uxteam.starget.im_sys;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uxteam.starget.R;
import com.uxteam.starget.bmob_sys_pkg.User;

import java.util.List;

public class FrendRecAdt extends RecyclerView.Adapter<FrendVH> {
    private Context context;
    private List<User> users;

    public FrendRecAdt(Context context, List<User> users) {

        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public FrendVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.frends_rec_item,parent,false);
        FrendVH frendVH=new FrendVH(view);
        return frendVH;
    }

    @Override
    public void onBindViewHolder(@NonNull FrendVH holder, int position) {

    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
