package com.uxteam.starget.self_page;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;


import com.uxteam.starget.R;
import com.uxteam.starget.login_registe.LoginPageActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.jpush.im.android.api.JMessageClient;

public class SelfPagePresenter {
    private SelfPage selfPage;

    public SelfPagePresenter(SelfPage selfpage) {
        this.selfPage = selfpage;
    }
    public void load(){
        selfPage.refreshView(
                getUserName(), 
                getRecItem(),
                loginOut()
        );
    }
    private View.OnClickListener loginOut(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BmobUser.logOut();
                JMessageClient.logout();
                selfPage.startActivity(new Intent(selfPage.getContext(), LoginPageActivity.class));
                Toast.makeText(selfPage.getContext(), "退出登录成功", Toast.LENGTH_SHORT).show();
            }
        };
    }
    private SelfPageRecAdt getRecItem() {
        List<SelfPageRecItem> items=new ArrayList<>();
        items.add(new SelfPageRecItem(R.mipmap.list,"监督列表"));
        items.add(new SelfPageRecItem(R.mipmap.frends,"朋友列表"));
        SelfPageRecAdt selfPageRecAdt = new SelfPageRecAdt(selfPage.getContext(),items);
        return selfPageRecAdt;
    }
    private String getUserName(){
        
        return "AAA";
    }

}
