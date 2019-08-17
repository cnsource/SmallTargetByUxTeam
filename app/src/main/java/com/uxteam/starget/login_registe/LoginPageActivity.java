package com.uxteam.starget.login_registe;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.uxteam.starget.R;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginPageActivity extends AppCompatActivity {

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
}
