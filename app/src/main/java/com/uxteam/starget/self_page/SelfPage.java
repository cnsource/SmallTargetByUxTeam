package com.uxteam.starget.self_page;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.uxteam.starget.R;
import com.uxteam.starget.bmob_sys_pkg.User;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;


public class SelfPage extends Fragment {

    private RecyclerView recyclerView;
    private Button loginOut;
    private TextView self_uname;
    private CircleImageView circleImageView;
    private TextView supCnt;
    private TextView pubCnt;
    private TextView btmCnt;
    private SelfPagePresenter pagePresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_self_page, container, false);
        initView(view);
        pagePresenter = new SelfPagePresenter(this);
        pagePresenter.load();
        return view;
    }

    private void initView(View view) {
        circleImageView = view.findViewById(R.id.self_user_Icon);
        recyclerView = view.findViewById(R.id.rec_list);
        loginOut = view.findViewById(R.id.loginOut);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        self_uname = view.findViewById(R.id.self_username);
        pubCnt = view.findViewById(R.id.pubCnt);
        supCnt = view.findViewById(R.id.supCnt);
        btmCnt =view.findViewById(R.id.btmCnt);

    }
    public void bindViewInfo(String userName, SelfPageRecAdt selfPageRecAdt, View.OnClickListener loginOutListener, View.OnClickListener clickListener){
       User user= BmobUser.getCurrentUser(User.class);
        Glide.with(this).load("http://"+user.getAvatarUri()).error(R.drawable.aurora_headicon_default).into(circleImageView);
        self_uname.setText(userName);
        circleImageView.setOnClickListener(clickListener);
        recyclerView.setAdapter(selfPageRecAdt);
        loginOut.setOnClickListener(loginOutListener);
    }
    public void refreshView(){
        try {
            recyclerView.getAdapter().notifyDataSetChanged();
        }catch (Exception e){

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        pagePresenter.unregiste();
        pagePresenter.load();
        refreshView();
    }



    public  void showInfo(String pub, String sup, String btm){
        pubCnt.setText(pub);
        supCnt.setText(sup);
        btmCnt.setText(btm);
    }
    public void closePage(){
        getActivity().finish();
    }
}
