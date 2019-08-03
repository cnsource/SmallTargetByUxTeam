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
        spinner = view.findViewById(R.id.plan_page_spinner);
    }
    public void bindControlEvent(SwipeRefreshLayout.OnRefreshListener refreshListener, PlanPageRecAdt targetadt, AdapterView.OnItemSelectedListener itemSelectedListener){
        refreshView.setColorSchemeColors(Color.RED);
        refreshView.setOnRefreshListener(refreshListener);
        targets.setLayoutManager(new LinearLayoutManager(getContext()));
        targets.setAdapter(targetadt);
        spinner.setOnItemSelectedListener(itemSelectedListener);
    }
    public void closeRefresh(){
        refreshView.setRefreshing(false);
    }
     public void refreshData(){
         Log.i("调用了",""+ targets.getAdapter().getItemCount());
        targets.getAdapter().notifyDataSetChanged();
     }
}
