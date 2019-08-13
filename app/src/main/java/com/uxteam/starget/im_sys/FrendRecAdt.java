package com.uxteam.starget.im_sys;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.uxteam.starget.R;

import java.util.List;

import cn.jpush.im.android.api.model.UserInfo;

public class FrendRecAdt extends RecyclerView.Adapter<FrendVH> {
    private Context context;
    private List<UserInfo> users;

    private OnclickListener onclickListener;

    public FrendRecAdt(Context context, List<UserInfo> users) {

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
        Glide.with(context).load(users.get(position).getAvatar()).error(R.drawable.aurora_headicon_default).into(holder.circleImageView);
        holder.nickName.setText(users.get(position).getDisplayName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            onclickListener.click();
            }

        });
        holder.signture.setText("该用户未设置签名");
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setOnclickListener(OnclickListener onclickListener) {
        this.onclickListener = onclickListener;
    }
}
interface OnclickListener{
    void click();
}
