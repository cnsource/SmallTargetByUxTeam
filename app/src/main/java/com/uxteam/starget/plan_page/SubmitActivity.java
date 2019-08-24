package com.uxteam.starget.plan_page;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.uxteam.starget.R;
import com.uxteam.starget.app_utils.UPYunUtils;
import com.uxteam.starget.app_utils.UpLoadResultListener;
import com.uxteam.starget.bmob_sys_pkg.User;
import com.uxteam.starget.login_registe.MyGlideEngine;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.io.File;
import java.util.Date;

import cn.bmob.v3.BmobUser;

public class SubmitActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_CHOOSE = 89;
    private ImageView yzmimg;
    private EditText pluscontent;
    private Button submitbtn;
    private File file;
    private String path;
    private String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        bindView();
        bindEvent();
    }

    private void bindEvent() {
        yzmimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePic();
            }
        });
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filename=(BmobUser.getCurrentUser(User.class).getObjectId())+new Date().getTime();
                UPYunUtils.upLoadFile(new File(path), UPYunUtils.getSourcePath(UPYunUtils.PATH_AUDIT, filename, UPYunUtils.JPG), new UpLoadResultListener() {
                    @Override
                    public void result(boolean isSuccess, String resultInfo) {
                        if (isSuccess){
                            Toast.makeText(SubmitActivity.this, "图片上传成功", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(SubmitActivity.this, "图片上传失败", Toast.LENGTH_SHORT).show();
                            Log.e("上传图片","Error : "+resultInfo);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_CHOOSE:
                if (data != null) {
                    Uri uri = Matisse.obtainResult(data).get(0);
                    try {
                        Bitmap bitmap = Bitmap.createBitmap(BitmapFactory.decodeFile(getPAth(uri)));
                        yzmimg.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        Log.i("捕获异常", e.getMessage());
                    }
                }
                break;
        }
    }

    private void bindView() {
        yzmimg = findViewById(R.id.yzmimg);
        pluscontent = findViewById(R.id.pluscontent);
        submitbtn = findViewById(R.id.submitbtn);
    }

    private void choosePic() {
        Matisse.from(SubmitActivity.this)
                .choose(MimeType.ofImage())
                .countable(true)
                .maxSelectable(1)
                .gridExpectedSize(SubmitActivity.this.getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new MyGlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    public String getPAth(Uri uri) {
        String path = null;
        if (!TextUtils.isEmpty(uri.getAuthority())) {
            Cursor cursor = getContentResolver().query(uri,
                    new String[]{MediaStore.Images.Media.DATA}, null, null, null);
            if (null == cursor) {
                Toast.makeText(this, "图片没找到", Toast.LENGTH_SHORT).show();
                return null;
            }
            cursor.moveToFirst();
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();
        } else {
            path = uri.getPath();
        }
        this.path=path;
        return path;
    }
} 
