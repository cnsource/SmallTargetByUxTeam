package com.uxteam.starget.im_sys;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.uxteam.starget.R;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.model.Message;

public class ChatActivityPresender {
    private ChatActivity chatActivity;
    private List<Message> messages=new ArrayList<>();
    public ChatActivityPresender(ChatActivity chatActivity) {

        this.chatActivity = chatActivity;
    }

    public ChatActivityPresender load(){
        chatActivity.bindViewEvent(clickListenerProvider(), refreshListenerProvider(),adtProvider(),layoutManagerProvider());

        return this;
    }
    private SwipeRefreshLayout.OnRefreshListener refreshListenerProvider(){
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        };
    }
    private LinearLayoutManager layoutManagerProvider(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(chatActivity);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        return layoutManager;
    }
    private View.OnClickListener clickListenerProvider(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.back:
                            chatActivity.close();
                        break;
                    case R.id.contact_person_info:

                        break;
                    case R.id.activity_chat_send_btn:

                        break;
                }
            }
        };
    }
    private ChatMsgListAdt adtProvider(){
        return new ChatMsgListAdt(chatActivity.getApplicationContext(),messages);
    }
}
