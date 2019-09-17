package com.uxteam.starget.main_face;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.uxteam.starget.R;
import com.uxteam.starget.main_page.TargetDymicPage;
import com.uxteam.starget.plan_page.PlanPage;
import com.uxteam.starget.self_page.SelfPage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;

public class MainfacePage extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    showFragment(new PlanPage());
                    //getSupportFragmentManager().beginTransaction().replace(R.id.page_container,new PlanPage()).commit();
                    return true;
                case R.id.navigation_dashboard:
                    showFragment(new TargetDymicPage());
                    //getSupportFragmentManager().beginTransaction().replace(R.id.page_container,new TargetDymicPage()).commit();
                    return true;
                case R.id.navigation_notifications:
                    showFragment(new SelfPage());
                    //getSupportFragmentManager().beginTransaction().replace(R.id.page_container,new SelfPage()).commit();
                    return true;
            }
            return false;
        }
    };
    private Fragment currentFragment;
    private TextView tip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainface_page);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navView.getChildAt(0);
        View tab = menuView.getChildAt(2);
        BottomNavigationItemView itemView = (BottomNavigationItemView) tab;
        View badge = LayoutInflater.from(this).inflate(R.layout.badge, menuView, false);
        tip = badge.findViewById(R.id.un_read_msg);
        itemView.addView(badge);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        showFragment(new PlanPage());
        setTipCnt(JMessageClient.getAllUnReadMsgCount(),tip);
        EventBus.getDefault().register(this);
        // getSupportFragmentManager().beginTransaction().replace(R.id.page_container,new PlanPage()).commit();
    }
    private void setTipCnt(int cnt, TextView view){
        view.setVisibility(View.VISIBLE);
        if (cnt>99)
            view.setText("99+");
        else if (cnt>0)
            view.setText(cnt+"");
        else
            view.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTipCnt(JMessageClient.getAllUnReadMsgCount(),tip);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTipCnt(JMessageClient.getAllUnReadMsgCount(),tip);
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (currentFragment == null) {
            transaction.add(R.id.page_container, fragment).show(fragment).commit();
            currentFragment = fragment;
        } else {
            if (currentFragment != fragment) {
                transaction.hide(currentFragment);
                if (!fragment.isAdded()) {
                    transaction.add(R.id.page_container, fragment).show(fragment).commit();
                } else {
                    transaction.show(fragment).commit();
                }
                currentFragment=fragment;
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void messageEvent(MessageEvent messageEvent) {
        setTipCnt(JMessageClient.getAllUnReadMsgCount(),tip);
        Log.i("接受消息测试p1","在线");
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void messageEvent(OfflineMessageEvent offlineMessageEvent) {
        setTipCnt(JMessageClient.getAllUnReadMsgCount(),tip);
        Log.i("接受消息测试p2","在线");
    }

}
