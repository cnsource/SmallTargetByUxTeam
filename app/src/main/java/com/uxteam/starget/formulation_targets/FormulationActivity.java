package com.uxteam.starget.formulation_targets;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.uxteam.starget.R;
import com.uxteam.starget.app_utils.CloudFuncationListener;
import com.uxteam.starget.app_utils.DateUtils;
import com.uxteam.starget.app_utils.MyBmobUtils;
import com.uxteam.starget.app_utils.UPYunUtils;
import com.uxteam.starget.app_utils.UpLoadResultListener;
import com.uxteam.starget.bmob_sys_pkg.Target;
import com.uxteam.starget.bmob_sys_pkg.User;
import com.uxteam.starget.login_registe.MyGlideEngine;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.callback.GetUserInfoListCallback;
import cn.jpush.im.android.api.model.UserInfo;

public class FormulationActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CHOOSE = 100;
    private ImageView backBtn;
    private Button reportBtn;
    private EditText target_content;
    private AppCompatSpinner spinner;
    private List<User> users = new ArrayList<>();
    private SuperListAdt superListAdt;
    private Button uploadImg;
    private File file;
    private String filename;
    private ImageView img;
    private RadioGroup radioGroup;
    private boolean isPublic =true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulation);
        bindView();
        bindViewEvent();
        loadSpinnerData();
    }

    private void bindView() {
        radioGroup = findViewById(R.id.radiogroup);
        img = findViewById(R.id.img);
        backBtn = findViewById(R.id.formulation_back);
        reportBtn = findViewById(R.id.target_report_btn);
        target_content = findViewById(R.id.target_text);
        spinner = findViewById(R.id.supervisor_seletor);
        uploadImg=findViewById(R.id.selectimg);
    }

    private void bindViewEvent() {
        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filename=(BmobUser.getCurrentUser(User.class).getObjectId())+new Date().getTime();
                Log.i("FileName",filename);
                startChoosePic();
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.radio0){
                    isPublic=true;
                }else {
                    isPublic=false;
                }
            }
        });
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
                    if (file!=null)
                        UPYunUtils.upLoadFile(file, UPYunUtils.getUPLoadPath(UPYunUtils.PATH_TARGETS, filename, UPYunUtils.JPG), new UpLoadResultListener() {
                            @Override
                            public void result(boolean isSuccess, String resultInfo,String resultPath) {
                                if (isSuccess){
                                    Toast.makeText(FormulationActivity.this, "文件上传成功", Toast.LENGTH_SHORT).show();
                                    if (file.exists())
                                        file.delete();
                                }else {
                                    Toast.makeText(FormulationActivity.this, "文件上传失败"+resultInfo, Toast.LENGTH_SHORT).show();
                                }
                            }
                        },FormulationActivity.this);
                    else
                        Toast.makeText(getApplicationContext(), "文件加载失败", Toast.LENGTH_SHORT).show();
                    Target target = new Target();
                    target.setTargetImg(filename);
                    target.setTargetState(false);
                    target.setPublic(isPublic);
                    target.setAudited(false);
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
                                        if (list != null) {
                                            User user = list.get(0);
                                            Map<String, String> map = new HashMap<String, String>();
                                            map.put("identity", MyBmobUtils.Identity_Publisher);
                                            map.put("objectId", user.getObjectId());
                                            map.put("todayTargets", (user.getTodayTargets() + 1) + "");
                                            map.put("targetNumbers", (user.getTargetNumbers() + 1) + "");
                                            MyBmobUtils.AccessBmobCloudFuncation(getApplicationContext(), " http://cloud.bmob.cn/c629a4dcb2dd21a8/update_user_key", map, new CloudFuncationListener() {
                                                @Override
                                                public void result(boolean result, String response) {
                                                    if (result) {
                                                        Toast.makeText(FormulationActivity.this, "更新用户数据成功", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(FormulationActivity.this, "更新用户数据失败", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
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
                                    MyBmobUtils.AccessBmobCloudFuncation(getApplicationContext(), " http://cloud.bmob.cn/c629a4dcb2dd21a8/update_user_key", map, new CloudFuncationListener() {
                                        @Override
                                        public void result(boolean result, String response) {
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

    private void startChoosePic() {
        Matisse.from(this)
                .choose(MimeType.ofImage())
                .countable(true)
                .maxSelectable(1)
                .gridExpectedSize(this.getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new MyGlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("ResultMethod","进入了");
        switch (requestCode) {
            case REQUEST_CODE_CHOOSE:
                if (data!=null){
                Uri uri=Matisse.obtainResult(data).get(0);
                startCut(uri);}
                break;
            case UCrop.REQUEST_CROP:
                img.setVisibility(View.VISIBLE);
                try {
                    Bitmap bitmap = Bitmap.createBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
                    img.setImageBitmap(bitmap);
                } catch (Exception e) {
                    Log.i("捕获异常", e.getMessage());
                }
                break;
            case UCrop.RESULT_ERROR:
                Throwable t = UCrop.getError(data);
                Log.i("UCropResult",t.toString());
                break;
        }
    }

    private void startCut(Uri uri) {
        file = new File(getCacheDir() + "/target.jpg");
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(100);
        options.setRootViewBackgroundColor(Color.WHITE);
        options.setToolbarTitle("裁剪图片");
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        UCrop.of(uri, Uri.fromFile(file))
                .withAspectRatio(1, 1)
                .withMaxResultSize(400, 1000)
                .withOptions(options)
                .start(this);
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
