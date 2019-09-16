package com.uxteam.starget.login_registe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.uxteam.starget.R;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class LoginPageActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private CircleImageView cirHeadIcon;
    private EditText account;
    private EditText password;
    private Button loginbtn;
    private TextView lostPwd;
    private TextView userRegiste;
    private LoginPagePresenter loginPagePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
        setContentView(R.layout.login_registe_login_page);
        bindViewControl();
        loginPagePresenter = new LoginPagePresenter(this);
        loginPagePresenter.load();
    }

    private void bindViewControl() {
        cirHeadIcon = findViewById(R.id._registe_headIcon);
        account = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginbtn = findViewById(R.id.loginbtn);
        lostPwd = findViewById(R.id.lost_pwd);
        userRegiste = findViewById(R.id.user_registe);
    }

    public void resetHeadIcon() {
        cirHeadIcon.setImageDrawable(getDrawable(R.drawable.aurora_headicon_default));
    }

    public void refreshLoginbtn(boolean bool) {
        loginbtn.setEnabled(bool);
    }

    public void transerParameter(){
        loginPagePresenter.transerParameter(account.getText().toString(),password.getText().toString());
    }
    public void bindControlEvent(TextWatcher accountChangeListener, TextWatcher pwdChangeListener, View.OnClickListener clickListener) {
        account.addTextChangedListener(accountChangeListener);
        password.addTextChangedListener(pwdChangeListener);
        loginbtn.setOnClickListener(clickListener);
        lostPwd.setOnClickListener(clickListener);
        userRegiste.setOnClickListener(clickListener);
    }

    public void loadCirHeadIcon(String url) {
        Glide.with(this).load(url).error(getDrawable(R.drawable.aurora_headicon_default)).into(cirHeadIcon);
    }
    private void checkPermission() {
        String[] permissions=new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        for (String permiss:permissions){
            if (EasyPermissions.hasPermissions(this,permiss)){
            }else{
                EasyPermissions.requestPermissions(this, "",121,permiss);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(this, "用户授权失败，请手动开启所需权限，否则本软件可能部分功能受限", Toast.LENGTH_LONG).show();
        /**
         * 若是在权限弹窗中，用户勾选了'NEVER ASK AGAIN.'或者'不在提示'，且拒绝权限。
         * 这时候，需要跳转到设置界面去，让用户手动开启。
         */
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
}
