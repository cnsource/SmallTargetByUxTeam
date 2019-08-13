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

import com.uxteam.starget.R;

public class QueryIMUser extends AppCompatActivity {

    private AppCompatButton findUser;
    private AppCompatButton addBtn;
    private RelativeLayout userInfolayout;
    private EditText inputUserName;
    private ImageView back;
    private CardView cardView;

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
    public void Qfinish(){
        this.finish();
    }
}
