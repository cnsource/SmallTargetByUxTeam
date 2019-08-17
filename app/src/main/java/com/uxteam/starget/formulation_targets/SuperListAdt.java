package com.uxteam.starget.formulation_targets;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.uxteam.starget.R;
import com.uxteam.starget.bmob_sys_pkg.User;

import java.util.List;

import cn.jiguang.imui.view.CircleImageView;

public class SuperListAdt extends BaseAdapter {
    private Context context;
    private List<User> users;

    public SuperListAdt(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public User getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LinearLayout v;
        if (view != null) {
            v = (LinearLayout) view;
        } else {
            v = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.spinner_view, viewGroup, false);
        }
        Log.i("SpinnerItem", "" + i);
        CircleImageView imageView = v.findViewById(R.id.headimg);
        Glide.with(context).load(users.get(i).getAvatarNetPath()).error(R.drawable.aurora_headicon_default).into(imageView);
        TextView tv = v.findViewById(R.id.spinnerItem);
        String name = users.get(i).getNickName();
        if (TextUtils.isEmpty(name.trim())) {
            tv.setText(users.get(i).getUsername());
        }else {
            tv.setText(name);
        }
        TextView cnt = v.findViewById(R.id.target_cnt);
        cnt.setText("剩余：" + (10 - users.get(i).getTodaySupervision()) + "个");
        return v;
    }

}
