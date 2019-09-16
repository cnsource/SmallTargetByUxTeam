package com.uxteam.starget.plan_page;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.uxteam.starget.R;
import com.uxteam.starget.app_utils.DateUtils;
import com.uxteam.starget.app_utils.UPYunUtils;
import com.uxteam.starget.bmob_sys_pkg.Target;
import com.uxteam.starget.bmob_sys_pkg.User;
import com.uxteam.starget.im_sys.ChatMsgListSenderVH;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;

public class PlanPagePubRecAdt extends RecyclerView.Adapter<PlanPagePubRecVH> {
    private Context context;
    private List<Target> targets;
    private String name;

    public PlanPagePubRecAdt(Context context, List<Target> targets) {
        this.context = context;
        this.targets = targets;
    }

    @NonNull
    @Override
    public PlanPagePubRecVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.plan_page_targetitem, parent, false);
        PlanPagePubRecVH vh = new PlanPagePubRecVH(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final PlanPagePubRecVH holder, final int position) {

        holder.startTime.setText(targets.get(position).getCreatedAt());
        holder.supervisor.setVisibility(View.VISIBLE);
        if (targets.get(position).getSupervisor() != null) {
            BmobQuery<User> userBmobQuery = new BmobQuery<>();
            userBmobQuery.addWhereEqualTo("objectId", targets.get(position).getSupervisor().getObjectId());
            userBmobQuery.findObjects(new FindListener<User>() {
                @Override
                public void done(final List<User> list, BmobException e) {
                    name = list.get(0).getUsername();
                    if (list.get(0).getAvatarUri()!=null)
                    Glide.with(context).load("http://"+list.get(0).getAvatarUri()).error(R.drawable.aurora_headicon_default).into(holder.itemhead);
                    JMessageClient.getUserInfo(list.get(0).getNickName(), new GetUserInfoCallback() {
                        @Override
                        public void gotResult(int i, String s, UserInfo userInfo) {
                            if (i == 0) {
                                holder.supervisor.setText(userInfo.getDisplayName());
                            } else {
                                holder.supervisor.setText(list.get(0).getNickName());
                            }
                        }
                    });
                }
            });
        }
        holder.targetContent.setText(targets.get(position).getTargetContent());
        if (targets.get(position).isSubmit()){
            holder.submitbtn.setText("等待审核");
            holder.submitbtn.setEnabled(false);
        }
        if (targets.get(position).isAudited()){
            if (targets.get(position).isAuditResult()){
                holder.submitbtn.setText("审核结果：通过");
                holder.submitbtn.setCompoundDrawables(null,null,context.getDrawable(R.drawable.ic_saily),null);
            }else {
                holder.submitbtn.setText("审核结果：未通过");
                holder.submitbtn.setCompoundDrawables(null,null,context.getDrawable(R.drawable.ic_no_pass),null);
            }
                holder.submitbtn.setEnabled(false);
        }
        holder.submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SubmitActivity.class);
                intent.putExtra("supervisor",name);
                intent.putExtra("objectid",targets.get(position).getObjectId());
                context.startActivity(intent);
            }
        });
        holder.targetimg.setVisibility(View.VISIBLE);
        Glide.with(context).load(UPYunUtils.getSourcePath(UPYunUtils.PATH_TARGETS, targets.get(position).getTargetImg(), UPYunUtils.JPG)).into(holder.targetimg);
        holder.endTime.setText(DateUtils.getNextDay());
    }

    @Override
    public int getItemCount() {
        return targets.size();
    }

}
