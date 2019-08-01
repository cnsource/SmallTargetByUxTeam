package com.uxteam.starget.plan_page;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.uxteam.starget.bmob_sys_pkg.Target;
import com.uxteam.starget.bmob_sys_pkg.User;

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

public class PlanPagePresenter {
    private PlanPage planPage;
    private List<Target> targets=new ArrayList<>();
    private PlanPageRecAdt adt;
    private SwipeRefreshLayout view;

    public PlanPagePresenter(PlanPage planPage) {
        this.planPage = planPage;
    }

    public PlanPagePresenter load(){
        planPage.bindControlEvent(refreshListenerProvider(),adtProvider());
        return this;
    }
    private PlanPageRecAdt adtProvider(){

        adt = new PlanPageRecAdt(planPage.getContext(),targets);

        return adt;
    }
    private SwipeRefreshLayout.OnRefreshListener refreshListenerProvider(){
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initViewData();
                view.setRefreshing(false);
            }
        };
    }

    private void initViewData(){
        try {
            QueryData();
        } catch (ParseException e) {
            Toast.makeText(planPage.getContext(), "数据获取错误", Toast.LENGTH_SHORT).show();
            Log.i("DataGetError",e.getMessage());
        }
        adtProvider().notifyDataSetChanged();
    }

    private void QueryData() throws ParseException {
        BmobQuery query1=new BmobQuery();
                query1.addWhereEqualTo("publisher",BmobUser.getCurrentUser(User.class));
                query1.order("-updatedAt");
                query1.include("publisher,supervisor");
        BmobQuery<Target> query2=new BmobQuery<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String createdAt = format.format(new Date())+" 00:00:01";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date createdAtDate = sdf.parse(createdAt);
        BmobDate date=new BmobDate(createdAtDate);
                query2.addWhereGreaterThanOrEqualTo("createdAt",date);
        List<BmobQuery<Target>> querySet=new ArrayList<>();
        querySet.add(query1);
        querySet.add(query2);
        BmobQuery<Target> querys=new BmobQuery<>();
        querys.and(querySet);
        querys.findObjects(new FindListener<Target>() {
            @Override
            public void done(List<Target> list, BmobException e) {
                targets.addAll(list);
            }
        });
    }
    public void closeRefresh(SwipeRefreshLayout view){
        this.view = view;
    }
}
