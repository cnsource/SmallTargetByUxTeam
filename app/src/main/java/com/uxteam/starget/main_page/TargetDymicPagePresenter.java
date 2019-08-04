package com.uxteam.starget.main_page;

import android.content.Intent;
import android.view.View;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.uxteam.starget.formulation_targets.FormulationTargetsPage;

public class TargetDymicPagePresenter {
    private TargetDymicPage targetDymicPage;

    public TargetDymicPagePresenter(TargetDymicPage targetDymicPage) {
        this.targetDymicPage = targetDymicPage;
    }

    public TargetDymicPagePresenter load(){
        targetDymicPage.bindViewEvent(refreshListenerProvider(),clickListenerProvider());
        return this;
    }
    private SwipeRefreshLayout.OnRefreshListener refreshListenerProvider(){
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        };
    }
    private View.OnClickListener clickListenerProvider(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                targetDymicPage.startActivity(new Intent(targetDymicPage.getContext(), FormulationTargetsPage.class));
            }
        };
    }
}
