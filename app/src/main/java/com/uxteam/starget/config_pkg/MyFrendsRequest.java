package com.uxteam.starget.config_pkg;

import cn.jpush.im.android.api.event.ContactNotifyEvent;

public class MyFrendsRequest extends ContactNotifyEvent {
    private ContactNotifyEvent contactNotifyEvent;

    public MyFrendsRequest(ContactNotifyEvent contactNotifyEvent) {
        this.contactNotifyEvent = contactNotifyEvent;
    }

    @Override
    public String getFromUsername() {
        return contactNotifyEvent.getFromUsername();
    }

    private boolean dealState;

    public boolean isDealState() {
        return dealState;
    }

    public void setDealState(boolean dealState) {
        this.dealState = dealState;
    }

}