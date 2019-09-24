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
import com.uxteam.starget.self_page.HistoryActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.callback.GetUserInfoListCallback;
import cn.jpush.im.android.api.model.UserInfo;


public class TargetDymicPagePresenter {
    private TargetDymicPage targetDymicPage;
    private List<String> pubName=new ArrayList<>();
    private List<Target> pubTarget=new ArrayList<>();
    private HistoryActivity historyActivity;
    private int state=0;

    public TargetDymicPagePresenter(TargetDymicPage targetDymicPage) {
        this.targetDymicPage = targetDymicPage;
    }

    public TargetDymicPagePresenter(HistoryActivity historyActivity) {
        state = 1;
        this.historyActivity = historyActivity;
    }
    public TargetDymicPagePresenter load(){
        targetDymicPage.bindViewEvent(refreshListenerProvider(),clickListenerProvider(),adapterPresenter());
        if (pubTarget.size()==0)
            getFrendList();
        return this;
    }
    public TargetDymicPagePresenter loadHistory(){
        historyActivity.bindViewEvent(refreshListenerProvider(),HistoryadapterPresenter());
        if (pubTarget.size()==0)
            findHistory();
        return this;
    }

    private SwipeRefreshLayout.OnRefreshListener refreshListenerProvider(){
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (state==1)
                    findHistory();
                else
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
    private RecyclerView.Adapter HistoryadapterPresenter(){
        return new TargetDymicRecAdt(historyActivity,pubTarget);
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
                        findTargets(user.getUserName());
                } else {
                    //获取好友列表失败
                }
            }
        });
    }
    private void findHistory(){
        BmobQuery<Target> query=new BmobQuery<>();
        query.addWhereEqualTo("publisher", BmobUser.getCurrentUser(User.class));
        query.findObjects(new FindListener<Target>() {
            @Override
            public void done(List<Target> list, BmobException e) {
                if (list!=null&&e==null){
                    pubTarget.clear();
                    pubTarget.addAll(list);
                    historyActivity.freshView();
                    historyActivity.closeFresh();
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
                    if (list!=null&&e==null){
                        BmobQuery<Target> query = new BmobQuery<>();
                        query.addWhereEqualTo("publisher",list.get(0));
                        query.include("publisher");
                        BmobQuery<Target> query1 = new BmobQuery<>();
                        query.addWhereEqualTo("isPublic",true);

                        String createdAt = DateUtils.getBeforeDay(-3);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date createdAtDate = null;
                        try {
                            createdAtDate = sdf.parse(createdAt);
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                        BmobDate bmobCreatedAtDate = new BmobDate(createdAtDate);
                        BmobQuery<Target> targetsquery = new BmobQuery<>();
                        targetsquery.addWhereGreaterThanOrEqualTo("createdAt", bmobCreatedAtDate);

                        List<BmobQuery<Target>> set=new ArrayList<>();
                        set.add(query);
                        set.add(targetsquery);
                        set.add(query1);
                        BmobQuery<Target> query2=new BmobQuery<>();
                        query2.order("-createdAt");
                        query2.and(set);
                        query2.findObjects(new FindListener<Target>() {
                            @Override
                            public void done(List<Target> list, BmobException e) {
                                if (e==null&&list!=null){
                                    pubTarget.addAll(list);
                                    //Toast.makeText(targetDymicPage.getContext(), ""+list.size(), Toast.LENGTH_SHORT).show();
                                    targetDymicPage.freshView();
                                }
                            }
                        });
                    }
                }
            });

        }
}
