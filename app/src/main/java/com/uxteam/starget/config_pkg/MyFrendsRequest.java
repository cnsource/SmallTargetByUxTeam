package com.uxteam.starget.config_pkg;

import cn.jpush.im.android.api.event.ContactNotifyEvent;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class MyFrendsRequest extends ContactNotifyEvent {
    @Id
    private long id;
    private boolean dealState;

    public boolean isDealState() {
        return dealState;
    }

    public void setDealState(boolean dealState) {
        this.dealState = dealState;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
