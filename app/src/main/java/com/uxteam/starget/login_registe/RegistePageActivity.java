package com.uxteam.starget.login_registe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.uxteam.starget.R;
import com.yalantis.ucrop.UCrop;
import com.zhihu.matisse.Matisse;

import java.io.File;
import java.util.List;

import cn.smssdk.EventHandler;
import de.hdodenhof.circleimageview.CircleImageView;

public class RegistePageActivity extends AppCompatActivity implements TransferData {

    private CircleImageView headIcon;
    private Button getYzm;
    private Button regBtn;
    private EditText nickName;
    private EditText tel;
    private EditText yzm;
    private EditText pwd;
    private EditText secpwd;
    private EventHandler eventHandler;
    private RegistePagePresenter presenter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_registe_registe_page);
        bindViewControl();
        presenter = new RegistePagePresenter(this).load();
    }

    private void bindViewControl() {
        headIcon = findViewById(R.id._registe_headIcon);
        nickName = findViewById(R.id._registe_nick_name);
        tel = findViewById(R.id._registe__tel);
        yzm = findViewById(R.id._registe_yzm);
        getYzm = findViewById(R.id._registe_getYzm);
        pwd = findViewById(R.id._registe_pwd);
        secpwd = findViewById(R.id._registe_secPwd);
        regBtn = findViewById(R.id._registe_btn);
        toolbar = findViewById(R.id.toolbar);
    }

    public void bindControlEvent(View.OnClickListener onClickListener) {
        headIcon.setOnClickListener(onClickListener);
        getYzm.setOnClickListener(onClickListener);
        regBtn.setOnClickListener(onClickListener);
        toolbar.setNavigationOnClickListener(onClickListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void reFreshHeadIcon(Bitmap bitmap) {
        headIcon.setImageBitmap(bitmap);
    }

    @Override
    public String getNickName() {
        return nickName.getText().toString();
    }

    @Override
    public String getTel() {
        return tel.getText().toString();
    }

    @Override
    public String getYzm() {
        return yzm.getText().toString();
    }

    @Override
    public String getPwd() {
        return pwd.getText().toString();
    }

    @Override
    public String getSecPwd() {
        return secpwd.getText().toString();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RegistePagePresenter.REQUEST_CODE_CHOOSE:
                try {
                    presenter.switchIconResult(Matisse.obtainResult(data).get(0));
                }catch (Exception e){
                    Log.i("捕获异常",e.getMessage());
                }
                break;
            case UCrop.REQUEST_CROP:
                presenter.cutIconResult();
                break;
            case UCrop.RESULT_ERROR:
                Throwable t = UCrop.getError(data);
                break;
        }
    }
}

interface TransferData {
    String getNickName();

    String getTel();

    String getYzm();

    String getPwd();

    String getSecPwd();
}