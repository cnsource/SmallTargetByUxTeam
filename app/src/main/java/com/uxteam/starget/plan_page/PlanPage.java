package com.uxteam.starget.plan_page;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

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
    private Spinner spinner;
    private RecyclerView supervision;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan_page, container, false);
        bindViewControl(view);
        new PlanPagePresenter(this).load();
        return view;
    }

    private void bindViewControl(View view) {
        refreshView = view.findViewById(R.id.planpage_refreshView);
        targets = view.findViewById(R.id.planpage_targets);
        supervision = view.findViewById(R.id.planpage_supervision);
        spinner = view.findViewById(R.id.plan_page_spinner);
    }

    public void bindControlEvent(SwipeRefreshLayout.OnRefreshListener refreshListener, PlanPagePubRecAdt targetadt, PlanPageSupRecAdt supervisionadt, AdapterView.OnItemSelectedListener itemSelectedListener) {
        refreshView.setColorSchemeColors(Color.RED);
        refreshView.setOnRefreshListener(refreshListener);
        targets.setLayoutManager(new LinearLayoutManager(getContext()));
        targets.setAdapter(targetadt);
        supervision.setLayoutManager(new LinearLayoutManager(getContext()));
        supervision.setAdapter(supervisionadt);
        spinner.setOnItemSelectedListener(itemSelectedListener);
    }

    public void closeRefresh() {
        refreshView.setRefreshing(false);
    }

    public void showView(int i) {
        if (i == 0) {
            targets.setVisibility(View.VISIBLE);
            supervision.setVisibility(View.INVISIBLE);
        } else {
            targets.setVisibility(View.INVISIBLE);
            supervision.setVisibility(View.VISIBLE);
        }
    }

    public void refreshData() {
        supervision.getAdapter().notifyDataSetChanged();
        targets.getAdapter().notifyDataSetChanged();
    }
}
