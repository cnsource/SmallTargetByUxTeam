package com.uxteam.starget.plan_page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uxteam.starget.R;
import com.uxteam.starget.app_utils.DateUtils;
import com.uxteam.starget.bmob_sys_pkg.Target;
import com.uxteam.starget.bmob_sys_pkg.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;

public class PlanPageRecAdt extends RecyclerView.Adapter<PlanPageRecVH> {
    private Context context;
    private List<Target> targets;
    private int ViewSign;

    public PlanPageRecAdt(Context context, List<Target> targets) {
        this.context = context;
        this.targets = targets;
    }

    @NonNull
    @Override
    public PlanPageRecVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.plan_page_targetitem, parent, false);
        PlanPageRecVH pageRecVH = new PlanPageRecVH(view);
        return pageRecVH;
    }

    @Override
    public void onBindViewHolder(@NonNull final PlanPageRecVH holder, int position) {
        holder.startTime.setText(targets.get(position).getCreatedAt());
        if (ViewSign == 0) {
            holder.supervisor.setVisibility(View.VISIBLE);
            if (targets.get(position).getSupervisor() != null) {
                BmobQuery<User> userBmobQuery = new BmobQuery<>();
                userBmobQuery.addWhereEqualTo("objectId",targets.get(position).getSupervisor().getObjectId());
                userBmobQuery.findObjects(new FindListener<User>() {
                    @Override
                    public void done(final List<User> list, BmobException e) {
                        JMessageClient.getUserInfo(list.get(0).getNickName(), new GetUserInfoCallback() {
                            @Override
                            public void gotResult(int i, String s, UserInfo userInfo) {
                                if (i==0){
                                    holder.supervisor.setText("监督人："+userInfo.getDisplayName());
                                }else {
                                    holder.supervisor.setText("监督人："+list.get(0).getNickName());
                                }
                            }
                        });
                    }
                });
            }
        } else {
            holder.supervisor.setVisibility(View.GONE);
        }
        holder.targetContent.setText(targets.get(position).getTargetContent());
        holder.endTime.setText(DateUtils.getNextDay());
    }

    public void setViewSign(int ViewSign) {
        this.ViewSign = ViewSign;
    }

    @Override
    public int getItemCount() {
        return targets.size();
    }
}
