package com.example.administrator.policetong.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.administrator.policetong.MainActivity;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.base.BaseActivity;
import com.example.administrator.policetong.base.BaseBean;
import com.example.administrator.policetong.httppost.getNetInfo;
import com.example.administrator.policetong.network.Network;
import com.example.administrator.policetong.new_bean.UserBean;
import com.example.administrator.policetong.utils.LoadingDialog;
import com.example.administrator.policetong.utils.OtherDialog;
import com.example.administrator.policetong.utils.SPUtils;
import com.master.permissionhelper.BuildConfig;
import com.master.permissionhelper.PermissionHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 登录
     */
    private EditText userid;
    private EditText password;
    private Button btn_login;
    private PermissionHelper permissionHelper;

    @SuppressLint("ObsoleteSdkInt")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SPUtils.getBoolean("save", false)) {
            userlogin(SPUtils.getString("userid", ""), SPUtils.getString("password", ""));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_login);
        initView();
        getpermission();
    }


    private void getpermission() {
        permissionHelper = new PermissionHelper(this, new String[]{
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        }, 100);
        permissionHelper.request(new PermissionHelper.PermissionCallback() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onIndividualPermissionGranted(String[] grantedPermission) {
                getpermission();
            }

            @Override
            public void onPermissionDenied() {
                getpermission();
            }

            @Override
            public void onPermissionDeniedBySystem() {
                showMissingPermissionDialog("相机,定位,存储");
            }
        });
    }

    public void showMissingPermissionDialog(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        final AlertDialog alertDialog = builder.create();
        builder.setMessage("当前应用缺少-" + s + "-权限。\n\n请点击\"设置\"-\"权限\"-打开所需权限。\n\n最后点击两次后退按钮，即可返回。");
        // 拒绝, 退出应用
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
                finish();
            }
        });
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                startActivity(intent);
            }
        });
        builder.setCancelable(false);
        builder.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionHelper != null) {
            permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 9;
    private static String[] PERMISSIONS_STORAGE = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};

    public void verifyStoragePermissions(Activity activity) {
        try { //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        userid = (EditText) findViewById(R.id.userid);
        password = (EditText) findViewById(R.id.password);
        password.setOnClickListener(this);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
    }


    long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000)  //System.currentTimeMillis()无论何时调用，肯定大于2000
            {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                final String username = userid.getText().toString().trim();
                String userpwd = password.getText().toString().trim();
                if (username.isEmpty()) {
                    Toast.makeText(this, "请输入你的账号！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (userpwd.isEmpty()) {
                    Toast.makeText(this, "请输入你的密码！", Toast.LENGTH_SHORT).show();
                    return;
                }
                userlogin(username, userpwd);
                break;
        }
    }

    private void userlogin(final String name, final String pwd) {
//        LoadingDialog.showDialog(this);
//        LoadingDialog.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialogInterface) {
//                WindowManager.LayoutParams lp = getWindow().getAttributes();
//                lp.alpha = 1.0f;
//                getWindow().setAttributes(lp);
//                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//            }
//        });
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.alpha = 0.5f;
//        getWindow().setAttributes(lp);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        Map<String, String> input = new HashMap<String, String>();
        input.put("user", name);
        input.put("password", pwd);
        JSONObject jsonObject = new JSONObject(input);
        disposable = Network.getPoliceApi().login(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString()))
                .compose(BaseActivity.<ResponseBody>applySchedulers())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        String name=responseBody.string();
                        Log.e("accept: ",name );
//                        LoadingDialog.disDialog();
//                        if (userBean.getCode()!=0){
//                            Toast.makeText(LoginActivity.this, "账号或密码错误!", Toast.LENGTH_SHORT).show();
//                            SPUtils.saveBoolean("save", false);
//                            return;
//                        }
//                        SPUtils.setUserInfo(LoginActivity.this, userBean.getData());
//                        SPUtils.saveBoolean("save", true);
//                        SPUtils.saveString("userid", name);
//                        SPUtils.saveString("password", pwd);
//                        finish();
//                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                        startActivity(intent);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(LoginActivity.this, "登陆失败，请检查网络是否连接!", Toast.LENGTH_SHORT).show();
                        LoadingDialog.disDialog();
                        SPUtils.saveBoolean("save", false);
                    }
                });

//        getNetInfo.NetInfo(LoginActivity.this, "loginservlet", new JSONObject(input), new getNetInfo.VolleyCallback() {
//            @Override
//            public void onSuccess(JSONObject object) throws JSONException {
//                Log.e("onSuccess: ",object.toString() );
//                if (object.getString("ERRMSG").equals("成功")){
//                    SharedPreferences.Editor editor=sharedPreferences.edit();
//                    editor.putString("userid",name).commit();
//                    editor.putString("userpwd",pwd).commit();
//                    editor.putString("group",object.getString("group")).commit();
//                    editor.putString("detachment",object.getString("detachment")).commit();
//                    getUserInfo(name);
//                }else {
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            LoadingDialog.disDialog();
//                            Toast.makeText(LoginActivity.this, "用户名或密码错误！", Toast.LENGTH_SHORT).show();
//                        }
//                    },1000);
//                }
//            }
//            @Override
//            public void onError(VolleyError volleyError) {
//                Log.e("onError: ","========" );
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(LoginActivity.this, "登陆失败，请检查网络是否连接!", Toast.LENGTH_SHORT).show();
//                        LoadingDialog.disDialog();
//                        sharedPreferences.edit().putBoolean("save", false).apply();
//                    }
//                },1500);
//            }
//        });
    }

    private void getUserInfo(String id) {
        Map input = new HashMap();
        input.put("userid", id);
        getNetInfo.NetInfo(this, "selectuserinfo", new JSONObject(input), new getNetInfo.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject object) throws JSONException {
                Log.e("onSuccess: ", object.toString());
                LoadingDialog.disDialog();
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString("username", object.getString("username")).apply();
//                editor.putBoolean("save", true).commit();
//                editor.putString("sex", object.getString("usersex")).commit();
                finish();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onError(VolleyError volleyError) {

            }
        });
    }

}
