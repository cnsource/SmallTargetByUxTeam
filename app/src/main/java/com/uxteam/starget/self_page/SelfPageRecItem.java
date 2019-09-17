package com.uxteam.starget.self_page;

public class SelfPageRecItem {
    private int id;
    private String infotext;

    public int getUn_read_msg_cnt() {
        return un_read_msg_cnt;
    }

    private int un_read_msg_cnt;
    public int getId() {
        return id;
    }

    public String getInfotext() {
        return infotext;
    }

    public SelfPageRecItem(int id, String infotext,int un_read_msg_cnt) {
        this.id = id;
        this.infotext = infotext;
        this.un_read_msg_cnt = un_read_msg_cnt;
    }
}
