package com.uxteam.starget.im_sys;

import android.content.Intent;
import android.view.View;

import com.uxteam.starget.R;
import com.uxteam.starget.bmob_sys_pkg.User;

import java.util.ArrayList;
import java.util.List;

public class MyFrendsPresenter {
    private MyFrends myFrends;
    private List<User> users = new ArrayList<>();

    public MyFrendsPresenter(MyFrends myFrends) {
        this.myFrends = myFrends;
    }

    public MyFrendsPresenter load() {
        myFrends.bindViewEvent(clickListenerProvider(), adtProvider());
        return this;
    }

    private View.OnClickListener clickListenerProvider() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.addFrend:
                        myFrends.startActivity(new Intent(myFrends, QueryIMUser.class));
                        break;
                    case R.id.frendsRequestNotify:
                        myFrends.startActivity(new Intent(myFrends, NewFrend.class));
                        break;
                }
            }
        };
    }

    private FrendRecAdt adtProvider() {
        return new FrendRecAdt(myFrends.getApplicationContext(), users);
    }
}
