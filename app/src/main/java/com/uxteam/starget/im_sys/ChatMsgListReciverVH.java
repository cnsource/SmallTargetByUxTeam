package com.uxteam.starget.im_sys;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uxteam.starget.R;

import cn.jiguang.imui.view.CircleImageView;

public class ChatMsgListReciverVH extends RecyclerView.ViewHolder {
    public CircleImageView reciverHeadImg;
    public TextView reciverNickName;
    public TextView reciverMsgContent;
    public ChatMsgListReciverVH(@NonNull View itemView) {
        super(itemView);
        reciverHeadImg= itemView.findViewById(R.id.char_reciver_head);
        reciverNickName= itemView.findViewById(R.id.chat_reciver_nickname);
        reciverMsgContent= itemView.findViewById(R.id.chat_reciver_msgcontent);
    }
}
