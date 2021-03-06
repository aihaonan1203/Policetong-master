package com.example.administrator.policetong.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationQualityReport;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.fragment.DailyService;
import com.example.administrator.policetong.fragment.ParkManageFragment;
import com.example.administrator.policetong.fragment.PathParameter;
import com.example.administrator.policetong.fragment.PoliceFragment;
import com.example.administrator.policetong.fragment.SafetyChecks;
import com.example.administrator.policetong.fragment.ShiGuFragment;
import com.example.administrator.policetong.fragment.StudyFragment;
import com.example.administrator.policetong.fragment.VisitRectification;
import com.example.administrator.policetong.utils.Utils;
import com.luck.picture.lib.entity.LocalMedia;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModulesActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ac_arrow_back;
    private TextView ac_tv_title;
    public List<LocalMedia> selectList;
    public JSONObject j;
    public int id;

    public static ModulesActivity getmContext() {
        return mContext;
    }

    public static void setmContext(ModulesActivity mContext) {
        ModulesActivity.mContext = mContext;
    }

    private static ModulesActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        initLocation();
        try {
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
            setContentView(R.layout.activity_modules);
            initView();
            Bundle bundle = getIntent().getBundleExtra("data");
            id = bundle.getInt("id");
            switch (id) {
                case 1:
//                    getSupportFragmentManager().beginTransaction().replace(R.id.ac_context, new Email()).commit();
//                    ac_tv_title.setText("邮件收发");
                    break;
                case 11:
                case 2:
                    getSupportFragmentManager().beginTransaction().replace(R.id.ac_context, new PoliceFragment()).commit();
                    ac_tv_title.setText("警保台账");
                    break;
                case 3:
                    getSupportFragmentManager().beginTransaction().replace(R.id.ac_context, new SafetyChecks(), "SafetyChecks").commit();
                    ac_tv_title.setText("安全排查");
                    break;
                case 4:
                    getSupportFragmentManager().beginTransaction().replace(R.id.ac_context, new VisitRectification()).commit();
                    ac_tv_title.setText("走访整改");
                    break;
                case 5:
                    getSupportFragmentManager().beginTransaction().replace(R.id.ac_context, new DailyService()).commit();
                    ac_tv_title.setText("日常勤务");
                    break;
                case 6:
                    getSupportFragmentManager().beginTransaction().replace(R.id.ac_context, new PathParameter()).commit();
                    ac_tv_title.setText("道路台账");
                    break;
//                case 7:
//                    getSupportFragmentManager().beginTransaction().replace(R.id.ac_context, new Fragment_manage()).commit();
//                    ac_tv_title.setText("管理/查看我的提交");
//                    break;
                case 8:
                    getSupportFragmentManager().beginTransaction().replace(R.id.ac_context, new StudyFragment()).commit();
                    ac_tv_title.setText("学习台账");
                    break;
                case 12:
                case 9:
                    getSupportFragmentManager().beginTransaction().replace(R.id.ac_context, new ShiGuFragment()).commit();
                    ac_tv_title.setText("事故接警");
                    break;
                case 10:
                    getSupportFragmentManager().beginTransaction().replace(R.id.ac_context, new ParkManageFragment()).commit();
                    ac_tv_title.setText("停车场管理");
                    break;
            }
        } catch (Exception e) {

        }
    }


    private void initView() {
        ac_arrow_back = (ImageView) findViewById(R.id.ac_arrow_back);
        ac_arrow_back.setOnClickListener(this);
        ac_tv_title = (TextView) findViewById(R.id.ac_tv_title);
    }

    @Override
    public void onClick(View view) {
        finish();
    }

    public void shilihua(Myjiekou myjiekou) {
        this.myjiekou = myjiekou;
    }

    public interface Myjiekou {
        void callback(String ooo);
    }

    Myjiekou myjiekou;


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//// TODO Auto-generated method stub
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.e("onActivityResult: ",resultCode+"    "+(resultCode == RESULT_OK) );
//        if ( resultCode == RESULT_OK) {
//            myjiekou.callback("拍照");
//        }else {
//            Toast.makeText(this, "已取消本次上传！", Toast.LENGTH_SHORT).show();
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;


    private void initLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    301);//自定义的code
        }
        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
        locationClient.startLocation();

    }

    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {
                StringBuffer sb = new StringBuffer();
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if (location.getErrorCode() == 0) {
                    sb.append("定位成功" + "\n");
                    sb.append("定位类型: " + location.getLocationType() + "\n");
                    sb.append("经    度    : " + location.getLongitude() + "\n");
                    sb.append("纬    度    : " + location.getLatitude() + "\n");
                    sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
                    sb.append("提供者    : " + location.getProvider() + "\n");

                    sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
                    sb.append("角    度    : " + location.getBearing() + "\n");
                    // 获取当前提供定位服务的卫星个数
                    sb.append("星    数    : " + location.getSatellites() + "\n");
                    sb.append("国    家    : " + location.getCountry() + "\n");
                    sb.append("省            : " + location.getProvince() + "\n");
                    sb.append("市            : " + location.getCity() + "\n");
                    sb.append("城市编码 : " + location.getCityCode() + "\n");
                    sb.append("区            : " + location.getDistrict() + "\n");
                    sb.append("区域 码   : " + location.getAdCode() + "\n");
                    sb.append("地    址    : " + location.getAddress() + "\n");
                    sb.append("兴趣点    : " + location.getPoiName() + "\n");
                    //定位完成的时间
                    sb.append("定位时间: " + Utils.formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss") + "\n");

                    Map<String,String> map=new HashMap<>();
                    map.put("longitude",location.getLongitude()+"");
                    map.put("latitude",location.getLatitude()+"");
                    map.put("LocationType",location.getLocationType()+"");
                    map.put("Province",location.getProvince()+"");
                    map.put("City",location.getCity()+"");
                    map.put("District",location.getDistrict()+"");
                    map.put("Address",location.getAddress()+"");
                    map.put("time",Utils.formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss")+"");
                    map.put("Accuracy",location.getAccuracy()+"");
                    j = new JSONObject(map);
                } else {
                    //定位失败
                    sb.append("定位失败" + "\n");
                    sb.append("错误码:" + location.getErrorCode() + "\n");
                    sb.append("错误信息:" + location.getErrorInfo() + "\n");
                    sb.append("错误描述:" + location.getLocationDetail() + "\n");
                }
                sb.append("***定位质量报告***").append("\n");
                sb.append("* WIFI开关：").append(location.getLocationQualityReport().isWifiAble() ? "开启" : "关闭").append("\n");
                sb.append("* GPS状态：").append(getGPSStatusString(location.getLocationQualityReport().getGPSStatus())).append("\n");
                sb.append("* GPS星数：").append(location.getLocationQualityReport().getGPSSatellites()).append("\n");
                sb.append("* 网络类型：" + location.getLocationQualityReport().getNetworkType()).append("\n");
                sb.append("* 网络耗时：" + location.getLocationQualityReport().getNetUseTime()).append("\n");
                sb.append("****************").append("\n");
                //定位之后的回调时间
                sb.append("回调时间: " + Utils.formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");

                //解析定位结果，
                String result = sb.toString();
//                tvResult.setText(result);
            } else {
            }
        }
    };

    private String getGPSStatusString(int statusCode) {
        String str = "";
        switch (statusCode) {
            case AMapLocationQualityReport.GPS_STATUS_OK:
                str = "GPS状态正常";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPROVIDER:
                str = "手机中没有GPS Provider，无法进行GPS定位";
                break;
            case AMapLocationQualityReport.GPS_STATUS_OFF:
                str = "GPS关闭，建议开启GPS，提高定位质量";
                break;
            case AMapLocationQualityReport.GPS_STATUS_MODE_SAVING:
                str = "选择的定位模式中不包含GPS定位，建议选择包含GPS定位的模式，提高定位质量";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPERMISSION:
                str = "没有GPS定位权限，建议开启gps定位权限";
                break;
        }
        return str;
    }

    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(true);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        mOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        return mOption;
    }
}
