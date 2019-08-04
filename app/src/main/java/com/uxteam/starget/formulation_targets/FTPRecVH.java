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
    public EditText targetContent;
    public Spinner supervisor;
    public FTPRecVH(@NonNull View itemView) {
        super(itemView);
        supervisor= itemView.findViewById(R.id.supervisor_list);
        targetContent= itemView.findViewById(R.id.ftp_targetContent);
        endTime= itemView.findViewById(R.id.endtime);
    }
}
