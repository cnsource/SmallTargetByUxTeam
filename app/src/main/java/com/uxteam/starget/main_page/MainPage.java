package com.uxteam.starget.main_page;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.uxteam.starget.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainPage extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_main_page, container, false);

        new MainPagePresenter(this).load();
        return view;
    }

}
