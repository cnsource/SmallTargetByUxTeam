package com.uxteam.starget.login_registe;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.uxteam.starget.R;
import com.uxteam.starget.app_utils.UPYunUtils;
import com.uxteam.starget.main_face.MainfacePage;
import com.uxteam.starget.bmob_sys_pkg.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

public class LoginPagePresenter implements InputTextChecked {
    private int nameSize = 0;
    private int pwdSize = 0;
    private int loginResult = 0;
    private LoginPageActivity activity;
    private String account;
    private String pwd;
    private int BMLoginResult = 0;
    private int JMLoginResult = 0;
    private List<String> errorInfos = new ArrayList<>();

    public LoginPagePresenter(LoginPageActivity activity) {
        this.activity = activity;
    }

    public LoginPagePresenter load() {
        activity.refreshLoginbtn(false);
        activity.bindControlEvent(initAccountTextChangeListener(), initPwdTextChangeListener(), initClickListener());
        return this;
    }

    private TextWatcher initAccountTextChangeListener() {
        return new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 11) {
                    namesize(1);
                    BmobQuery<User> query=new BmobQuery<>();
                    query.addWhereEqualTo("username",editable.toString()+"");
                    Log.i("UserName",editable.toString()+"");
                    query.findObjects(new FindListener<User>(){
                          @Override
                        public void done(List<User> list, BmobException e) {
                              if (e!=null||list.get(0)==null){
                                  Log.i("HasNull","--------");
                              }
                              else{
                                  if (list.get(0).getAvatarUri()!=null)
                                      activity.loadCirHeadIcon("http://"+list.get(0).getAvatarUri());
                        }}
                    });
                } else {
                    namesize(0);
                    activity.resetHeadIcon();
                }
                changeState();
                activity.transerParameter();
            }
        };
    }

    private TextWatcher initPwdTextChangeListener() {
        return new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() >= 6) {
                    pwdsize(1);
                } else {
                    pwdsize(0);
                }
                changeState();
                activity.transerParameter();
            }
        };
    }

    private View.OnClickListener initClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.loginbtn:
                        loginEvent();
                        break;
                    case R.id.lost_pwd:
                        Toast.makeText(activity, "点击了忘记密码按钮", Toast.LENGTH_SHORT).show();
                        Log.i("Presenter", "点击了忘记密码按钮");
                        break;
                    case R.id.user_registe:
                        activity.startActivity(new Intent(activity, RegistePageActivity.class));
                        Log.i("Presenter", "点击了注册按钮");
                        break;
                }
            }
        };
    }

    private void loginEvent() {
        errorInfos.clear();
        loginResult = 0;
        BmobUser.loginByAccount(account, pwd, new LogInListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    BMLoginResult = 1;
                    Log.i("ResultBM", "登录成功" + loginResult);
                } else {
                    errorInfos.add(e.getMessage());
                    Log.e(getClass().getName(), "BmobLoginError-" + e.getMessage());
                }
                tryLogin(BMLoginResult, JMLoginResult);
            }
        });

        JMessageClient.login(account, pwd, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    JMLoginResult = 1;
                    Log.i("ResultJM", "登录成功" + loginResult);
                } else {
                    errorInfos.add(s);
                    Log.e(getClass().getName(), "JMessageLoginError-" + s);
                }
                tryLogin(BMLoginResult, JMLoginResult);
            }
        });
    }

    private void tryLogin(int a, int b) {
        if (errorInfos.size() == 0) {
            if (a + b == 2) {
                //  Todo  登陆成功
                activity.startActivity(new Intent(activity, MainfacePage.class));
                activity.finish();
            }
        } else {
            StringBuilder builder = new StringBuilder("登录失败————");
            for (String str : errorInfos) {
                builder.append(str);
            }
            Toast.makeText(activity, builder, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void namesize(int a) {
        this.nameSize = a;
    }

    @Override
    public void pwdsize(int b) {
        this.pwdSize = b;
    }

    @Override
    public void changeState() {
        if (this.nameSize + this.pwdSize == 2) {
            activity.refreshLoginbtn(true);
        } else {
            activity.refreshLoginbtn(false);
        }
    }

    public LoginPagePresenter transerParameter(String account, String pwd) {
        this.account = account;
        this.pwd = pwd;
        return this;
    }
}
