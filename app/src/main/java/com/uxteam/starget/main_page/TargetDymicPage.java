package com.uxteam.starget.main_page;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uxteam.starget.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TargetDymicPage extends Fragment {

    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private Bundle savedInstanceState;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_main_page, container, false);
        bindViewControl(view);
        new TargetDymicPagePresenter(this).load();
        return view;
    }

    private void bindViewControl(View  view) {
        refreshLayout = view.findViewById(R.id.dymic_target_refresh);
        recyclerView = view.findViewById(R.id.dymic_targets_rec);
        fab = view.findViewById(R.id.dymic_open_add_target);
    }
    public void bindViewEvent(SwipeRefreshLayout.OnRefreshListener refreshListener, View.OnClickListener clickListener,RecyclerView.Adapter adapter){
        refreshLayout.setOnRefreshListener(refreshListener);
        refreshLayout.setRefreshing(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        fab.setOnClickListener(clickListener);
    }

    public void closeFresh(){
        refreshLayout.setRefreshing(false);
    }
    public void freshView(){
        recyclerView.getAdapter().notifyDataSetChanged();
    }
}
