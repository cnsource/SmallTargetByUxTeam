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

import com.uxteam.starget.R;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.io.File;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegistePagePresenter implements View.OnClickListener {
    public static final int REQUEST_CODE_CHOOSE = 1;
    private RegistePageActivity registePageActivity;
    private EventHandler eventHandler;
    private File file;

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
                SMSSDK.getVerificationCode("86", registePageActivity.getTel());
                break;
            case R.id._registe_btn:
                SMSSDK.submitVerificationCode("86", registePageActivity.getTel(), registePageActivity.getYzm());
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
                                startRegiste();
                                Toast.makeText(registePageActivity, "验证码验证通过", Toast.LENGTH_LONG).show();
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

    private void startRegiste() {
        unRegisteEventHandler();
    }

    public void switchIconResult(Uri uri) {
        file = new File(registePageActivity.getCacheDir() + "/uCrop.jpg");
        if (file.exists()){
            file.delete();
        }
        UCrop.Options options=new UCrop.Options();
        options.setCircleDimmedLayer(true);
        options.setRootViewBackgroundColor(Color.WHITE);
        options.setToolbarTitle("裁剪头像");
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        UCrop.of(uri, Uri.fromFile(file))
                .withAspectRatio(16, 16)
                .withMaxResultSize(128, 128)
                .withOptions(options)
                .start(registePageActivity);
    }

    public void cutIconResult() {
        try {
            Bitmap bitmap = Bitmap.createBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
            registePageActivity.reFreshHeadIcon(bitmap);
        }catch (Exception e){
            Log.i("捕获异常2",e.getMessage());
        };
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

}
