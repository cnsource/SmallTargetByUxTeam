package com.uxteam.starget.self_page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.uxteam.starget.R;

import java.util.List;

public class SelfPageRecAdt extends RecyclerView.Adapter<SelfPageRecVH> {
    private OnItemClickListener itemClickListener;

    private Context context;

    private List<SelfPageRecItem> itemInfos;

    public SelfPageRecAdt(Context context, List<SelfPageRecItem> infos) {
        this.context = context;
        this.itemInfos=infos;
    }


    @Override
    public SelfPageRecVH onCreateViewHolder( ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.fragment_self_page_rec_item,viewGroup,false);
        SelfPageRecVH viewHolder=new SelfPageRecVH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelfPageRecVH uv,final int i) {
        uv.iv.setImageResource(itemInfos.get(i).getId());
        setTipCnt(itemInfos.get(i).getUn_read_msg_cnt(),uv.unreadMsgCnt);
        uv.infotext.setText(itemInfos.get(i).getInfotext());
        uv.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemInfos.size();
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    private void setTipCnt(int cnt, TextView view){
        view.setVisibility(View.VISIBLE);
        if (cnt>99)
            view.setText("99+");
        else if (cnt>0)
            view.setText(cnt+"");
        else
            view.setVisibility(View.INVISIBLE);
    }
}
