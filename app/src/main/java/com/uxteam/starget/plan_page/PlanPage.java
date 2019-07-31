package com.uxteam.starget.plan_page;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.uxteam.starget.R;


public class PlanPage extends Fragment {

    private PlanPagePresenter mViewModel;

    public static PlanPage newInstance() {
        return new PlanPage();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan_page, container, false);

        new PlanPagePresenter(this).load();
        return view;
    }


}
