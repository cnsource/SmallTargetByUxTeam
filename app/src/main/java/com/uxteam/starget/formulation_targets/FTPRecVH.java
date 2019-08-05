package com.uxteam.starget.formulation_targets;

import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uxteam.starget.R;


public class FTPRecVH extends RecyclerView.ViewHolder {
    public TextView endTime;
    public TextView targetcontent;
    public Spinner supervisor;
    public FTPRecVH(@NonNull View itemView) {
        super(itemView);
        supervisor= itemView.findViewById(R.id.target_supervisor);
        targetcontent= itemView.findViewById(R.id.target_content);
        endTime= itemView.findViewById(R.id.endtime);
    }
}
