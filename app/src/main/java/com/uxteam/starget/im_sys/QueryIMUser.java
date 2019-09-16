package com.uxteam.starget.im_sys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.uxteam.starget.R;

import cn.jiguang.imui.view.CircleImageView;

public class QueryIMUser extends AppCompatActivity {

    private AppCompatButton findUser;
    private AppCompatButton addBtn;
    private RelativeLayout userInfolayout;
    private EditText inputUserName;
    private ImageView back;
    private CardView cardView;
    private TextView name;
    private CircleImageView head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_imuser);
        bindView();
        new QueryIMUserPresenter(this).load();
    }

    private void bindView() {
        inputUserName = findViewById(R.id.query_name);
        findUser = findViewById(R.id.querybtn);
        userInfolayout = findViewById(R.id.userInfolayout);
        cardView = findViewById(R.id.userInfoCard);
        addBtn = findViewById(R.id.addBtn);
        head = findViewById(R.id.head);
        name = findViewById(R.id.name);
    }
    public void bindViewEvent(View.OnClickListener clickListener){
        findUser.setOnClickListener(clickListener);
        addBtn.setOnClickListener(clickListener);
    }
    public String getEditTextInfo(){
        return inputUserName.getText().toString();
    }
    public void setUserInfolayout(int visiable){
        cardView.setVisibility(visiable);
        addBtn.setVisibility(visiable);
    }
    public void reFreshView(String url,String name){
        if (url!=null)
        Glide.with(this).load(url).into(head);
        this.name.setText(name);
    }
    public void Qfinish(){
        this.finish();
    }
}
