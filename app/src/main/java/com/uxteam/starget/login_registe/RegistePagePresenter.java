package com.uxteam.starget.login_registe;

import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.upyun.library.common.ParallelUploader;
import com.upyun.library.listener.UpCompleteListener;
import com.upyun.library.listener.UpProgressListener;
import com.upyun.library.utils.UpYunUtils;
import com.uxteam.starget.R;
import com.uxteam.starget.app_utils.DateUtils;

import com.uxteam.starget.app_utils.UPYunUtils;
import com.uxteam.starget.app_utils.UpLoadResultListener;
import com.uxteam.starget.bmob_sys_pkg.User;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.options.RegisterOptionalUserInfo;
import cn.jpush.im.api.BasicCallback;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegistePagePresenter implements View.OnClickListener {
    public static final int REQUEST_CODE_CHOOSE = 1;
    private RegistePageActivity registePageActivity;
    private EventHandler eventHandler;
    private File file;
    private String path;
    private String TAG = "上传进度";
    private boolean CHOOSEICON = false;
    private int BmobInfoRegisteResult = 0;
    private int JMInfoRegisteResult = 0;
    private List<String> errorInfo = new ArrayList<>();
    private String netName;

    public RegistePagePresenter(RegistePageActivity registePageActivity) {
        this.registePageActivity = registePageActivity;
    }

    public RegistePagePresenter load() {
        registePageActivity.bindControlEvent(this);
        initEventHandler();
        return this;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id._registe_headIcon:
                openIconSwitch();
                break;
            case R.id._registe_getYzm:
                if (!TextUtils.isEmpty(registePageActivity.getTel()))
                    SMSSDK.getVerificationCode("86", registePageActivity.getTel());
                else Toast.makeText(registePageActivity, "请输入手机号码", Toast.LENGTH_SHORT).show();
                break;
            case R.id._registe_btn:
                RegisteVerification();
                break;
            default:
                registePageActivity.finish();
                break;
        }
    }

    private void openIconSwitch() {
        Matisse.from(registePageActivity)
                .choose(MimeType.ofImage())
                .countable(true)
                .maxSelectable(1)
                .gridExpectedSize(registePageActivity.getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new MyGlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    public void initEventHandler() {
        eventHandler = new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                // afterEvent会在子线程被调用，因此如果后续有UI相关操作，需要将数据发送到UI线程
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                new Handler(Looper.getMainLooper(), new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        int event = msg.arg1;
                        int result = msg.arg2;
                        Object data = msg.obj;
                        if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            if (result == SMSSDK.RESULT_COMPLETE) {
                                // TODO 处理成功得到验证码的结果
                                Toast.makeText(registePageActivity, "验证码已发送，请注意查收", Toast.LENGTH_SHORT).show();
                                // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                            } else {
                                // TODO 处理错误的结果
                                Log.i("验证码", "获取验证码失败");
                                ((Throwable) data).printStackTrace();
                            }
                        } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            if (result == SMSSDK.RESULT_COMPLETE) {
                                // TODO 处理验证码验证通过的结果
                                registeFuncation();
                            } else {
                                // TODO 处理错误的结果
                                Toast.makeText(registePageActivity, "验证码验证失败", Toast.LENGTH_LONG).show();
                                ((Throwable) data).printStackTrace();
                            }
                        }
                        // TODO 其他接口的返回结果也类似，根据event判断当前数据属于哪个接口
                        return false;
                    }
                }).sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eventHandler);
    }

    private void RegisteVerification() {
        if (!(TextUtils.isEmpty(registePageActivity.getTel()) || TextUtils.isEmpty(registePageActivity.getPwd())
                || TextUtils.isEmpty(registePageActivity.getSecPwd()))) {
            if (registePageActivity.getPwd().length() >= 6 && registePageActivity.getPwd().length() <= 18) {
                if (registePageActivity.getPwd().equals(registePageActivity.getSecPwd())) {
                    if (!TextUtils.isEmpty(registePageActivity.getYzm()))
                        SMSSDK.submitVerificationCode("86", registePageActivity.getTel(), registePageActivity.getYzm());
                    else Toast.makeText(registePageActivity, "请输入验证码", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(registePageActivity, "两次密码不一致", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(registePageActivity, "密码长度不符合要求", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(registePageActivity, "请填写完整的注册信息", Toast.LENGTH_SHORT).show();
        }
    }

    private void registeFuncation() {
        errorInfo.clear();
        BmobInfoRegisteResult = 0;
        JMInfoRegisteResult = 0;
        User user = new User();
        user.setUsername(registePageActivity.getTel());
        netName = registePageActivity.getTel()+DateUtils.getTimeStamp();
        if (CHOOSEICON) {
            user.setAvatarUri("small-target.test.upcdn.net/head/"+ netName +".jpg");
            upLoadHeadIcon();
        }
        user.setNickName(registePageActivity.getNickName());
        user.setPassword(registePageActivity.getPwd());
        user.setCachePwd(registePageActivity.getPwd());
        user.setMobilePhoneNumber(registePageActivity.getTel());
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    BmobInfoRegisteResult = 1;
                } else {
                    errorInfo.add(e.getMessage());
                }
                registeResult(BmobInfoRegisteResult, JMInfoRegisteResult);
            }
        });
        RegisterOptionalUserInfo optionalUserInfo = new RegisterOptionalUserInfo();
        optionalUserInfo.setNickname(registePageActivity.getNickName());
        if(file!=null)
        optionalUserInfo.setAvatar(file.getAbsolutePath());
        JMessageClient.register(registePageActivity.getTel(), registePageActivity.getPwd(), optionalUserInfo, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    JMInfoRegisteResult = 1;
                } else {
                    errorInfo.add(s);
                }
                registeResult(BmobInfoRegisteResult, JMInfoRegisteResult);

            }
        });

        unRegisteEventHandler();
    }

    private void upLoadHeadIcon() {

        if (file==null){
            Toast.makeText(registePageActivity, "您稍后可以继续上传头像", Toast.LENGTH_SHORT).show();
        }else {
            String path = UPYunUtils.getUPLoadPath(UPYunUtils.PATH_HEAD, netName, UPYunUtils.JPG);
            UPYunUtils.upLoadFile(file, path, new UpLoadResultListener() {
                @Override
                public void result(boolean isSuccess, String resultInfo, String resultPath) {
                    if (isSuccess) {
                        Toast.makeText(registePageActivity, "头像上传成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(registePageActivity, "头像上传失败" + resultInfo, Toast.LENGTH_SHORT).show();
                    }
                }
            },registePageActivity);
        }
    }

    public void chooseHeadIconResult(Uri uri) {
        file = new File(registePageActivity.getCacheDir() + "/uCrop.jpg");
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
                .start(registePageActivity);
    }

    public void cutIconResult() {
        try {
            Bitmap bitmap = Bitmap.createBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
            registePageActivity.reFreshHeadIcon(bitmap);
            CHOOSEICON = true;
        } catch (Exception e) {
            Log.i("捕获异常2", e.getMessage());
        }
    }

    private String getPAth(Uri uri) {
        String path = null;
        if (!TextUtils.isEmpty(uri.getAuthority())) {
            Cursor cursor = registePageActivity.getContentResolver().query(uri,
                    new String[]{MediaStore.Images.Media.DATA}, null, null, null);
            if (null == cursor) {
                Toast.makeText(registePageActivity, "无法获取该文件路径", Toast.LENGTH_SHORT).show();
                return null;
            }
            cursor.moveToFirst();
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();
        } else {
            path = uri.getPath();
        }
        return path;
    }

    private void unRegisteEventHandler() {
        SMSSDK.unregisterEventHandler(eventHandler);
    }

    private void registeResult(int a, int b) {
        if (errorInfo.size() == 0) {
            if (a + b == 2)
                Toast.makeText(registePageActivity, "注册成功", Toast.LENGTH_SHORT).show();
        } else {
            StringBuilder builder = new StringBuilder("注册失败————");
            for (String str : errorInfo) {
                builder.append(str);
            }
            Toast.makeText(registePageActivity, builder, Toast.LENGTH_SHORT).show();
        }
    }
}
