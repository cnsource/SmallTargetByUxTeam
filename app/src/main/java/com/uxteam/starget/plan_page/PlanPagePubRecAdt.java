package com.uxteam.starget.plan_page;

import android.content.Context;
import android.content.Intent;
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
    public void onBindViewHolder(@NonNull final PlanPagePubRecVH holder, int position) {

        holder.startTime.setText(targets.get(position).getCreatedAt());
        holder.supervisor.setVisibility(View.VISIBLE);
        if (targets.get(position).getSupervisor() != null) {
            BmobQuery<User> userBmobQuery = new BmobQuery<>();
            userBmobQuery.addWhereEqualTo("objectId", targets.get(position).getSupervisor().getObjectId());
            userBmobQuery.findObjects(new FindListener<User>() {
                @Override
                public void done(final List<User> list, BmobException e) {
                    JMessageClient.getUserInfo(list.get(0).getNickName(), new GetUserInfoCallback() {
                        @Override
                        public void gotResult(int i, String s, UserInfo userInfo) {
                            if (i == 0) {
                                name = list.get(0).getUsername();
                                holder.supervisor.setText("监督人：" + userInfo.getDisplayName());
                            } else {
                                holder.supervisor.setText("监督人：" + list.get(0).getNickName());
                            }
                        }
                    });
                }
            });
        }
        Glide.with(context).load(UPYunUtils.getSourcePath(UPYunUtils.PATH_HEAD, BmobUser.getCurrentUser(User.class).getUsername(), UPYunUtils.JPG)).error(R.drawable.aurora_headicon_default).into(holder.itemhead);
        holder.targetContent.setText(targets.get(position).getTargetContent());
        holder.submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SubmitActivity.class);
                intent.putExtra("supervisor",name);
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
