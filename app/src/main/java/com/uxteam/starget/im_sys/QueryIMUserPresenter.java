package com.uxteam.starget.im_sys;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.uxteam.starget.R;
import com.uxteam.starget.app_utils.MyBmobUtils;
import com.uxteam.starget.app_utils.UserUtils;
import com.uxteam.starget.bmob_sys_pkg.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

public class QueryIMUserPresenter {
    private QueryIMUser queryIMUser;
    private User user;
    private String url;

    public QueryIMUserPresenter(QueryIMUser queryIMUser) {
        this.queryIMUser = queryIMUser;
    }
    public  QueryIMUserPresenter load(){
        queryIMUser.setUserInfolayout(View.INVISIBLE);
        queryIMUser.bindViewEvent(clickListenerProvider());
        return this;
    }

    private View.OnClickListener clickListenerProvider(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.back:
                        Toast.makeText(queryIMUser, "Back", Toast.LENGTH_SHORT).show();
                        queryIMUser.Qfinish();
                        break;
                    case R.id.querybtn:
                        queryIMUser.setUserInfolayout(View.GONE);
                        queryUser(queryIMUser.getEditTextInfo());
                        break;

                    case R.id.addBtn:
                        addUser();

                        break;
                }
            }
        };
    }

    private void addUser() {
        queryIMUser.setBtnstate("添加为好友",true);
        ContactManager.sendInvitationRequest(user.getUsername(), null, "hello", new BasicCallback() {
            @Override
            public void gotResult(int responseCode, String responseMessage) {
                if (0 == responseCode) {
                    //好友请求请求发送成功
                    queryIMUser.setBtnstate("已发送好友请求",false);
                    Toast.makeText(queryIMUser, "添加好友请求已经发送，请耐心等待。", Toast.LENGTH_SHORT).show();
                } else {
                    //好友请求发送失败
                    queryIMUser.setBtnstate("已经是好友",false);
                    Toast.makeText(queryIMUser, "你们已经是好友了", Toast.LENGTH_SHORT).show();
                    Log.e("发送好友请求","失败："+responseMessage);
                }
            }
        });
    }

    private void showUserData(User user) {
        this.user=user;
        url = null;
        Toast.makeText(queryIMUser, "查询到了"+user.getUsername(), Toast.LENGTH_SHORT).show();
        if (user.getAvatarUri()!=null)
            url ="http://"+user.getAvatarUri();
        JMessageClient.getUserInfo(user.getUsername(), null, new GetUserInfoCallback(){
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                if (i==0){
                    queryIMUser.reFreshView(url,userInfo.getDisplayName());
                }
            }
        });

        queryIMUser.setUserInfolayout(View.VISIBLE);
    }
    private void queryUser(String username) {
        if (BmobUser.isLogin()) {
            User user = BmobUser.getCurrentUser(User.class);
            if (username.equals(user.getUsername())) {
                this.user=user;
            }
        }
        BmobQuery<User> query = new BmobQuery<>();
        Log.e("MyBmobUtils","QueryName_"+username);
        query.addWhereEqualTo("username", username);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    showUserData(list.get(0));
                    Log.e("MyBmobUtils","获取用户成功——"+list);
                }else {
                    Toast.makeText(queryIMUser, "不存在该用户", Toast.LENGTH_SHORT).show();
                    Log.e("MyBmobUtils","获取用户失败——"+e.getMessage());
                }
            }
        });
    }
}
