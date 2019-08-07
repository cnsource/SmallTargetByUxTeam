package com.uxteam.starget.im_sys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.uxteam.starget.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyFrends extends AppCompatActivity {

    private RecyclerView frendsList;
    private TextView frendRequestNotofy;
    private ImageView addUser;
    private CircleImageView myHeadimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.self_my_frends_list);
        bindView();
        new MyFrendsPresenter(this).load();
    }

    private void bindView() {
        myHeadimg = findViewById(R.id.frendList_MyIcon);
        addUser = findViewById(R.id.addFrend);
        frendRequestNotofy = findViewById(R.id.frendsRequestNotify);
        frendsList = findViewById(R.id.frendsList);
    }

    public void bindViewEvent(View.OnClickListener clickListener, FrendRecAdt adt) {
        frendsList.setLayoutManager(new LinearLayoutManager(this));
        frendsList.setAdapter(adt);
        addUser.setOnClickListener(clickListener);
        frendRequestNotofy.setOnClickListener(clickListener);
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
