package com.uxteam.starget.im_sys;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
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

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
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
        final String[] url = {null};
        BmobQuery<User> query=new BmobQuery<>();
        query.addWhereEqualTo("username",username);
        query.findObjects(new FindListener<User>(){
            @Override
            public void done(List<User> list, BmobException e) {
                if (e!=null||list.get(0)==null){
                    Log.i("HasNull","--------");
                }
                else{
                    if (list.get(0).getAvatarUri()!=null)
                        url[0] ="http://"+list.get(0).getAvatarUri();
                }}
        });
        if (getItemViewType(position)==0){
            Glide.with(context).load(url[0]).error(R.drawable.aurora_headicon_default).into(((ChatMsgListReciverVH)holder).reciverHeadImg);
            ((ChatMsgListReciverVH)holder).reciverMsgContent.setText(MsgUtils.getTextMsg(messages.get(position).toJson()));
        }else {
            ((ChatMsgListSenderVH)holder).senderMsgContent.setText(MsgUtils.getTextMsg(messages.get(position).toJson()));
            Glide.with(context).load("http://"+BmobUser.getCurrentUser(User.class).getAvatarUri()).error(R.drawable.aurora_headicon_default).into(((ChatMsgListSenderVH)holder).senderHeadImg);
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
