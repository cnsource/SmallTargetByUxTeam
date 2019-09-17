package com.uxteam.starget.im_sys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.uxteam.starget.R;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

public class FrendUserInfoCard extends AppCompatActivity implements View.OnClickListener {

    private EditText markname;
    private EditText markname1;
    private Button save;
    private Button goback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frend_user_info_card);
        bindView();
        bindControllEvent();
    }

    private void bindControllEvent() {
        save.setOnClickListener(this);
        goback.setOnClickListener(this);
    }

    private void bindView() {
        markname1 = findViewById(R.id.markediter);
        save = findViewById(R.id.save);
        goback = findViewById(R.id.goback);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.save:
                String username=getIntent().getStringExtra("username");
                JMessageClient.getUserInfo(username, null, new GetUserInfoCallback() {
                    @Override
                    public void gotResult(int i, String s, UserInfo userInfo) {
                        userInfo.updateNoteName(markname1.getText().toString().trim(), new BasicCallback() {
                            @Override
                            public void gotResult(int responseCode, String responseMessage) {
                                if (0 == responseCode) {
                                    //更新备注名成功
                                    Toast.makeText(FrendUserInfoCard.this, "备注修改成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    //更新备注名失败
                                    Toast.makeText(FrendUserInfoCard.this, "修改失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });

                break;
            case R.id.goback:
                finish();
                break;
        }
    }
}
