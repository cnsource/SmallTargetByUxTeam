package com.uxteam.starget.self_page;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uxteam.starget.R;

public class SelfPageRecVH extends RecyclerView.ViewHolder {
    public ImageView iv;
    public TextView infotext;
    public TextView unreadMsgCnt;
    public SelfPageRecVH(@NonNull View itemView) {
        super(itemView);
        iv= itemView.findViewById(R.id.user_item_icon);
        infotext= itemView.findViewById(R.id.user_item_text);
        unreadMsgCnt=itemView.findViewById(R.id.unreadmsg);
    }
}
