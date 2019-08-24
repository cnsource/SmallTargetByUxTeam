package com.uxteam.starget.app_utils;

import android.util.Log;
import android.widget.Toast;

import com.upyun.library.common.ParallelUploader;
import com.upyun.library.listener.UpCompleteListener;
import com.upyun.library.listener.UpProgressListener;
import com.upyun.library.utils.UpYunUtils;

import java.io.File;

public class UPYunUtils {

    private static final String TAG = "UPYunUtils";
    public static final String PATH_TARGETS = "targets";
    public static final String PATH_HEAD = "head";
    public static final String PATH_AUDIT = "audit";

    public static final String JPG = "jpg";
    public static final String TXT = "txt";

    public static String getSourcePath(String path,String filename, String extrlname) {
        return "http://small-target.test.upcdn.net/"+path+"/" + filename + "." + extrlname;
    }

    public static String getUPLoadPath(String path, String filename, String extrlname) {
        return "/" + path + "/" + filename + "." + extrlname;
    }

    public static void upLoadFile(File file, String upLoadPath, final UpLoadResultListener upLoadResultListener) {
        ParallelUploader parallelUploader = new ParallelUploader("small-target", "qwe", UpYunUtils.md5("46ASahJopHUgjg9MWEWC0b9WTEt4kEoR"));
        parallelUploader.setCheckMD5(true);
        parallelUploader.setOnProgressListener(new UpProgressListener() {
            @Override
            public void onRequestProgress(long bytesWrite, long contentLength) {
                Log.e(TAG, bytesWrite + ":" + contentLength);
            }
        });
        parallelUploader.upload(file, upLoadPath, null, new UpCompleteListener() {
            @Override
            public void onComplete(boolean isSuccess, String result) {
               upLoadResultListener.result(isSuccess,result);
            }
        });
    }
}
