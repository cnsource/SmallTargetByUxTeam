package com.uxteam.starget;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.uxteam.starget.login_registe.LoginPageActivity;
import com.uxteam.starget.main_face.MainfacePage;
import com.uxteam.starget.bmob_sys_pkg.User;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

public class MainActivity extends AppCompatActivity {

    private String pwd;
    private String account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initOpenApi();
        if (BmobUser.isLogin()) {
            User user = BmobUser.getCurrentUser(User.class);
            JMessageClient.login(user.getUsername(), user.getCachePwd(), new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    if (i == 0) {
                        Log.e(getClass().getName(), "账户登录");
                        startActivity(new Intent(MainActivity.this, MainfacePage.class));
                    } else {
                        Log.e(getClass().getName(), "账户登录过期，请重新登录" + s);
                        startActivity(new Intent(MainActivity.this, LoginPageActivity.class));
                        finish();
                    }
                }
            });
        } else {
            startActivity(new Intent(MainActivity.this, LoginPageActivity.class));
            finish();
        }
    }

    private void initOpenApi() {
        /*JMessageClient.setDebugMode(true);
        JMessageClient.init(this, true);*/
        /*Bmob.initialize(this, "e1d39fe30fd8389b9e26bea8d9f3207f");*/
    }
}
