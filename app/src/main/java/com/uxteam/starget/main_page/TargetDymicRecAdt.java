package com.uxteam.starget.main_page;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.uxteam.starget.R;
import com.uxteam.starget.app_utils.UPYunUtils;
import com.uxteam.starget.app_utils.UserUtils;
import com.uxteam.starget.bmob_sys_pkg.Target;
import com.uxteam.starget.bmob_sys_pkg.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;

public class TargetDymicRecAdt extends RecyclerView.Adapter<TargetDymicRecVH> {
    private Context context;
    private List<Target> targets;

    public TargetDymicRecAdt(Context context, List<Target> targets) {
        this.context = context;
        this.targets = targets;
    }
    @NonNull
    @Override
    public TargetDymicRecVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.target_dymic,parent,false);
        TargetDymicRecVH vh=new TargetDymicRecVH(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final TargetDymicRecVH holder, final int position) {
        BmobQuery<User> query=new BmobQuery<>();
        query.addWhereEqualTo("objectId",targets.get(position).getPublisher().getObjectId());
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(final List<User> list, BmobException e) {
                if (list!=null&&e==null) {
                    JMessageClient.getUserInfo(list.get(0).getUsername(), new GetUserInfoCallback() {
                        @Override
                        public void gotResult(int i, String s, UserInfo userInfo) {
                            if (i == 0) {
                                holder.name.setText(userInfo.getDisplayName());
                                Log.i("User:",list.get(0).toString());
                            } else {
                                holder.name.setText(targets.get(position).getPublisher().getUsername());
                            }
                        }
                    });
                    Glide.with(context).load("http://"+list.get(0).getAvatarUri()).error(R.drawable.aurora_headicon_default).into(holder.head);
                }
            }
        });

        holder.ctext.setText(targets.get(position).getTargetContent());
        if(targets.get(position).getTargetImg()!=null)
            Glide.with(context).load(UPYunUtils.getSourcePath(UPYunUtils.PATH_TARGETS, targets.get(position).getTargetImg(), UPYunUtils.JPG)).into(holder.img1);
        if (targets.get(position).isSubmit()){
            Glide.with(context).load(UPYunUtils.getSourcePath(targets.get(position).getSubmitImgPath())).into(holder.img2);
        }else{
            holder.img2.setVisibility(View.INVISIBLE);
        }
        holder.upDateTime.setText(targets.get(position).getUpdatedAt().substring(0,16));
    }

    @Override
    public int getItemCount() {
        return targets.size();
    }
}
