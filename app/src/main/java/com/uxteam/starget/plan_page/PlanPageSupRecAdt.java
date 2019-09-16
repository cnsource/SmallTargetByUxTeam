package com.uxteam.starget.plan_page;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.uxteam.starget.R;
import com.uxteam.starget.app_utils.UPYunUtils;
import com.uxteam.starget.bmob_sys_pkg.Target;
import com.uxteam.starget.bmob_sys_pkg.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;

public class PlanPageSupRecAdt extends RecyclerView.Adapter<PlanPageSupRecVH> {
    private Context context;
    private List<Target> targets;

    public PlanPageSupRecAdt(Context context, List<Target> targets) {
        this.context = context;
        this.targets = targets;
    }


    @NonNull
    @Override
    public PlanPageSupRecVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.plan_page_super_targetitem, parent, false);
        PlanPageSupRecVH vh = new PlanPageSupRecVH(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final PlanPageSupRecVH holder, final int position) {
        BmobQuery<User> userBmobQuery = new BmobQuery<>();
        userBmobQuery.addWhereEqualTo("objectId", targets.get(position).getPublisher().getObjectId());
        userBmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(final List<User> list, BmobException e) {
                if (list != null) {
                    final User user = list.get(0);
                    Glide.with(context).load(UPYunUtils.getSourcePath("head", user.getUsername(), UPYunUtils.JPG)).into(holder.head);
                    JMessageClient.getUserInfo(user.getUsername(), new GetUserInfoCallback() {
                        @Override
                        public void gotResult(int i, String s, UserInfo userInfo) {
                            if (i == 0) {
                                holder.name.setText(userInfo.getDisplayName());
                            } else {
                                holder.name.setText(user.getUsername());
                            }
                        }
                    });
                }

            }
        });

        holder.content.setText(targets.get(position).getTargetContent());
        holder.deninebtn.setVisibility(View.INVISIBLE);
        holder.passbtn.setVisibility(View.INVISIBLE);
        if (targets.get(position).isSubmit()&&!targets.get(position).isAudited()) {
            holder.checkbtn.setEnabled(true);
            holder.checkbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context,AuditActivity.class);
                    intent.putExtra("audit-img",targets.get(position).getSubmitImgPath());
                    intent.putExtra("audit-content",targets.get(position).getSubmitText());
                    intent.putExtra("objectId",targets.get(position).getObjectId());
                    context.startActivity(intent);
                }
            });
            holder.checkbtn.setText("待审核");
        }else if (targets.get(position).isAudited()){
            holder.checkbtn.setEnabled(false);
            holder.checkbtn.setText("已审核");
        }else {
            holder.checkbtn.setText("待提交");
        }
    }

    @Override
    public int getItemCount() {
        return targets.size();
    }
}
