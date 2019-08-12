package com.example.administrator.policetong.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.administrator.policetong.base.App;
import com.example.administrator.policetong.bean.LocationBean;

import java.util.HashMap;
import java.util.Map;

public class LocationUtils {


    @SuppressLint("StaticFieldLeak")
    private static AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption = null;

    private static class LocationHolder {
        private static final LocationUtils INSTANCE = new LocationUtils();
    }

    public static LocationUtils getInstance() {
        return LocationHolder.INSTANCE;
    }

    public void startLocalService() {
        //初始化定位
        mLocationClient = new AMapLocationClient(App.getInstance());
        //设置定位回调监听
        mLocationOption = getDefaultOption();
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (null != aMapLocation) {
                    if (aMapLocation.getErrorCode() == 0) {
                        LocationBean locationBean = new LocationBean();
                        locationBean.setLongitude(aMapLocation.getLongitude());
                        locationBean.setLatitude(aMapLocation.getLatitude());
                        App.getInstance().setLocationBean(locationBean);
                        stopLocalService();
                    } else {
                        Log.e("location<<<failed", "定位失败\n错误码：" + aMapLocation.getErrorCode()
                                + "\n错误信息:" + aMapLocation.getErrorInfo()
                                + "\n错误描述:" + aMapLocation.getLocationDetail());
                    }
                } else {
                    UIUtils.t("定位失败",false,UIUtils.T_ERROR);
                }
            }
        });
        mLocationClient.startLocation();
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {

            }
        });
    }

    public void stopLocalService() {
        if (null != mLocationClient) {
            mLocationClient.onDestroy();
            mLocationClient.stopLocation();
            mLocationClient = null;
            mLocationOption = null;
        }
    }

    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(5000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        mOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        return mOption;
    }



}
