package com.uxteam.starget.im_sys;

import android.content.Context;
import android.os.Message;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

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
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
