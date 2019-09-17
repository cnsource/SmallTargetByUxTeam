package com.uxteam.starget.im_sys;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.uxteam.starget.R;
import com.uxteam.starget.app_utils.UPYunUtils;
import com.uxteam.starget.bmob_sys_pkg.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.UserInfo;
import kotlin.ranges.UIntProgression;

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
    public void onBindViewHolder(@NonNull final FrendVH holder,final int position) {
        BmobQuery<User> query=new BmobQuery<>();
        query.addWhereEqualTo("username",users.get(position).getUserName());
        query.findObjects(new FindListener<User>(){
            @Override
            public void done(List<User> list, BmobException e) {
                if (e!=null||list.get(0)==null){
                    Log.i("HasNull","--------");
                }
                else{
                    if (list.get(0).getAvatarUri()!=null)
                        Glide.with(context).load("http://"+list.get(0).getAvatarUri()).error(R.drawable.aurora_headicon_default).into(holder.circleImageView);
                }}
        });
        final Conversation conversation = Conversation.createSingleConversation(users.get(position).getUserName(), null);
        setTipCnt(conversation.getUnReadMsgCnt(),holder.unreadMsg);
        conversation.getUnReadMsgCnt();
        holder.nickName.setText(users.get(position).getDisplayName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                conversation.resetUnreadCount();
            onclickListener.click(users.get(position).getUserName(),users.get(position).getDisplayName());
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
    private void setTipCnt(int cnt, TextView view){
        view.setVisibility(View.VISIBLE);
        if (cnt>99)
            view.setText("99+");
        else if (cnt>0)
            view.setText(cnt+"");
        else
            view.setVisibility(View.INVISIBLE);
    }
}

interface OnclickListener{
    void click(String username,String displayname);
}
