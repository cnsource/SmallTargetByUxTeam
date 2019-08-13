package com.uxteam.starget.im_sys;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uxteam.starget.R;

import cn.jiguang.imui.view.CircleImageView;

public class ChatMsgListSenderVH extends RecyclerView.ViewHolder {
    public CircleImageView senderHeadImg;
    public TextView senderNickName;
    public TextView senderMsgContent;

    public ChatMsgListSenderVH(@NonNull View itemView) {
        super(itemView);
        senderHeadImg = itemView.findViewById(R.id.char_sender_head);
        senderNickName = itemView.findViewById(R.id.chat_sender_nickname);
        senderMsgContent = itemView.findViewById(R.id.chat_sender_msgcontent);
    }
}
