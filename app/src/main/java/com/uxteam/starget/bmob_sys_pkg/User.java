package com.uxteam.starget.bmob_sys_pkg;

import cn.bmob.v3.BmobUser;

public class User extends BmobUser {
    private String cachePwd;

    public String getCachePwd() {
        return cachePwd;
    }

    public void setCachePwd(String cachePwd) {
        this.cachePwd = cachePwd;
    }
}
