package com.uxteam.starget.plan_page;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
    private List<Target> targets = new ArrayList<>();
    private PlanPageRecAdt adt;
    private int ViewSign = 0;

    public PlanPagePresenter(PlanPage planPage) {
        this.planPage = planPage;
    }

    public PlanPagePresenter load() {
        planPage.bindControlEvent(refreshListenerProvider(), adtProvider(), itemSelectedListenerProvider());
        initViewData(ViewSign);
        return this;
    }

    private PlanPageRecAdt adtProvider() {

        adt = new PlanPageRecAdt(planPage.getContext(), targets);
        adt.setViewSign(0);
        return adt;
    }

    private SwipeRefreshLayout.OnRefreshListener refreshListenerProvider() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initViewData(ViewSign);
                planPage.closeRefresh();
            }
        };
    }

    private AdapterView.OnItemSelectedListener itemSelectedListenerProvider() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    ViewSign = 0;
                } else {
                    ViewSign = 1;
                }
                adt.setViewSign(ViewSign);
                initViewData(ViewSign);
                planPage.refreshData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }

    private void initViewData(int ViewSign) {
            QueryData(ViewSign);
    }

    private void QueryData(int ViewSign){
        BmobQuery<Target> query = new BmobQuery<>();
        if (ViewSign == 0)
            query.addWhereEqualTo("publisher", BmobUser.getCurrentUser(User.class));
        else {
            query.addWhereEqualTo("supervisor", BmobUser.getCurrentUser(User.class));
        }
        query.include("publisher,supervisor");
        BmobQuery<Target> query2 = new BmobQuery<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String createdAt = format.format(new Date()) + " 00:00:01";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date createdAtDate = null;
        try {
            createdAtDate = sdf.parse(createdAt);
        } catch (ParseException e) {
            Toast.makeText(planPage.getContext(), "数据获取错误", Toast.LENGTH_SHORT).show();
            Log.i("DataGetError", e.getMessage());
            e.printStackTrace();
        }
        BmobDate date = new BmobDate(createdAtDate);
        query2.addWhereGreaterThanOrEqualTo("createdAt", date);
        List<BmobQuery<Target>> querySet = new ArrayList<>();
        querySet.add(query);
        querySet.add(query2);
        BmobQuery<Target> querys = new BmobQuery<>();
        querys.and(querySet);
        querys.findObjects(new FindListener<Target>() {
            @Override
            public void done(List<Target> list, BmobException e) {
                if (e == null) {
                    Log.i("List表", "" + list.size());
                    targets.clear();
                    targets.addAll(list);
                    planPage.refreshData();
                } else {
                    Log.e("查询失败", e.getMessage());
                }
            }
        });
    }
}
