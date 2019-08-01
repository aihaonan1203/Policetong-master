package com.example.administrator.policetong.base;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

import com.alibaba.fastjson.JSON;
import com.example.administrator.policetong.bean.new_bean.UserInfo;
import com.example.administrator.policetong.utils.GsonUtil;

public class App extends Application {
    private static App mContext;

    public static UserInfo userInfo;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
    }

    public static App getApplication() {
        return mContext;
    }


    public static void initUser(String response) {
        userInfo = JSON.parseObject(response, UserInfo.class);
    }
}
