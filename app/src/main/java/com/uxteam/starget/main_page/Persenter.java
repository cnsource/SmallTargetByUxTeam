package com.uxteam.starget.main_page;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.uxteam.starget.app_utils.DateUtils;
import com.uxteam.starget.bmob_sys_pkg.Target;
import com.uxteam.starget.bmob_sys_pkg.User;
import com.uxteam.starget.formulation_targets.FormulationActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.callback.GetUserInfoListCallback;
import cn.jpush.im.android.api.model.UserInfo;

public class Persenter {
    private TargetDymicPage targetDymicPage;
    private List<String> pubName=new ArrayList<>();
    private List<Target> pubTarget=new ArrayList<>();
    private int state=0;

    public Persenter(TargetDymicPage targetDymicPage) {

        this.targetDymicPage = targetDymicPage;
    }

    public Persenter load(){
        targetDymicPage.bindViewEvent(refreshListenerProvider(),clickListenerProvider(),adapterPresenter());
        if (pubTarget.size()==0)
            getFrendList();
        return this;
    }
    private SwipeRefreshLayout.OnRefreshListener refreshListenerProvider(){
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                    getFrendList();
            }
        };
    }
    private View.OnClickListener clickListenerProvider(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                targetDymicPage.startActivity(new Intent(targetDymicPage.getContext(), FormulationActivity.class));
            }
        };
    }
    private RecyclerView.Adapter adapterPresenter(){
        return new TargetDymicRecAdt(targetDymicPage.getContext(),pubTarget);
    }
    private void getFrendList(){
        pubTarget.clear();
        ContactManager.getFriendList(new GetUserInfoListCallback() {
            @Override
            public void gotResult(int responseCode, String responseMessage, List<UserInfo> userInfoList) {
                if (0 == responseCode) {
                    //获取好友列表成功
                    targetDymicPage.closeFresh();
                    for (UserInfo user:userInfoList)
                        findUser(user.getUserName());
                } else {
                    //获取好友列表失败
                }
            }
        });
    }

    private void findUser(String userName) {
        Log.i("PPPPUserName",userName);
        BmobQuery<User> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("username",userName);
        query1.include("publisher");
        query1.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                findTargets(list.get(0));
            }
        });

    }

    private void findTargets(User user) {
        BmobQuery<Target> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("publisher",user);
        BmobQuery<Target> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("isPublic",true);

        List<BmobQuery<Target>> set=new ArrayList<>();
        set.add(query1);
        //set.add(targetsquery);
        set.add(query2);
        //set.add(query3);
        BmobQuery<Target> query4=new BmobQuery<>();
        query4.order("-createdAt");
        query4.and(set);
        query4.findObjects(new FindListener<Target>() {
            @Override
            public void done(List<Target> list, BmobException e) {
                if (e==null&&list!=null){

                    pubTarget.addAll(list);
                    Toast.makeText(targetDymicPage.getContext(), ""+list.size(), Toast.LENGTH_SHORT).show();
                    targetDymicPage.freshView();
                }
            }
        });
    }
}
