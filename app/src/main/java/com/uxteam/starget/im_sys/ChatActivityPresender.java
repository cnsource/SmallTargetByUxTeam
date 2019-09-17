package com.uxteam.starget.im_sys;


import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.uxteam.starget.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;

public class ChatActivityPresender {
    private ChatActivity chatActivity;
    private List<Message> messages = new ArrayList<>();
    private String username=null;
    private Conversation conversation;

    public ChatActivityPresender(ChatActivity chatActivity) {
        EventBus.getDefault().register(this);
        this.chatActivity = chatActivity;
    }

    public ChatActivityPresender load(String username) {
        messages.clear();
        chatActivity.initChatInput();
        this.username = username;
        conversation = Conversation.createSingleConversation(username, null);
        chatActivity.bindViewEvent(clickListenerProvider(), refreshListenerProvider(), adtProvider(), layoutManagerProvider());
        initMessage();
        return this;
    }

    private void initMessage() {
        List<Message> msg= conversation.getMessagesFromNewest(0,15);
        messages.addAll(msg);
        chatActivity.refreshMsgList( 0);
    }

    private SwipeRefreshLayout.OnRefreshListener refreshListenerProvider() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                int i=messages.size();
                List<Message> historyMsg = conversation.getMessagesFromNewest(messages.size(), 10);
                if (historyMsg != null && historyMsg.size() > 0) {
                    messages.addAll(historyMsg);
                    chatActivity.refreshMsgList(i);
                } else {
                    Toast.makeText(chatActivity, "没有更早的记录了", Toast.LENGTH_SHORT).show();
                }

                chatActivity.closeRefresh();
            }
        };
    }

    private LinearLayoutManager layoutManagerProvider() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(chatActivity);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        return layoutManager;
    }

    private View.OnClickListener clickListenerProvider() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.back:
                        chatActivity.close();
                        break;
                    case R.id.contact_person_info:
                        Intent in=new Intent(chatActivity.getApplicationContext(),FrendUserInfoCard.class);
                        in.putExtra("username",username);
                        chatActivity.startActivity(in);
                        break;
                    case R.id.activity_chat_send_btn:
                        Message message = JMessageClient.createSingleTextMessage(username, null, chatActivity.getChatInput());
                        JMessageClient.sendMessage(message);
                        messages.add(0, message);
                        chatActivity.refreshMsgList( 0);
                        chatActivity.initChatInput();
                        break;
                }
            }
        };
    }

    private ChatMsgListAdt adtProvider() {
        return new ChatMsgListAdt(chatActivity.getApplicationContext(), messages,username);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void messageEvent(MessageEvent messageEvent) {
        Message message = messageEvent.getMessage();
        messages.add(0, message);
        Log.i("AMW在线消息","************");
        chatActivity.refreshMsgList( 0);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void messageEvent(OfflineMessageEvent offlineMessageEvent) {
        if (conversation == offlineMessageEvent.getConversation()) {
            for (Message message : offlineMessageEvent.getOfflineMessageList()) {
                Log.i("AMW离线消息","************");
                messages.add(0, message);
            }
            chatActivity.refreshMsgList(0);
        }

    }
    public void unRegisteEventBus(){
        EventBus.getDefault().unregister(this);
    }
}
