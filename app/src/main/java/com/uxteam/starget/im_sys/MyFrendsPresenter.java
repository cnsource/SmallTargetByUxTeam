package com.uxteam.starget.im_sys;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.uxteam.starget.R;
import com.uxteam.starget.config_pkg.MyFrendsRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.callback.GetUserInfoListCallback;
import cn.jpush.im.android.api.model.UserInfo;

public class MyFrendsPresenter {
    private MyFrends myFrends;
    private List<UserInfo> users = new ArrayList<>();

    public MyFrendsPresenter(MyFrends myFrends) {
        this.myFrends = myFrends;
    }

    public MyFrendsPresenter load() {
        myFrends.bindViewEvent(clickListenerProvider(), adtProvider(),refreshListenerProvider());
        getNetList();
        EventBus.getDefault().register(this);
        return this;
    }
    private SwipeRefreshLayout.OnRefreshListener refreshListenerProvider(){
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNetList();
            }
        };
    }
    private View.OnClickListener clickListenerProvider() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.addFrend:
                        myFrends.startActivity(new Intent(myFrends, QueryIMUser.class));
                        break;
                    case R.id.frendsRequestNotify:
                        myFrends.startActivity(new Intent(myFrends, NewFrend.class));
                        break;
                }
            }
        };
    }

    private FrendRecAdt adtProvider() {
        FrendRecAdt frendRecAdt=new FrendRecAdt(myFrends.getApplicationContext(), users);
        frendRecAdt.setOnclickListener(new OnclickListener() {
            @Override
            public void click(String username,String displayname) {
                Intent in=new Intent(myFrends,ChatActivity.class);
                in.putExtra("displayname",displayname);
                in.putExtra("username",username);
                myFrends.startActivity(in);
            }
        });
        return frendRecAdt;
    }
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void UserRequestEvent(MyFrendsRequest frendsRequest){
        Toast.makeText(myFrends, "Post到了朋友列表界面", Toast.LENGTH_SHORT).show();

        Log.i("好友申请事件",frendsRequest.getFromUsername());
        myFrends.setFrendRequestNotofy(frendsRequest.getFromUsername()+"申请成为您的好友");
    }
    private void getLocalList(){

    }
    private void getNetList(){
        ContactManager.getFriendList(new GetUserInfoListCallback() {
            @Override
            public void gotResult(int responseCode, String responseMessage, List<UserInfo> userInfoList) {
                if (0 == responseCode) {
                    //获取好友列表成功
                    users.clear();
                    users.addAll(userInfoList);
                    myFrends.refreshfrendsList();
                    myFrends.setRefresh(false);
                } else {
                    //获取好友列表失败
                    Log.e("获取用户列表失败",responseMessage);
                }
            }
        });
    }
    public void onDestoryToDo(){
        EventBus.getDefault().unregister(this);
    }
}
