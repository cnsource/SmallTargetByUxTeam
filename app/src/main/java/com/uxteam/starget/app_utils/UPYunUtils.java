package com.uxteam.starget.app_utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.upyun.library.common.ParallelUploader;
import com.upyun.library.listener.UpCompleteListener;
import com.upyun.library.listener.UpProgressListener;
import com.upyun.library.utils.UpYunUtils;
import com.uxteam.starget.R;

import java.io.File;

public class UPYunUtils {

    private static final String TAG = "UPYunUtils";
    public static final String PATH_TARGETS = "targets";
    public static final String PATH_HEAD = "head";
    public static final String PATH_AUDIT = "audit";

    public static final String JPG = "jpg";
    public static final String TXT = "txt";


        /**
     * 获取又拍云资源路径
     * @param path 又拍云数据空间/head/---/targets/---/audit/
     * @param filename 存储文件名
     * @param extrlname 存储文件扩展名
     * @return 又拍云存储绝对路径
     */
    public static String getSourcePath(String path,String filename, String extrlname) {
        return "http://small-target.test.upcdn.net/"+path+"/" + filename + "." + extrlname;
    }
    public static String getSourcePath(String path) {
        return "http://small-target.test.upcdn.net"+path;
    }
    /**
     * 生成上传路径
     * @param path 又拍云数据空间/head/---/targets/---/audit/
     * @param filename 存储文件名
     * @param extrlname 存储文件扩展名
     * @return 又拍云存储绝对路径
     */
    public static String getUPLoadPath(String path, String filename, String extrlname) {
        return "/" + path + "/" + filename + "." + extrlname;
    }


    public static void upLoadFile(File file, final String upLoadPath, final UpLoadResultListener upLoadResultListener, final Context context) {
        final AlertDialog dialog;
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("正在上传文件");
        builder.setView(R.layout.progress);
        dialog=builder.create();
        dialog.show();
        ParallelUploader parallelUploader = new ParallelUploader("small-target", "qwe", UpYunUtils.md5("46ASahJopHUgjg9MWEWC0b9WTEt4kEoR"));
        parallelUploader.setCheckMD5(true);
        parallelUploader.setOnProgressListener(new UpProgressListener() {
            @SuppressLint("NewApi")
            @Override
            public void onRequestProgress(long bytesWrite, long contentLength) {
                Log.i(TAG, bytesWrite + ":" + contentLength);


            }
        });
        parallelUploader.upload(file, upLoadPath, null, new UpCompleteListener() {
            @Override
            public void onComplete(boolean isSuccess, String result) {
               upLoadResultListener.result(isSuccess,result,upLoadPath);
               dialog.dismiss();
            }
        });
    }
}
