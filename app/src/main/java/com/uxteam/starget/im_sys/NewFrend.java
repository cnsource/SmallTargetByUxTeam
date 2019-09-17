package com.uxteam.starget.im_sys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.uxteam.starget.R;
import com.uxteam.starget.config_pkg.MyFrendsRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.event.ContactNotifyEvent;
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
        newFrendAdt = new NewFrendAdt(this, userInfos, new EventResult() {
            @Override
            public void CallBack(int position) {
                userInfos.remove(position);
                requests.getAdapter().notifyDataSetChanged();
            }
        });
        requests.setAdapter(newFrendAdt);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void event(MyFrendsRequest request) {
        ContactNotifyEvent event=request.getContactNotifyEvent();
        String reason = event.getReason();
        String fromUsername = event.getFromUsername();
        String appkey = event.getfromUserAppKey();

        switch (event.getType()) {
            case invite_received://收到好友邀请
                //...
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
                break;
            case invite_accepted://对方接收了你的好友邀请
                //...
                break;
            case invite_declined://对方拒绝了你的好友邀请
                //...
                break;
            case contact_deleted://对方将你从好友中删除
                //...
                break;
            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    interface EventResult{
        void CallBack(int position);
    }
}
