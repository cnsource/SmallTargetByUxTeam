package com.uxteam.starget.formulation_targets;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.uxteam.starget.bmob_sys_pkg.User;

import java.util.List;

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
        TextView v;
            if (view != null) {
                v = (TextView) view;
            } else {
                v=new TextView(context);
            }
            v.setText(users.get(i).getUsername());
        return v;
    }
}
