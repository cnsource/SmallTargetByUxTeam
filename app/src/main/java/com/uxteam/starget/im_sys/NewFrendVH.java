package com.uxteam.starget.im_sys;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uxteam.starget.R;

import org.w3c.dom.Text;

import cn.jiguang.imui.view.CircleImageView;

public class NewFrendVH extends RecyclerView.ViewHolder {
    public CircleImageView headImg;
    public TextView username;
    public TextView requestInfo;
    public Button refause;
    public Button accept;

    public NewFrendVH(@NonNull View itemView) {
        super(itemView);
        headImg= itemView.findViewById(R.id.head);
        username= itemView.findViewById(R.id.username);
        requestInfo= itemView.findViewById(R.id.requestInfo);
        refause= itemView.findViewById(R.id.refause);
        accept= itemView.findViewById(R.id.accept);

    }
}
