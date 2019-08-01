package com.example.administrator.policetong.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.policetong.MainActivity;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.base.App;
import com.example.administrator.policetong.base.BaseActivity;
import com.example.administrator.policetong.base.CommonThrowable;
import com.example.administrator.policetong.base.VerifyConsumer;
import com.example.administrator.policetong.bean.new_bean.UserInfo;
import com.example.administrator.policetong.network.Network;
import com.example.administrator.policetong.utils.GsonUtil;
import com.example.administrator.policetong.utils.RequestBodyUtils;
import com.example.administrator.policetong.utils.SPUtils;
import com.master.permissionhelper.BuildConfig;
import com.master.permissionhelper.PermissionHelper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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

    @Override
    protected void onStart() {
        super.onStart();
        if (SPUtils.getBoolean("isLogin", false)) {
            userlogin(SPUtils.getString("userId", "1111"), SPUtils.getString("password", "11111"));
        }
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


    private void initView() {
        userid =  findViewById(R.id.userid);
        password =  findViewById(R.id.password);
        password.setOnClickListener(this);
        btn_login =  findViewById(R.id.btn_login);
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
        Map<String, String> input = new HashMap<>();
        input.put("user", name);
        input.put("password", pwd);
        JSONObject jsonObject = new JSONObject(input);
        disposable = Network.getPoliceApi(true,this).login(RequestBodyUtils.getRequestBody(jsonObject))
                .compose(BaseActivity.<ResponseBody>applySchedulers())
                .subscribe(new VerifyConsumer() {
                    @Override
                    public void result(String result) {
                        App.initUser(result);
                        SPUtils.saveBoolean("isLogin", true);
                        SPUtils.saveString("userId", name);
                        SPUtils.saveString("password", pwd);
                        finish();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                }, new CommonThrowable() {
                    @Override
                    public void onThrowable(Throwable throwable) {
                        SPUtils.saveBoolean("isLogin", false);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
