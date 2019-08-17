package com.uxteam.starget.self_page;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.uxteam.starget.R;
import com.uxteam.starget.app_utils.UPYunUtils;
import com.uxteam.starget.bmob_sys_pkg.User;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;


public class SelfPage extends Fragment {

    private RecyclerView recyclerView;
    private Button loginOut;
    private TextView self_uname;
    private CircleImageView circleImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_self_page, container, false);
        initView(view);
        new SelfPagePresenter(this).load();
        return view;
    }

    private void initView(View view) {
        circleImageView = view.findViewById(R.id.self_user_Icon);
        recyclerView = view.findViewById(R.id.rec_list);
        loginOut = view.findViewById(R.id.loginOut);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        self_uname = view.findViewById(R.id.self_username);
    }
    public void refreshView(String userName, SelfPageRecAdt selfPageRecAdt, View.OnClickListener loginOutListener){
        Glide.with(this).load(UPYunUtils.getSourcePath("head", BmobUser.getCurrentUser(User.class).getUsername(),UPYunUtils.JPG)).into(circleImageView);
        self_uname.setText(userName);
        recyclerView.setAdapter(selfPageRecAdt);
        loginOut.setOnClickListener(loginOutListener);
    }
    public void closePage(){
        getActivity().finish();
    }
}
