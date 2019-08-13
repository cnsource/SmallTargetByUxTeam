package com.uxteam.starget.im_sys;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uxteam.starget.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class FrendVH extends RecyclerView.ViewHolder {
    public CircleImageView circleImageView;
    public TextView nickName;
    public TextView signture;
    public FrendVH(@NonNull View itemView) {

        super(itemView);
        circleImageView= itemView.findViewById(R.id.user_head_img);
        nickName= itemView.findViewById(R.id.user_nick_name);
        signture= itemView.findViewById(R.id.user_signture);
    }
}
