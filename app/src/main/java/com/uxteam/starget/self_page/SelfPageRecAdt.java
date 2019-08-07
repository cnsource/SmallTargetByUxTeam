package com.uxteam.starget.self_page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

}
