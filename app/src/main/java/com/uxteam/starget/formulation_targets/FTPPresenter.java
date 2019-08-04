package com.uxteam.starget.formulation_targets;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uxteam.starget.R;
import com.uxteam.starget.bmob_sys_pkg.Target;
import com.uxteam.starget.bmob_sys_pkg.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.callback.GetUserInfoListCallback;
import cn.jpush.im.android.api.model.UserInfo;

public class FTPPresenter implements FabBehaviorAnimator {
    private FormulationTargetsPage activity;
    private boolean fabState = true;
    private List<User> supervisorlist = new ArrayList<>();
    private List<Target> targets = new ArrayList<>();

    public FTPPresenter(FormulationTargetsPage activity) {
        this.activity = activity;
        initSupervisorList();
    }

    public FTPPresenter load() {
        activity.bindViewEvent(onClickListenerProvider(), adtProvider(), supervisorListAdtProvider(), this);
        activity.displayEdit(View.INVISIBLE);
        return this;
    }

    private View.OnClickListener onClickListenerProvider() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.ftp_add_fab:
                        addTargets(fabState);
                        activity.startFabAnimator(fabState);

                        break;
                    case R.id.ftp_cencle_fab:
                        activity.startFabAnimator(fabState);

                        break;
                    case R.id.ftp_report_btn:

                        break;
                }
            }
        };
    }

    private void addTargets(boolean fabState) {
        if (!fabState) {
            Target target = new Target();
            target.setPublisher(BmobUser.getCurrentUser(User.class));
            target.setTargetContent(activity.getTargetContent());
            target.setSupervisor(activity.getSupervisorSelected());
            //累了，歇会。。。。。。。。。
        }
    }

    private FTPRecAdt adtProvider() {
        return new FTPRecAdt(activity, targets);
    }

    private SuperListAdt supervisorListAdtProvider() {
        return new SuperListAdt(activity.getApplicationContext(), supervisorlist);
    }

    @Override
    public void fabout(FloatingActionButton add_fab, FloatingActionButton cencle_fab) {
        activity.displayEdit(View.VISIBLE);
        ObjectAnimator moveL = ObjectAnimator.ofFloat(add_fab, "translationX", 0f, 280f);
        ObjectAnimator rotaa = ObjectAnimator.ofFloat(add_fab, "rotation", 0f, 360f);
        ObjectAnimator rotab = ObjectAnimator.ofFloat(cencle_fab, "rotation", 0f, 360f);
        AnimatorSet animationSet = new AnimatorSet();
        animationSet.play(moveL).with(rotaa).with(rotab);
        animationSet.setInterpolator(new AccelerateInterpolator());
        animationSet.start();
        fabState = false;
    }

    private void initSupervisorList() {
        ContactManager.getFriendList(new GetUserInfoListCallback() {
            @Override
            public void gotResult(int responseCode, String responseMessage, List<UserInfo> userInfoList) {
                if (0 == responseCode) {
                    //获取好友列表成功
                    FindListener<User> findListener = new FindListener<User>() {
                        @Override
                        public void done(List<User> list, BmobException e) {
                            supervisorlist.addAll(list);
                        }
                    };
                    List<BmobQuery<User>> queries = new ArrayList<BmobQuery<User>>();
                    if (userInfoList.size() > 1) {
                        for (UserInfo user : userInfoList) {
                            BmobQuery<User> query = new BmobQuery<>();
                            query.addWhereEqualTo("username", user);
                            queries.add(query);
                        }
                    } else {
                        BmobQuery<User> query = new BmobQuery<>();
                        query.addWhereEqualTo("username", userInfoList.get(0));
                        query.findObjects(findListener);
                    }
                    BmobQuery<User> query = new BmobQuery<>();
                    query.or(queries);
                    query.findObjects(findListener);

                } else {
                    //获取好友列表失败
                    Toast.makeText(activity.getApplicationContext(), "好友列表获取失败", Toast.LENGTH_SHORT).show();
                    Log.e("GetFrendsListError", responseMessage);
                }
            }
        });
    }

    @Override
    public void fabreset(FloatingActionButton add_fab, FloatingActionButton cencle_fab) {
        activity.displayEdit(View.INVISIBLE);
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        ObjectAnimator moveR = ObjectAnimator.ofFloat(add_fab, "translationX", 280f, 0f);
        ObjectAnimator rotaa = ObjectAnimator.ofFloat(add_fab, "rotation", 0f, -360f);
        ObjectAnimator rotab = ObjectAnimator.ofFloat(cencle_fab, "rotation", 0f, -360f);
        AnimatorSet animationSet = new AnimatorSet();
        animationSet.play(rotaa).with(moveR).with(rotab);
        animationSet.setInterpolator(new AccelerateInterpolator());
        animationSet.start();
        fabState = true;
    }
}
