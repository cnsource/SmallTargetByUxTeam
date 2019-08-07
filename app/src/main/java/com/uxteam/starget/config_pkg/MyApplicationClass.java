package com.uxteam.starget.config_pkg;

import android.app.Application;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.uxteam.starget.MyObjectBox;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;
import io.objectbox.android.BuildConfig;

public class MyApplicationClass extends Application implements MessageReciveEvent {

    private static BoxStore boxStore;
    @Override
    public void onCreate() {
        super.onCreate();
        JMessageClient.registerEventReceiver(this);
        boxStore = MyObjectBox.builder().androidContext(getApplicationContext()).build();
        if (BuildConfig.DEBUG) {
            boolean started = new AndroidObjectBrowser(boxStore).start(this);
            Log.i("ObjectBrowser", "Started: " + started + "   " + boxStore.getObjectBrowserPort());
        }
        Log.i("MyApplicationClass","初始化成功");
    }

    public static BoxStore getBoxStore() {
        return boxStore;
    }


    @Override//好友请求消息
    public void onEvent(final MyFrendsRequest frendsRequest) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                Box<MyFrendsRequest> frendsRequestBox=getBoxStore().boxFor(MyFrendsRequest.class);
                frendsRequest.setDealState(false);
                frendsRequestBox.put(frendsRequest);
                new Handler(){
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);
                        //  Todo 通知有好友申请
                    }
                }.sendMessage(new Message());
            }
        };
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
