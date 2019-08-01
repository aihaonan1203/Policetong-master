package com.example.administrator.policetong.utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;


import com.example.administrator.policetong.base.App;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;


/**
 * Created by Administrator on 2017/11/20 0020.
 */

public class UIUtils {
    //----------------------------------------------------------------------------------------
    public static final String DATE_TYPE_11 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TYPE_12 = "yyyy-MM-dd";
    public static final String DATE_TYPE_13 = "MM-dd HH:mm";
    public static final String DATE_TYPE_21 = "yyyy年MM月dd日 HH:mm:ss";
    //给定时间 格式    ok
    public static String getDate(Timestamp tt, String dateType) {
        if(tt == null){
            return "0";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateType);
        return sdf.format(tt);
    }
    //给定格式    ok
    public static String getDate(String dateType) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateType);
        return sdf.format(new Date(System.currentTimeMillis()));
    }
    //返回当前时间戳字符串
    public static String getDate() {
        return System.currentTimeMillis()+"";
    }





//--------------------------------------------选择日期----------------------------------------------------------------------

    public static void showDatePickerDialog(Activity activity, final TextView tv, final Calendar calendar, final String[]time) {
        // Calendar 需要这样来得到

        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(activity,
                // 绑定监听器(How the parent is notified that the date is set.)
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // 此处得到选择的时间，可以进行你想要的操作
                        calendar.set(year, monthOfYear, dayOfMonth);
                        //日期是从零开始的
                        tv.setText(year + "-" + (monthOfYear+1)+ "-" + dayOfMonth);
                        time[0] = year + "-" + (monthOfYear+1) + "-" + dayOfMonth;
                    }
                }
                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    public static void showtTimePickerDialog(Activity activity, final TextView tv, final Calendar calendar, final String[]time) {
        // Calendar 需要这样来得到

        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(activity,
                // 绑定监听器(How the parent is notified that the date is set.)
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // 此处得到选择的时间，可以进行你想要的操作
                        calendar.set(year, monthOfYear, dayOfMonth);
                        //日期是从零开始的
                        tv.setText(year + "-" + (monthOfYear+1)+ "-" + dayOfMonth);
                        time[0] = year + "-" + (monthOfYear+1) + "-" + dayOfMonth;
                    }
                }
                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }







//----------------------------------地址-----------------------------------------------------

    public static String getAddress(String address, String fullAddress){
        if(TextUtils.isEmpty(address)){
            return fullAddress;
        }
        String[] city = address.split(" ");
        return city[1] + "市" + city[2] + fullAddress;
    }







    //因为swipeLayout 经常报空指针
    public static void setRefresh(boolean b, SwipeRefreshLayout swipeLayout) {
        if (swipeLayout != null) {
            swipeLayout.setRefreshing(b);
        }
    }

    public static void setCanRefresh(boolean b, SwipeRefreshLayout swipeLayout) {
        if (swipeLayout != null) {
            swipeLayout.setEnabled(b);
        }
    }

    /**
     * 判断activity是否存活  显示dialog
     */
    public static Boolean doDialog(Context context, Dialog dialog) {

        if (dialog == null) {
            return false;
        }
        if (context == null) {
            return false;
        }
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return false;
            }
        }
        dialog.show();
        return true;
    }



    public static Boolean closeDialog(Context context, Dialog dialog) {
        if (dialog == null) {
            return false;
        }
        if (context == null) {
            return false;
        }
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return false;
            }
        }
        if (dialog.isShowing()) {
            dialog.dismiss();

        }
        return true;
    }


    public static Context getContext() {
        return App.getApplication();
    }



    //返回指定colorId对应的颜色值
    public static int getColor(int colorId) {
        return getContext().getResources().getColor(colorId);
    }

    //加载指定viewId的视图对象，并返回
    public static View getView(int viewId) {
        View view = View.inflate(getContext(), viewId, null);
        return view;
    }

    public static String[] getStringArr(int strArrId) {
        String[] stringArray = getContext().getResources().getStringArray(strArrId);
        return stringArray;
    }


    //----------------------------------------分辨率转换---------------------------------------
    //将dp转化为px
    public static int dp2px(int dp) {
        //获取手机密度
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5);//实现四舍五入
    }




    public static int px2dp(int px) {
        //获取手机密度
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5);//实现四舍五入
    }




    public static int dip2px(float dpValue) {
//        final float scale = x.app().getResources().getDisplayMetrics().density;
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public static int dip2px(Context context, float dpValue) {
//        final float scale = x.app().getResources().getDisplayMetrics().density;
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2sp(float pxValue) {
        float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }







    //定制Toast 显示一个
    public static final int T_SUCCESS = 2;
    public static final int T_COMMON = 0;
    public static final int T_ERROR = 1;
    public static final int T_INFO = 3;
    public static final int T_WARNING = 4;
    private static Toast toast;
    public static void t(String message, boolean isLengthLong, int type) {
        int during;
        if (isLengthLong) {
            during = Toast.LENGTH_LONG;
        } else {
            during = Toast.LENGTH_SHORT;
        }
        if (toast != null) {
            toast.cancel();
        }
        switch (type) {
            case T_COMMON:
                toast = Toasty.normal(App.getApplication(), message, during);
                break;
            case T_ERROR:
                toast = Toasty.error(App.getApplication(), message, during);
                break;
            case T_SUCCESS:
                toast = Toasty.success(App.getApplication(), message, during);
                break;
            case T_INFO:
                toast = Toasty.info(App.getApplication(), message, during);
                break;
            case T_WARNING:
                toast = Toasty.warning(App.getApplication(), message, during);
                break;
        }
        assert toast != null;
        toast.show();
    }

}
