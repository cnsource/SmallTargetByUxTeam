package com.uxteam.starget.plan_page;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.uxteam.starget.R;


public class PlanPage extends Fragment {
    private SwipeRefreshLayout refreshView;
    private RecyclerView targets;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan_page, container, false);
        bindViewControl(view);
        new PlanPagePresenter(this).load().closeRefresh(refreshView);
        return view;
    }

    private void bindViewControl(View view) {
        refreshView = view.findViewById(R.id.planpage_refreshView);
        targets = view.findViewById(R.id.planpage_targets);
        refreshView.setColorSchemeColors(Color.RED);
        targets.setLayoutManager(new LinearLayoutManager(getContext()));
    }
    public void bindControlEvent(SwipeRefreshLayout.OnRefreshListener refreshListener,PlanPageRecAdt targetadt){
        refreshView.setOnRefreshListener(refreshListener);
        targets.setAdapter(targetadt);
    }

}
