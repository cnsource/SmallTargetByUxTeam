package com.uxteam.starget.im_sys;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.uxteam.starget.R;

import java.util.List;

import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

public class NewFrendAdt extends RecyclerView.Adapter<NewFrendVH> {
    public NewFrendAdt(Context context, List<UserInfo> userInfos) {
        this.context = context;
        this.userInfos = userInfos;
    }

    private Context context;
    private List<UserInfo> userInfos;

    @NonNull
    @Override
    public NewFrendVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.activity_new_frend_request_item,parent,false);
        NewFrendVH frendVH=new NewFrendVH(view);
        return frendVH;
    }

    @Override
    public void onBindViewHolder(@NonNull NewFrendVH holder, final int position) {
        holder.username.setText(userInfos.get(position).getNickname());
        Glide.with(context).load(userInfos.get(position).getAvatar()).into(holder.headImg);
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ContactManager.acceptInvitation(userInfos.get(position).getUserName(),null, new BasicCallback() {
                    @Override
                    public void gotResult(int i, java.lang.String s) {
                        if (0 == i) {
                            //接收好友请求成功
                            Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
                        } else {
                            //接收好友请求失败
                            Log.e("接受好友请求失败",s);
                        }
                    }
                });
            }
        });
        holder.refause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ContactManager.declineInvitation(userInfos.get(position).getUserName(), null, null, new BasicCallback() {
                    @Override
                    public void gotResult(int responseCode, String responseMessage) {
                        if (0 == responseCode) {
                            Toast.makeText(context, "已拒绝", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("拒绝好友请求失败",responseMessage);
                        }
                    }
                });
            }
        });
    }
    @Override
    public int getItemCount() {
        return userInfos.size();
    }
}
