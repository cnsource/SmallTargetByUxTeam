package com.uxteam.starget.im_sys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.uxteam.starget.R;
import com.uxteam.starget.bmob_sys_pkg.User;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyFrends extends AppCompatActivity {

    private RecyclerView frendsList;
    private TextView frendRequestNotofy;
    private ImageView addUser;
    private CircleImageView myHeadimg;
    private MyFrendsPresenter myFrendsPresenter;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.self_my_frends_list);
        bindView();
        myFrendsPresenter = new MyFrendsPresenter(this).load();
    }

    private void bindView() {
        refreshLayout = findViewById(R.id.swiperfrends);
        addUser = findViewById(R.id.addFrend);
        frendRequestNotofy = findViewById(R.id.frendsRequestNotify);
        frendsList = findViewById(R.id.frendsList);
    }

    public void bindViewEvent(View.OnClickListener clickListener, FrendRecAdt adt, SwipeRefreshLayout.OnRefreshListener refreshListener) {
        frendsList.setLayoutManager(new LinearLayoutManager(this));
        frendsList.setAdapter(adt);
        addUser.setOnClickListener(clickListener);
        frendRequestNotofy.setOnClickListener(clickListener);
        refreshLayout.setOnRefreshListener(refreshListener);
    }
    public void setRefresh(boolean bool){
        refreshLayout.setRefreshing(bool);
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        myFrendsPresenter.onDestoryToDo();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void setFrendRequestNotofy(String text) {
        this.frendRequestNotofy.setText(text);
    }

    public void refreshfrendsList() {
        frendsList.getAdapter().notifyDataSetChanged();
    }
    public void loadMyHeadImg(String path){
        Glide.with(this).load(path).into(myHeadimg);
    }
}
