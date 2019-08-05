package com.uxteam.starget.bmob_sys_pkg;

import cn.bmob.v3.BmobObject;

public class Target extends BmobObject {
    private String startTime;
    private String endTime;
    private String targetContent;
    private User publisher;
    private User supervisor;
    private boolean targetState;
    private String remark;

    private int select;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTargetContent() {
        return targetContent;
    }

    public void setTargetContent(String targetContent) {
        this.targetContent = targetContent;
    }

    public User getPublisher() {
        return publisher;
    }

    public void setPublisher(User publisher) {
        this.publisher = publisher;
    }

    public User getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(User supervisor) {
        this.supervisor = supervisor;
    }

    public boolean isTargetState() {
        return targetState;
    }

    public void setTargetState(boolean targetState) {
        this.targetState = targetState;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getSelect() {
        return select;
    }

    public void setSelect(int select) {
        this.select = select;
    }

}
