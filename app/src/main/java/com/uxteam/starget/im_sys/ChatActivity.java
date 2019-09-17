package com.uxteam.starget.im_sys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.uxteam.starget.R;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;

public class ChatActivity extends AppCompatActivity {

    private Button chatSendBtn;
    private EditText chatInput;
    private RecyclerView chatMsgList;
    private SwipeRefreshLayout loadHistoryMsg;
    private ImageView contactPersonInfo;
    private ImageView backBtn;
    private TextView markName;
    private String username;
    private Intent intent;
    private String displayname;
    private ChatActivityPresender chatActivityPresender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        username = intent.getStringExtra("username");
        displayname = intent.getStringExtra("displayname");
        JMessageClient.enterSingleConversation(username, null);
        setContentView(R.layout.activity_chat);
        bindView();
        chatActivityPresender = new ChatActivityPresender(this).load(username);
    }

    private void bindView() {
        markName = findViewById(R.id.markName);
        markName.setText(displayname);
        backBtn = findViewById(R.id.back);
        contactPersonInfo = findViewById(R.id.contact_person_info);
        loadHistoryMsg = findViewById(R.id.loadHistory);
        chatMsgList = findViewById(R.id.chat_message);
        chatInput = findViewById(R.id.activity_chat_input);
        chatSendBtn = findViewById(R.id.activity_chat_send_btn);
    }

    public String getChatInput() {
        return chatInput.getText().toString();
    }

    public void bindViewEvent(View.OnClickListener clickListener, SwipeRefreshLayout.OnRefreshListener refreshListener, ChatMsgListAdt adt, LinearLayoutManager layoutManager) {
        chatMsgList.setLayoutManager(layoutManager);
        chatMsgList.setAdapter(adt);
        backBtn.setOnClickListener(clickListener);
        contactPersonInfo.setOnClickListener(clickListener);
        chatSendBtn.setOnClickListener(clickListener);
        loadHistoryMsg.setOnRefreshListener(refreshListener);
    }

    public void refreshMsgList(int i) {
        chatMsgList.getAdapter().notifyDataSetChanged();
        chatMsgList.scrollToPosition(i);
    }

    public void close() {
        finish();
    }

    public void closeRefresh() {
        loadHistoryMsg.setRefreshing(false);
    }

    public void setMarkName(String name) {
        markName.setText(name);
    }

    public void initChatInput() {
        chatInput.setText("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        JMessageClient.enterSingleConversation(username, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JMessageClient.exitConversation();
        Conversation.createSingleConversation(username, null).resetUnreadCount();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Conversation.createSingleConversation(username, null).resetUnreadCount();
        JMessageClient.exitConversation();
        chatActivityPresender.unRegisteEventBus();
    }
}
