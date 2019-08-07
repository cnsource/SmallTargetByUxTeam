package com.uxteam.starget.im_sys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uxteam.starget.R;

public class ChatActivity extends AppCompatActivity {

    private Button chatSendBtn;
    private EditText chatInput;
    private RecyclerView chatMsgList;
    private SwipeRefreshLayout loadHistoryMsg;
    private ImageView contactPersonInfo;
    private ImageView backBtn;
    private TextView markName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        bindView();
        new ChatActivityPresender(this).load();
    }

    private void bindView() {
        markName = findViewById(R.id.markName);
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
    public void bindViewEvent(View.OnClickListener clickListener, SwipeRefreshLayout.OnRefreshListener refreshListener, ChatMsgListAdt adt,LinearLayoutManager layoutManager){
        chatMsgList.setLayoutManager(layoutManager);
        chatMsgList.setAdapter(adt);
        backBtn.setOnClickListener(clickListener);
        contactPersonInfo.setOnClickListener(clickListener);
        chatSendBtn.setOnClickListener(clickListener);
        loadHistoryMsg.setOnRefreshListener(refreshListener);
    }
    public void refreshMsgList(boolean isLoadHistory) {
        chatMsgList.getAdapter().notifyDataSetChanged();
        if (isLoadHistory)
            chatMsgList.scrollToPosition(chatMsgList.getAdapter().getItemCount());
        else
            chatMsgList.scrollToPosition(0);
    }
    public void closeRefresh(){
        loadHistoryMsg.setRefreshing(false);
    }
    public void setMarkName(String name){
        markName.setText(name);
    }
}
