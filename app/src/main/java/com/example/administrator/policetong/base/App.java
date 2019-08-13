package com.example.administrator.policetong.base;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.example.administrator.policetong.bean.LocationBean;
import com.example.administrator.policetong.bean.new_bean.UserInfo;
import com.example.administrator.policetong.utils.CrashHandler;
import com.example.administrator.policetong.utils.GsonUtil;
import com.example.administrator.policetong.utils.LocationUtils;
import com.example.administrator.policetong.utils.SPUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class App extends Application {
    private static App mContext;

    public static UserInfo userInfo=new UserInfo();
    private LocationBean locationBean;

    public static App getInstance() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        CrashHandler.getInstance().init();
    }

    public static App getApplication() {
        return mContext;
    }


    public static void initUser(String response) {
        userInfo = JSON.parseObject(response, UserInfo.class);
        if (userInfo.getToken()!=null&&!userInfo.getToken().isEmpty()){
            SPUtils.setUserToken(App.getApplication(),userInfo.getToken());
        }
    }

    public LocationBean getLocationBean() {
        return locationBean;
    }

    public void setLocationBean(LocationBean locationBean) {
        this.locationBean = locationBean;
    }

}
