package com.uxteam.starget.self_page;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uxteam.starget.R;


public class SelfPage extends Fragment {

    private RecyclerView recyclerView;
    private Button loginOut;
    private TextView self_uname;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_self_page, container, false);
        initView(view);
        new SelfPagePresenter(this).load();
        return view;
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.rec_list);
        loginOut = view.findViewById(R.id.loginOut);
        recyclerView = view.findViewById(R.id.rec_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        self_uname = view.findViewById(R.id.self_username);
    }
    public void refreshView(String userName, SelfPageRecAdt selfPageRecAdt, View.OnClickListener loginOutListener){
        self_uname.setText(userName);
        recyclerView.setAdapter(selfPageRecAdt);
        loginOut.setOnClickListener(loginOutListener);
    }
}
