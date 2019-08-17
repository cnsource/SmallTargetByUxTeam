package com.uxteam.starget.im_sys;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.uxteam.starget.R;
import com.uxteam.starget.app_utils.MsgUtils;
import com.uxteam.starget.app_utils.UPYunUtils;
import com.uxteam.starget.bmob_sys_pkg.User;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.jpush.im.android.api.enums.MessageDirect;
import cn.jpush.im.android.api.model.Message;

public class ChatMsgListAdt extends RecyclerView.Adapter {
    private Context context;
    private List<Message> messages;
    private String username;

    public ChatMsgListAdt(Context context, List<Message> messages, String username) {

        this.context = context;
        this.messages = messages;
        this.username = username;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType==0){
            view= LayoutInflater.from(context).inflate(R.layout.activity_chat_reciver,parent,false);
            return new ChatMsgListReciverVH(view);
        }else {
            view= LayoutInflater.from(context).inflate(R.layout.activity_chat_sender,parent,false);
            return new ChatMsgListSenderVH(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position)==0){
            Glide.with(context).load(UPYunUtils.getSourcePath("head",username,UPYunUtils.JPG)).error(R.drawable.aurora_headicon_default).into(((ChatMsgListReciverVH)holder).reciverHeadImg);
            ((ChatMsgListReciverVH)holder).reciverMsgContent.setText(MsgUtils.getTextMsg(messages.get(position).toJson()));
        }else {
            ((ChatMsgListSenderVH)holder).senderMsgContent.setText(MsgUtils.getTextMsg(messages.get(position).toJson()));
            Glide.with(context).load(UPYunUtils.getSourcePath("head", BmobUser.getCurrentUser(User.class).getUsername(),UPYunUtils.JPG)).error(R.drawable.aurora_headicon_default).into(((ChatMsgListSenderVH)holder).senderHeadImg);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getDirect()== MessageDirect.receive){
            return 0;
        }else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
