package com.uxteam.starget.plan_page;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class PlanPagePresenter {
    private PlanPage planPage;

    public PlanPagePresenter(PlanPage planPage) {
        this.planPage = planPage;
    }

    public void load(){
        planPage.bindControlEvent(refreshListenerProvider(),adtProvider());
    }
    private PlanPageRecAdt adtProvider(){

        PlanPageRecAdt adt=new PlanPageRecAdt(context);

        return null;
    }
    private SwipeRefreshLayout.OnRefreshListener refreshListenerProvider(){
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        };
    }
}
