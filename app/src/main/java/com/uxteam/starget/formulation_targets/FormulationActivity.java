package com.uxteam.starget.formulation_targets;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.uxteam.starget.R;
import com.uxteam.starget.app_utils.CloudFuncationListener;
import com.uxteam.starget.app_utils.DateUtils;
import com.uxteam.starget.app_utils.MyBmobUtils;
import com.uxteam.starget.bmob_sys_pkg.Target;
import com.uxteam.starget.bmob_sys_pkg.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.callback.GetUserInfoListCallback;
import cn.jpush.im.android.api.model.UserInfo;

public class FormulationActivity extends AppCompatActivity {

    private ImageView backBtn;
    private Button reportBtn;
    private EditText target_content;
    private AppCompatSpinner spinner;
    private List<User> users = new ArrayList<>();
    private SuperListAdt superListAdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulation);
        bindView();
        bindViewEvent();
        loadSpinnerData();
    }

    private void bindView() {
        backBtn = findViewById(R.id.formulation_back);
        reportBtn = findViewById(R.id.target_report_btn);
        target_content = findViewById(R.id.target_text);
        spinner = findViewById(R.id.supervisor_seletor);
    }

    private void bindViewEvent() {
        superListAdt = new SuperListAdt(this, users);
        spinner.setAdapter(superListAdt);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(target_content.getText().toString())) {
                    Toast.makeText(FormulationActivity.this, "请写下你今日的目标", Toast.LENGTH_SHORT).show();
                } else if (((User) spinner.getSelectedItem()).getTodaySupervision() >= 10) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FormulationActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("该伙伴今日监督目标数量已达上限！请重新选择");
                    builder.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    Target target = new Target();
                    target.setTargetState(false);
                    target.setSupervisor((User) spinner.getSelectedItem());
                    target.setPublisher(BmobUser.getCurrentUser(User.class));
                    target.setTargetContent(target_content.getText().toString());
                    target.setEndTime(DateUtils.getNextDay());
                    target.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                User localUser = BmobUser.getCurrentUser(User.class);
                                BmobQuery<User> userQuery = new BmobQuery<>();
                                userQuery.addWhereEqualTo("objectId", localUser.getObjectId());
                                userQuery.findObjects(new FindListener<User>() {
                                    @Override
                                    public void done(List<User> list, BmobException e) {
                                        User user=list.get(0);
                                        Map<String, String> map = new HashMap<String, String>();
                                        map.put("identity", MyBmobUtils.Identity_Publisher);
                                        map.put("objectId", user.getObjectId());
                                        map.put("todayTargets", (user.getTodayTargets() + 1) + "");
                                        map.put("targetNumbers", (user.getTargetNumbers() + 1) + "");
                                        MyBmobUtils.AccessBmobCloudFuncation(getApplicationContext(), " http://cloud.bmob.cn/65ceba774721fceb/update_user_key", map, new CloudFuncationListener() {
                                            @Override
                                            public void result(boolean result) {
                                                if (result) {
                                                    Toast.makeText(FormulationActivity.this, "更新用户数据成功", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(FormulationActivity.this, "更新用户数据失败", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                });
                            String id = ((User) spinner.getSelectedItem()).getObjectId();
                            BmobQuery<User> userBmobQuery = new BmobQuery<>();
                            userBmobQuery.addWhereEqualTo("objectId", id);
                            userBmobQuery.findObjects(new FindListener<User>() {
                                @Override
                                public void done(List<User> list, BmobException e) {
                                    User user = list.get(0);
                                    Map<String, String> map = new HashMap<String, String>();
                                    map.put("identity", MyBmobUtils.Identity_Supervisor);
                                    map.put("objectId", user.getObjectId());
                                    map.put("todaySupervision", (user.getTodaySupervision() + 1) + "");
                                    MyBmobUtils.AccessBmobCloudFuncation(getApplicationContext(), " http://cloud.bmob.cn/65ceba774721fceb/update_user_key", map, new CloudFuncationListener() {
                                        @Override
                                        public void result(boolean result) {
                                            if (result) {
                                                loadSpinnerData();
                                                Toast.makeText(FormulationActivity.this, "更新用户数据成功", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(FormulationActivity.this, "更新用户数据失败", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                        }else

                        {
                            Toast.makeText(FormulationActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
                            Log.e("Formulation", "发布失败" + e.getMessage());
                        }
                    }
                });
            }
        }
    });
}

    private void loadSpinnerData() {
        ContactManager.getFriendList(new GetUserInfoListCallback() {
            @Override
            public void gotResult(int responseCode, String responseMessage, List<UserInfo> userInfoList) {
                if (0 == responseCode) {
                    //获取好友列表成功
                    List<BmobQuery<User>> queries = new ArrayList<>();
                    for (int i = 0; i < userInfoList.size(); i++) {
                        BmobQuery<User> query = new BmobQuery<>();
                        query.addWhereEqualTo("username", userInfoList.get(i).getUserName());
                        queries.add(query);
                    }
                    BmobQuery<User> queryUser = new BmobQuery<>();
                    queryUser.or(queries);
                    queryUser.findObjects(new FindListener<User>() {
                        @Override
                        public void done(List<User> list, BmobException e) {
                            if (e == null) {
                                users.clear();
                                users.addAll(list);
                                superListAdt.notifyDataSetChanged();
                                Log.i("监督人列表", "" + users.size());
                            } else {
                                Log.e("FormulationMoudle", "获取好友列表失败");
                            }
                        }
                    });

                } else {
                    //获取好友列表失败
                    Log.e("获取用户列表失败", responseMessage);
                }
            }
        });
    }
}
