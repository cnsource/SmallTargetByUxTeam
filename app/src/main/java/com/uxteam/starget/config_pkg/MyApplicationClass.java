package com.uxteam.starget.config_pkg;

import android.app.Application;
import android.util.Log;


import cn.bmob.v3.Bmob;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.ContactNotifyEvent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;

public class MyApplicationClass extends Application implements MessageReciveEvent {

    @Override
    public void onCreate() {
        super.onCreate();

        JMessageClient.registerEventReceiver(this);
        Log.i("MyApplicationClass","初始化成功");
    }


    @Override//好友请求消息
    public void onEvent(ContactNotifyEvent contactNotifyEvent) {

    }

    @Override
    public void onEvent(OfflineMessageEvent offlineMessageEvent) {

    }

    @Override
    public void onEvent(MessageEvent messageEvent) {

    }

    @Override//主线程在线消息
    public void onEventMainThread(MessageEvent messageEvent) {

    }

    @Override//主线程离线消息
    public void onEventMainThread(OfflineMessageEvent offlineMessageEvent) {

    }


}
