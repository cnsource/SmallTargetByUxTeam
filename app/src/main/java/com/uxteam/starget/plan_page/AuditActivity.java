package com.uxteam.starget.plan_page;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.uxteam.starget.R;
import com.uxteam.starget.app_utils.UPYunUtils;
import com.uxteam.starget.bmob_sys_pkg.Target;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class AuditActivity extends AppCompatActivity implements View.OnClickListener {

    private Button passBtn;
    private Button failureBtn;
    private TextView audit_content;
    private ImageView auditImg;
    private Target target;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit);
        bindView();
        bindEvent();
        loadData();
    }

    private void loadData() {
        Intent in = getIntent();
        BmobQuery<Target> query = new BmobQuery<>();
        query.addWhereEqualTo("objectId", in.getStringExtra("objectId"));
        query.findObjects(new FindListener<Target>() {
            @Override
            public void done(List<Target> list, BmobException e) {
                target = list.get(0);
            }
        });
        Glide.with(this).load(UPYunUtils.getSourcePath(in.getStringExtra("audit-img"))).placeholder(R.mipmap.loading).into(auditImg);
        audit_content.setText(in.getStringExtra("audit-content"));
    }

    private void bindView() {
        auditImg = findViewById(R.id.auditImg);
        audit_content = findViewById(R.id.audit_content);
        passBtn = findViewById(R.id.passBtn);
        failureBtn = findViewById(R.id.failure);
    }

    private void bindEvent() {
        passBtn.setOnClickListener(this);
        failureBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.passBtn:
                target.setAudited(true);
                target.setAuditResult(true);
                break;
            case R.id.failure:
                target.setAudited(true);
                target.setAuditResult(false);
                break;
        }
        target.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    passBtn.setVisibility(View.INVISIBLE);
                    failureBtn.setVisibility(View.INVISIBLE);
                    Toast.makeText(AuditActivity.this, "审核成功！", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(AuditActivity.this, "失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
