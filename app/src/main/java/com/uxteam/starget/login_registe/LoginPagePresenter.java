package com.uxteam.starget.login_registe;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.uxteam.starget.R;

public class LoginPagePresenter implements InputTextChecked {
    private int nameSize=0;
    private int pwdSize=0;
    private LoginPageActivity activity;
    public LoginPagePresenter(LoginPageActivity activity) {
        this.activity = activity;
    }

    public void load() {
        activity.refreshLoginbtn(false);
        activity.bindControlEvent(initAccountTextChangeListener(),initPwdTextChangeListener(),initClickListener());

    }
    private TextWatcher initAccountTextChangeListener(){
        return new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()==11){
                    namesize(1);
                }else {
                    namesize(0);
                }
                changeState();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }
    private TextWatcher initPwdTextChangeListener(){
        return new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>6){
                    pwdsize(1);
                }else {
                    pwdsize(0);
                }
                changeState();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }
    private View.OnClickListener initClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.loginbtn:
                        Toast.makeText(activity, "点击了登录按钮", Toast.LENGTH_SHORT).show();
                        Log.i("Presenter","点击了登录按钮");
                        break;
                    case R.id.lost_pwd:
                        Toast.makeText(activity, "点击了忘记密码按钮", Toast.LENGTH_SHORT).show();
                        Log.i("Presenter","点击了忘记密码按钮");
                        break;
                    case R.id.user_registe:
                        Toast.makeText(activity, "点击了注册按钮", Toast.LENGTH_SHORT).show();
                        Log.i("Presenter","点击了注册按钮");
                        break;
                }
            }
        };
    }

    @Override
    public void namesize(int a) {
        this.nameSize=a;
    }

    @Override
    public void pwdsize(int b) {
        this.pwdSize=b;
    }

    @Override
    public void changeState() {
        if (this.nameSize+this.pwdSize==2){
            activity.refreshLoginbtn(true);
        }else {
            activity.refreshLoginbtn(false);
        }
    }
}
