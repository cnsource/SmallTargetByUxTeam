package com.uxteam.starget.config_pkg;

import android.app.Application;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.mob.MobSDK;

import org.greenrobot.eventbus.EventBus;

import cn.bmob.v3.Bmob;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.ContactNotifyEvent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;

public class MyApplicationClass extends Application implements MessageReciveEvent {

    @Override
    public void onCreate() {
        super.onCreate();
        MobSDK.init(this);
        JMessageClient.setDebugMode(true);
        JMessageClient.init(this, true);
        Bmob.initialize(this, "e1d39fe30fd8389b9e26bea8d9f3207f");
        JMessageClient.registerEventReceiver(getApplicationContext());
    }
    @Override//好友请求消息
    public void onEvent(ContactNotifyEvent contactNotifyEvent) {
        Log.i("Application接收到好友申请",contactNotifyEvent.getFromUsername());
        MyFrendsRequest frendsRequest=new MyFrendsRequest(contactNotifyEvent);
        EventBus.getDefault().postSticky(frendsRequest);
    }

    @Override
    public void onEventMainThread(ContactNotifyEvent contactNotifyEvent) {

    }

    @Override
    public void onEvent(OfflineMessageEvent offlineMessageEvent) {
        Log.i("离线消息","--------------");
    }

    @Override
    public void onEvent(MessageEvent messageEvent) {
        Log.i("在线消息","--------------");
    }

    @Override//主线程在线消息
    public void onEventMainThread(MessageEvent messageEvent) {

    }

    @Override//主线程离线消息
    public void onEventMainThread(OfflineMessageEvent offlineMessageEvent) {

    }


}
