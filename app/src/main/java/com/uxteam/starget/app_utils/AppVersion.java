package com.uxteam.starget.app_utils;

import cn.bmob.v3.BmobObject;

public class AppVersion extends BmobObject {
   private int versionCode ;
   private String versionUrl;
   private String useAgreement;
    public AppVersion(){

    }
    public int getVersionCode() {
        return versionCode;
    }

    public String getVersionUrl() {
        return versionUrl;
    }

    public String getUseAgreement() {
        return useAgreement;
    }
}
