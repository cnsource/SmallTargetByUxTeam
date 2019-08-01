package com.uxteam.starget.app_utils;

import android.util.Log;

import com.uxteam.starget.bmob_sys_pkg.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MyBmobUtils {
    public static User getUser(String username) {
        final User[] users = new User[]{null};
        if (BmobUser.isLogin()) {
            User user = BmobUser.getCurrentUser(User.class);
            if (username.equals(user.getUsername())) {
                return user;
            }
        }
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("username", username);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null && list.size() > 0) {
                    users[0]=list.get(0);
                }else {
                    Log.e("MyBmobUtils","获取用户失败——"+e.getMessage());
                }
            }
        });
        return users[0];
    }
}
