package com.uxteam.starget ;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.uxteam.starget.R;
import com.uxteam.starget.login_registe.LoginPageActivity;
import com.uxteam.starget.main_face.MainfacePage;
import com.uxteam.starget.utils_bmob.MyBmobUtils;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_registe_login_page);
        initOpenApi();
        /*Log.i("测试", MyBmobUtils.getUser("SSAA").getObjectId());*/
        startActivity(new Intent(MainActivity.this, LoginPageActivity.class));
    }

    private void initOpenApi() {
        JMessageClient.setDebugMode(true);
        JMessageClient.init(this,true);
        Bmob.initialize(this, "e1d39fe30fd8389b9e26bea8d9f3207f");
    }
}
