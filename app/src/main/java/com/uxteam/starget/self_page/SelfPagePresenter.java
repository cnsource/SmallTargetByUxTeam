package com.uxteam.starget.self_page;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.uxteam.starget.R;
import com.uxteam.starget.app_utils.AppVersion;
import com.uxteam.starget.app_utils.CloudFuncationListener;
import com.uxteam.starget.app_utils.MyBmobUtils;
import com.uxteam.starget.bmob_sys_pkg.User;
import com.uxteam.starget.im_sys.MyFrends;
import com.uxteam.starget.im_sys.SelfInfoEditerCard;
import com.uxteam.starget.login_registe.LoginPageActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FetchUserInfoListener;
import cn.bmob.v3.listener.FindListener;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import cn.jpush.im.android.api.model.Message;

public class SelfPagePresenter {
    private SelfPage selfPage;
    private List<SelfPageRecItem> items=new ArrayList<>();
    public SelfPagePresenter(SelfPage selfpage) {
        this.selfPage = selfpage;
    }
    public void load(){
        EventBus.getDefault().register(this);
        selfPage.bindViewInfo(
                getUserName(),
                getRecItem(),
                loginOut(), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selfPage.startActivity(new Intent(selfPage.getContext(), SelfInfoEditerCard.class));
                    }
                }
        );
        fetchUserInfo(selfPage.getView());
        initdata();
        selfPage.refreshView();
    }
    private View.OnClickListener loginOut(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BmobUser.logOut();
                JMessageClient.logout();
                selfPage.startActivity(new Intent(selfPage.getContext(), LoginPageActivity.class));
                selfPage.closePage();
                Toast.makeText(selfPage.getContext(), "退出登录成功", Toast.LENGTH_SHORT).show();

            }
        };
    }
    private void fetchUserInfo(final View view) {
        BmobUser.fetchUserInfo(new FetchUserInfoListener<BmobUser>() {
            @Override
            public void done(BmobUser user, BmobException e) {
                if (e == null) {
                    getInfo();
                } else {
                    Toast.makeText(selfPage.getContext(), "同步数据失败，请检查网络情况", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private SelfPageRecAdt getRecItem() {
        initdata();
        SelfPageRecAdt selfPageRecAdt = new SelfPageRecAdt(selfPage.getContext(),items);
        selfPageRecAdt.setItemClickListener(itemClickListenerProvider());
        return selfPageRecAdt;
    }

    public void initdata() {
        items.clear();
        items.add(new SelfPageRecItem(R.drawable.ic_frends,"朋友列表",JMessageClient.getAllUnReadMsgCount()));
        items.add(new SelfPageRecItem(R.drawable.ic_history,"历史目标",-1));
        items.add(new SelfPageRecItem(R.drawable.ic_version,"版本更新",-1));
        items.add(new SelfPageRecItem(R.drawable.ic_userprot,"使用协议",-1));
    }

    private String getUserName(){
        if (TextUtils.isEmpty(BmobUser.getCurrentUser(User.class).getNickName())){
            return "设置昵称";
        }
        return BmobUser.getCurrentUser(User.class).getNickName();
    }
    private void getInfo(){
        Map<String,String> map=new HashMap<>();
        map.put("objectId",BmobUser.getCurrentUser(User.class).getObjectId()+"");
        MyBmobUtils.AccessBmobCloudFuncation(selfPage.getContext(), "http://cloud.bmob.cn/c629a4dcb2dd21a8/self_page_info", map, new CloudFuncationListener() {
            @Override
            public void result(boolean result, String response) {
                String btm=BmobUser.getCurrentUser(User.class).getTodayTargets()+"/"+BmobUser.getCurrentUser(User.class).getTodaySupervision();
                if (result){
                    String str[]=response.split(",");
                    String pub=str[0];
                    String sup=str[1];
                    selfPage.showInfo(pub,sup,btm);
                   // Toast.makeText(selfPage.getContext(), "CloudFunctionSuccessed!:"+response, Toast.LENGTH_SHORT).show();
                }else{
                   // Toast.makeText(selfPage.getContext(), "CloudFunctionError!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private OnItemClickListener itemClickListenerProvider(){
        return new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                switch (position){
                    case 0:
                        selfPage.startActivity(new Intent(selfPage.getContext(), MyFrends.class));
                        break;
                        case 1:
                            selfPage.startActivity(new Intent(selfPage.getContext(),HistoryActivity.class));
                            break;
                    case 2:
                            BmobQuery<AppVersion> query=new BmobQuery<>();
                            query.addWhereEqualTo("objectId","cmf1BBBZ");
                            query.findObjects(new FindListener<AppVersion>() {
                                @Override
                                public void done(List<AppVersion> list, BmobException e) {
                                    if (e==null&&list!=null){
                                        File file=new File(selfPage.getContext().getCacheDir()+File.separator+list.get(0).getVersionCode()+".txt");
                                        if(!file.exists()){
                                            Log.i("self测试","jinlai");
                                            String s=file.getName().substring(0,1);
                                            Log.i("self测试",s);
                                            Log.i("self测试",list.get(0).getVersionUrl());
                                            startIn(list.get(0).getVersionUrl());
                                           }else{
                                               Toast.makeText(selfPage.getContext(), "已经是最新版本", Toast.LENGTH_SHORT).show();
                                           }
                                            //abcd.txt

                                    }
                                }
                            });

                        break;
                    case 3:
                        BmobQuery<AppVersion> query2=new BmobQuery<>();
                        query2.addWhereEqualTo("objectId","cmf1BBBZ");
                        query2.findObjects(new FindListener<AppVersion>() {
                            @Override
                            public void done(List<AppVersion> list, BmobException e) {
                                if (e==null&&list!=null){
                                    startIn(list.get(0).getUseAgreement());
                                }
                            }
                        });
                        break;
                }
            }
        };
    }

    private void startIn(String url){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        selfPage.startActivity(intent);
    }
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void messageEvent(MessageEvent messageEvent) {
       initdata();
       selfPage.refreshView();

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void messageEvent(OfflineMessageEvent offlineMessageEvent) {
            initdata();
            selfPage.refreshView();

        }
public void unregiste(){
        EventBus.getDefault().unregister(this);
}
    }
