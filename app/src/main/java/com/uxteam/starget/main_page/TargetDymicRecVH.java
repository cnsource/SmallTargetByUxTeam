package com.uxteam.starget.main_page;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uxteam.starget.R;

import cn.jiguang.imui.view.CircleImageView;

public class TargetDymicRecVH extends RecyclerView.ViewHolder {
    public CircleImageView head;
    public TextView name;
    public TextView ctext;
    public ImageView img1,img2;
    public TextView upDateTime;
    public TargetDymicRecVH(@NonNull View itemView) {
        super(itemView);
        head= itemView.findViewById(R.id.head);
        name= itemView.findViewById(R.id.name);
        ctext= itemView.findViewById(R.id.ctext);
        img1= itemView.findViewById(R.id.img1);
        img2= itemView.findViewById(R.id.img2);
        upDateTime= itemView.findViewById(R.id.updatetime);
    }
}
