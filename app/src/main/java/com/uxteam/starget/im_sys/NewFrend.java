package com.uxteam.starget.im_sys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.uxteam.starget.R;
import com.uxteam.starget.bmob_sys_pkg.User;
import com.uxteam.starget.config_pkg.MyFrendsRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.callback.GetUserInfoListCallback;
import cn.jpush.im.android.api.model.UserInfo;

public class NewFrend extends AppCompatActivity {
    private RecyclerView requests;
    private List<UserInfo> userInfos = new ArrayList<>();
    private NewFrendAdt newFrendAdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_frend);
        requests = findViewById(R.id.frendRequest);
        requests.setLayoutManager(new LinearLayoutManager(this));
        newFrendAdt = new NewFrendAdt(this, userInfos);
        requests.setAdapter(newFrendAdt);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void event(MyFrendsRequest request) {
        Toast.makeText(this, "Post到了添加界面", Toast.LENGTH_SHORT).show();
        JMessageClient.getUserInfo(request.getFromUsername(), new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                if (i == 0) {
                    userInfos.add(userInfo);
                    newFrendAdt.notifyDataSetChanged();
                } else {
                    Log.e("获取用户数据失败", s);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
