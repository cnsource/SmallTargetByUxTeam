package com.uxteam.starget.im_sys;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * MsgUtils说明：
 */
public class MsgUtils {
    public static String getTextMsg(String JsonStr){
        String msg=null;
        try {
            JSONObject jsonObject=new JSONObject(JsonStr);
            JSONObject jsonData=jsonObject.getJSONObject("content");
            msg=jsonData.getString("text");
            Log.e("Json对象构造成功","---------");
        } catch (JSONException e) {
            Log.e("Json对象构造失败","---------");
        }
        return msg;
    }
}
