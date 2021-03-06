package com.uxteam.starget.app_utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Map;

public class MyBmobUtils {
    public static final String Identity_Publisher="Publisher";
    public static final String Identity_Supervisor="Supervisor";
    public static void AccessBmobCloudFuncation(final Context context, String url, final Map<String,String> params, final CloudFuncationListener cloudFuncationListener){
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Volley请求成功",response);
                cloudFuncationListener.result(true,response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley请求失败",error.getMessage());
                cloudFuncationListener.result(false, null);
            }
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

}
