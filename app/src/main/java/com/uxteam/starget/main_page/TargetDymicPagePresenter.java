package com.uxteam.starget.main_page;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.uxteam.starget.bmob_sys_pkg.Target;
import com.uxteam.starget.bmob_sys_pkg.User;
import com.uxteam.starget.formulation_targets.FormulationActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.callback.GetUserInfoListCallback;
import cn.jpush.im.android.api.model.UserInfo;


public class TargetDymicPagePresenter {
    private TargetDymicPage targetDymicPage;
    private List<String> pubName=new ArrayList<>();
    private List<Target> pubTarget=new ArrayList<>();
    public TargetDymicPagePresenter(TargetDymicPage targetDymicPage) {
        this.targetDymicPage = targetDymicPage;
    }

    public TargetDymicPagePresenter load(){
        targetDymicPage.bindViewEvent(refreshListenerProvider(),clickListenerProvider(),adapterPresenter());
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
        ContactManager.getFriendList(new GetUserInfoListCallback() {
            @Override
            public void gotResult(int responseCode, String responseMessage, List<UserInfo> userInfoList) {
                if (0 == responseCode) {
                    //获取好友列表成功
                    targetDymicPage.closeFresh();
                    Toast.makeText(targetDymicPage.getContext(), "查询到了好友列表"+userInfoList.size(), Toast.LENGTH_SHORT).show();
                    for (UserInfo user:userInfoList)
                        findTargets(user.getUserName());
                } else {
                    //获取好友列表失败
                }
            }
        });
    }
    private void findTargets(String name){
            BmobQuery<User> query = new BmobQuery<>();
            query.addWhereEqualTo("username",name);
            query.findObjects(new FindListener<User>() {
                @Override
                public void done(List<User> list, BmobException e) {
                    Toast.makeText(targetDymicPage.getContext(), "用户"+list.get(0).getUsername(), Toast.LENGTH_SHORT).show();
                    if (list!=null&&e==null){
                        BmobQuery<Target> query = new BmobQuery<>();
                        query.addWhereEqualTo("publisher",list.get(0));
                        query.include("publisher");
                        BmobQuery<Target> query1 = new BmobQuery<>();
                        query.addWhereEqualTo("isPublic",true);
                        List<BmobQuery<Target>> set=new ArrayList<>();
                        set.add(query);
                        set.add(query1);
                        BmobQuery<Target> query2=new BmobQuery<>();
                        query2.order("-createdAt");
                        query2.and(set);
                        query2.findObjects(new FindListener<Target>() {
                            @Override
                            public void done(List<Target> list, BmobException e) {
                                if (e==null&&list!=null){
                                    pubTarget.addAll(list);
                                    targetDymicPage.freshView();
                                }
                            }
                        });
                    }
                }
            });

        }
}
