package com.example.administrator.policetong.base;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

public class App extends Application {
    private static App mContext;

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


    }
