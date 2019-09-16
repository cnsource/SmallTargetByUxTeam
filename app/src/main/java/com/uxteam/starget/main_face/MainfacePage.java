package com.uxteam.starget.main_face;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.uxteam.starget.R;
import com.uxteam.starget.main_page.TargetDymicPage;
import com.uxteam.starget.plan_page.PlanPage;
import com.uxteam.starget.self_page.SelfPage;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainface_page);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        showFragment(new PlanPage());
        // getSupportFragmentManager().beginTransaction().replace(R.id.page_container,new PlanPage()).commit();
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

}
