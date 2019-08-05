package com.uxteam.starget.formulation_targets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uxteam.starget.R;
import com.uxteam.starget.app_utils.DateUtils;
import com.uxteam.starget.bmob_sys_pkg.User;

public class FormulationTargetsPage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton addFab;
    private FloatingActionButton cencleFab;
    private Button reportBtn;
    private EditText edit;
    private LinearLayout inputLayout;
    private FabBehaviorAnimator fabBehaviorAnimator;
    private Spinner spinner;
    private TextView endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulation_targets_page);
        bindViewControl();
        new FTPPresenter(this).load();
    }

    private void bindViewControl() {
        inputLayout=findViewById(R.id.input_view);
        recyclerView = findViewById(R.id.ftp_rec_targets);
        addFab = findViewById(R.id.ftp_add_fab);
        cencleFab = findViewById(R.id.ftp_cencle_fab);
        reportBtn = findViewById(R.id.ftp_report_btn);
        edit = findViewById(R.id.ftp_targetContent);
        spinner = findViewById(R.id.supervisor_list);
        endTime = findViewById(R.id.endtime);
    }

    public void bindViewEvent(View.OnClickListener clickListener, FTPRecAdt ftpRecAdt, SpinnerAdapter spinnerAdapter, FabBehaviorAnimator behaviorAnimator) {
        addFab.setOnClickListener(clickListener);
        cencleFab.setOnClickListener(clickListener);
        reportBtn.setOnClickListener(clickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(ftpRecAdt);
        spinner.setAdapter(spinnerAdapter);
        setFabBehaviorAnimator(behaviorAnimator);
    }

    public String getTargetContent() {
        return edit.getText().toString();
    }
    public User getSupervisorSelected(){
        return (User) spinner.getSelectedItem();
    }
    public int getgetSupervisorSelectedId(){
        return (int) spinner.getSelectedItemId();
    }
    public void displayEdit(int visiable) {
        edit.setText("");
        endTime.setText(DateUtils.getNextDay());
        inputLayout.setVisibility(visiable);
    }

    private void setFabBehaviorAnimator(FabBehaviorAnimator fabBehaviorAnimator) {
        this.fabBehaviorAnimator = fabBehaviorAnimator;
    }

    public void startFabAnimator(boolean fabState) {
        if (fabState)
            fabBehaviorAnimator.fabout(addFab, cencleFab);
        else
            fabBehaviorAnimator.fabreset(addFab, cencleFab);
    }
    public void refreshRecAdt(){
        Log.i("调用了",""+ recyclerView.getAdapter().getItemCount());
        recyclerView.getAdapter().notifyDataSetChanged();
    }
}
