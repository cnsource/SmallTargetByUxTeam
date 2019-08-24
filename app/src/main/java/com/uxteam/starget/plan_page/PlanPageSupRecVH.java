package com.uxteam.starget.plan_page;

import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uxteam.starget.R;

import cn.jiguang.imui.view.CircleImageView;

public class PlanPageSupRecVH extends RecyclerView.ViewHolder {
    public  final String PASS="已\n审\n核";
    public  final String DENINE="待\n审\n核";
    public CircleImageView head;
    public TextView name;
    public TextView content;
    public TextView showstate;
    public Button checkbtn;
    public Button deninebtn;
    public Button passbtn;

    public PlanPageSupRecVH(@NonNull View itemView) {
        super(itemView);
        bindView(itemView);
    }

    private void bindView(View itemView) {
        head= itemView.findViewById(R.id.head);
        name= itemView.findViewById(R.id.name);
        content= itemView.findViewById(R.id.targetcontent);
        showstate= itemView.findViewById(R.id.statedisplay);
        checkbtn= itemView.findViewById(R.id.checkbtn);
        deninebtn= itemView.findViewById(R.id.denine);
        passbtn= itemView.findViewById(R.id.pass);
    }
}
