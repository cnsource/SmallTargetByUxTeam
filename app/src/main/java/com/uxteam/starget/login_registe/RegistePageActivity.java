package com.uxteam.starget.login_registe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.uxteam.starget.R;

public class RegistePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registe_page);
        new RegistePagePresenter().load();
    }
}
