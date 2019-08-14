package com.uxteam.starget.im_sys;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uxteam.starget.R;

import java.util.List;

import cn.jpush.im.android.api.enums.MessageDirect;
import cn.jpush.im.android.api.model.Message;

public class ChatMsgListAdt extends RecyclerView.Adapter {
    private Context context;
    private List<Message> messages;

    public ChatMsgListAdt(Context context, List<Message> messages) {

        this.context = context;
        this.messages = messages;
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
            ((ChatMsgListReciverVH)holder).reciverMsgContent.setText(MsgUtils.getTextMsg(messages.get(position).toJson()));

        }else {
            ((ChatMsgListSenderVH)holder).senderMsgContent.setText(MsgUtils.getTextMsg(messages.get(position).toJson()));

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
