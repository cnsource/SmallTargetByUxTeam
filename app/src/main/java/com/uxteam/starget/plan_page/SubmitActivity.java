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
import com.uxteam.starget.bmob_sys_pkg.Target;
import com.uxteam.starget.bmob_sys_pkg.User;
import com.uxteam.starget.login_registe.MyGlideEngine;
import com.uxteam.starget.main_face.MainfacePage;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.io.File;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class SubmitActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_CHOOSE = 89;
    private ImageView yzmimg;
    private EditText pluscontent;
    private Button submitbtn;
    private File file;
    private String path;
    private String filename;
    private String name;
    private String objectid;
    private Target target;
    private int requestCode = 121;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        initData();
        bindView();
        bindEvent();
    }

    private void initData() {
        name = getIntent().getStringExtra("name");
        objectid = getIntent().getStringExtra("objectid");
        BmobQuery<Target> query = new BmobQuery<>();
        query.addWhereEqualTo("objectId", objectid);
        query.findObjects(new FindListener<Target>() {
            @Override
            public void done(List<Target> list, BmobException e) {
                if (e == null) {
                    target = list.get(0);
                } else {
                    Log.e("SubmitActivity:查询出错", e.getMessage());
                }
            }
        });
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
                if (!target.isAudited()) {
                    filename = (BmobUser.getCurrentUser(User.class).getObjectId()) + new Date().getTime();
                    target.setSubmit(true);
                    target.setSubmitText(pluscontent.getText().toString());
                    UPYunUtils.upLoadFile(new File(path), UPYunUtils.getUPLoadPath(UPYunUtils.PATH_AUDIT, filename, UPYunUtils.JPG), new UpLoadResultListener() {
                        @Override
                        public void result(boolean isSuccess, String resultInfo, String resultPath) {
                            if (isSuccess) {
                                target.setSubmitImgPath(resultPath);
                                target.update(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        Intent in = new Intent(SubmitActivity.this, MainfacePage.class);
                                        if (e == null) {
                                            in.putExtra("isSubmit", true);
                                            Toast.makeText(SubmitActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(SubmitActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                                            Log.e("SubmitActivity提交失败", e.getMessage());
                                            in.putExtra("isSubmit", true);
                                        }
                                        startActivityForResult(in,requestCode);
                                        finish();
                                    }
                                });
                            } else {
                                Toast.makeText(SubmitActivity.this, "图片上传失败", Toast.LENGTH_SHORT).show();
                                Log.e("上传图片", "Error : " + resultInfo);
                            }
                        }
                    },SubmitActivity.this);

                } else {
                    Toast.makeText(SubmitActivity.this, "已经审核，无法提交", Toast.LENGTH_SHORT).show();
                }
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
        this.path = path;
        return path;
    }
} 
