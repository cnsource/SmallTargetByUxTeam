package com.uxteam.starget.self_page;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uxteam.starget.R;
import com.uxteam.starget.main_page.TargetDymicPagePresenter;

public class HistoryActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private Bundle savedInstanceState;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main_page);
        bindViewControl();
        new TargetDymicPagePresenter(this).loadHistory();
    }
    private void bindViewControl() {
        title = findViewById(R.id.title);
        refreshLayout = findViewById(R.id.dymic_target_refresh);
        recyclerView = findViewById(R.id.dymic_targets_rec);
        fab = findViewById(R.id.dymic_open_add_target);
    }
    @SuppressLint("RestrictedApi")
    public void bindViewEvent(SwipeRefreshLayout.OnRefreshListener refreshListener, RecyclerView.Adapter adapter){
        refreshLayout.setOnRefreshListener(refreshListener);
        refreshLayout.setRefreshing(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        fab.setVisibility(View.GONE);
        title.setVisibility(View.GONE);
    }

    public void closeFresh(){
        refreshLayout.setRefreshing(false);
    }
    public void freshView(){
        recyclerView.getAdapter().notifyDataSetChanged();
    }
}
