package com.uxteam.starget.im_sys;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.uxteam.starget.R;
import com.uxteam.starget.app_utils.DateUtils;
import com.uxteam.starget.app_utils.UPYunUtils;
import com.uxteam.starget.app_utils.UpLoadResultListener;
import com.uxteam.starget.bmob_sys_pkg.User;
import com.uxteam.starget.login_registe.MyGlideEngine;
import com.uxteam.starget.login_registe.RegistePagePresenter;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.jiguang.imui.view.CircleImageView;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

public class SelfInfoEditerCard extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_CHOOSE = 1090;
    private CircleImageView head;
    private EditText nickediter;
    private Button savae;
    private File file;
    private String netName;
    private boolean CHOOSEICON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_info_editer_card);
        netName = BmobUser.getCurrentUser(User.class).getUsername()+DateUtils.getTimeStamp();
        bindView();
        bindEnent();
    }

    private void bindView() {
        head = findViewById(R.id.head);
        nickediter = findViewById(R.id.nickediter);
        savae = findViewById(R.id.save);

    }

    private  void bindEnent() {
        head.setOnClickListener(this);
        savae.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head:
                openIconSwitch();
                break;
            case R.id.save:
                final User user =BmobUser.getCurrentUser(User.class);
                user.setNickName(nickediter.getText().toString().trim());
                user.setAvatarNetName(netName);
                user.setAvatarUri("small-target.test.upcdn.net/head/"+ netName +".jpg");

                JMessageClient.getUserInfo(user.getUsername(), new GetUserInfoCallback() {
                    @Override
                    public void gotResult(int i, String s, UserInfo userInfo) {
                        userInfo.setNickname(nickediter.getText().toString().trim());
                        JMessageClient.updateMyInfo(UserInfo.Field.nickname, userInfo, new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {
                                if (i == 0) {
                                    user.update(new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e==null){
                                                Toast.makeText(SelfInfoEditerCard.this, "修改成功", Toast.LENGTH_SHORT).show();
                                            }else {
                                                Toast.makeText(SelfInfoEditerCard.this, "修改失败", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                } else {

                                }

                            }
                        });

                    }});
                JMessageClient.getUserInfo(user.getUsername(), null, new GetUserInfoCallback() {
                    @Override
                    public void gotResult(int i, String s, UserInfo userInfo) {
                        userInfo.updateNoteName(nickediter.getText().toString().trim(), new BasicCallback() {
                            @Override
                            public void gotResult(int responseCode, String responseMessage) {
                                if (0 == responseCode) {
                                    //更新备注名成功

                                } else {
                                    //更新备注名失败

                                }
                            }
                        });
                    }
                });
                upLoadHeadIcon();

                break;
            case R.id.goback:
                finish();
                break;
        }
    }
    private void openIconSwitch() {
        Matisse.from(this)
                .choose(MimeType.ofImage())
                .countable(true)
                .maxSelectable(1)
                .gridExpectedSize(this.getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new MyGlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }
    private void upLoadHeadIcon() {

        if (file==null){
            Toast.makeText(this, "您稍后可以继续上传头像", Toast.LENGTH_SHORT).show();
        }else {
            String path = UPYunUtils.getUPLoadPath(UPYunUtils.PATH_HEAD, netName, UPYunUtils.JPG);
            UPYunUtils.upLoadFile(file, path, new UpLoadResultListener() {
                @Override
                public void result(boolean isSuccess, String resultInfo, String resultPath) {
                    if (isSuccess) {
                        Toast.makeText(getApplicationContext(), "头像上传成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "头像上传失败" + resultInfo, Toast.LENGTH_SHORT).show();
                    }
                }
            },this);
        }
    }
    public void chooseHeadIconResult(Uri uri) {
        file = new File(this.getCacheDir() + "/uCrop.jpg");
        if (file.exists()) {
            file.delete();
        }
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(100);
        options.setCircleDimmedLayer(true);
        options.setRootViewBackgroundColor(Color.WHITE);
        options.setToolbarTitle("裁剪头像");
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        UCrop.of(uri, Uri.fromFile(file))
                .withAspectRatio(16, 16)
                .withMaxResultSize(400, 400)
                .withOptions(options)
                .start(this);
    }
    public void cutIconResult() {
        try {
            Bitmap bitmap = Bitmap.createBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
            head.setImageBitmap(bitmap);
            CHOOSEICON = true;
        } catch (Exception e) {
            Log.i("捕获异常2", e.getMessage());
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_CHOOSE:
                try {
                    chooseHeadIconResult(Matisse.obtainResult(data).get(0));
                }catch (Exception e){
                    Log.i("捕获异常",e.getMessage());
                }
                break;
            case UCrop.REQUEST_CROP:
                cutIconResult();
                break;
            case UCrop.RESULT_ERROR:
                Throwable t = UCrop.getError(data);
                break;
        }
    }
}