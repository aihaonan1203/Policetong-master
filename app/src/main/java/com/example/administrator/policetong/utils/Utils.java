package com.example.administrator.policetong.utils;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.policetong.R;
import com.luck.picture.lib.tools.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * <p>Utils初始化相关 </p>
 */
public class Utils {

    private static Context context;

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        Utils.context = context.getApplicationContext();
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("u should init first");
    }

    public static long getStringToDate(String dateString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

    private static SimpleDateFormat sdf = null;

    public static String formatUTC(long l, String strPattern) {
        if (TextUtils.isEmpty(strPattern)) {
            strPattern = "yyyy-MM-dd HH:mm:ss";
        }
        if (sdf == null) {
            try {
                sdf = new SimpleDateFormat(strPattern, Locale.CHINA);
            } catch (Throwable e) {
            }
        } else {
            sdf.applyPattern(strPattern);
        }
        return sdf == null ? "NULL" : sdf.format(l);
    }

    public static String stampToDate(long timeMillis) {
        if (timeMillis==0){
            return "----";
        }
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(timeMillis * 1000);
        return simpleDateFormat.format(date);
    }

    /**
     * View获取Activity的工具
     *
     * @param view view
     * @return Activity
     */
    public static
    @NonNull
    Activity getActivity(View view) {
        Context context = view.getContext();

        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }

        throw new IllegalStateException("View " + view + " is not attached to an Activity");
    }

    /**
     * 全局获取String的方法
     *
     * @param id 资源Id
     * @return String
     */
    public static String getString(@StringRes int id) {
        return context.getResources().getString(id);
    }


    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     */
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }


    public static <T> T checkNotNull(T obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return obj;
    }


    public static String stampToDate(long timeMillis, String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(time);
        Date date = new Date(timeMillis * 1000);
        return simpleDateFormat.format(date);
    }

    public static void setTextResult(int textStatus, TextView textView) {
        switch (textStatus) {
            case 0:
                textView.setText("未审核");
                textView.setBackgroundResource(R.drawable.button_all_maincolor_r5_selector);
                break;
            case 1:
                textView.setText("通过");
                textView.setBackgroundResource(R.drawable.button_all_maincolor_r5_selector_green);
                break;
            case 2:
                textView.setText("否决");
                textView.setBackgroundResource(R.drawable.button_all_maincolor_r5_selector_red);
                break;
        }
    }

    public static void setTextStatus(int textStatus, TextView textView) {
        switch (textStatus) {

            case 0:
                textView.setText("未指派");
                textView.setBackgroundResource(R.drawable.button_all_maincolor_r5_selector_org);
                break;
            case 1:
                textView.setText("中队审核中");
                textView.setBackgroundResource(R.drawable.button_all_maincolor_r5_selector);
                break;
            case 2:
                textView.setText("大队复审");
                textView.setBackgroundResource(R.drawable.button_all_maincolor_r5_selector);
                break;
            case 3:
                textView.setText("审核完毕");
                textView.setBackgroundResource(R.drawable.button_all_maincolor_r5_selector_green);
                break;
        }
    }

    public static String getTextStatus(int textStatus) {
        switch (textStatus) {
            case 0:
                return ("未审核");
            case 1:
                return ("通过");
            case 2:
                return ("否决");
        }
        return "";
    }
}