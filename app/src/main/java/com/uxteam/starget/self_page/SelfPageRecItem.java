package com.uxteam.starget.self_page;

public class SelfPageRecItem {
    private int id;
    private String infotext;

    public int getId() {
        return id;
    }

    public String getInfotext() {
        return infotext;
    }

    public SelfPageRecItem(int id, String infotext) {
        this.id = id;
        this.infotext = infotext;
    }
}
