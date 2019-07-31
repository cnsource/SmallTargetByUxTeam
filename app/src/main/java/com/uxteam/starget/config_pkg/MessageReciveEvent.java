package com.uxteam.starget.config_pkg;

import cn.jpush.im.android.api.event.ContactNotifyEvent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;

public interface MessageReciveEvent {
    void onEvent(MessageEvent messageEvent);
    void onEventMainThread(MessageEvent messageEvent);
    void onEvent(OfflineMessageEvent offlineMessageEvent);
    void onEventMainThread(OfflineMessageEvent offlineMessageEvent);
    void onEvent(ContactNotifyEvent contactNotifyEvent);
}
