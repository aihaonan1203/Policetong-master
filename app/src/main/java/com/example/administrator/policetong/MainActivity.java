package com.example.administrator.policetong;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.PopupWindowCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.policetong.activity.ChangePwdActivity;
import com.example.administrator.policetong.activity.HelpActivity;
import com.example.administrator.policetong.activity.LoginActivity;
import com.example.administrator.policetong.activity.ModulesActivity;
import com.example.administrator.policetong.activity.NoticeActivity;
import com.example.administrator.policetong.activity.pass_card.more.MorePassCardListActivity;
import com.example.administrator.policetong.activity.pass_card.one.OnePassCardActivity;
import com.example.administrator.policetong.base.BaseActivity;
import com.example.administrator.policetong.base.BaseBean;
import com.example.administrator.policetong.base.Consts;
import com.example.administrator.policetong.bean.NoticeBean;
import com.example.administrator.policetong.bean.Notice_bean;
import com.example.administrator.policetong.httppost.getNetInfo;
import com.example.administrator.policetong.network.Network;
import com.example.administrator.policetong.new_bean.GongGaoBean;
import com.example.administrator.policetong.utils.GsonUtil;
import com.example.administrator.policetong.utils.LoadingDialog;
import com.example.administrator.policetong.utils.MorePopupWindow;
import com.example.administrator.policetong.utils.NotificationUtils;
import com.example.administrator.policetong.utils.SPUtils;
import com.example.administrator.policetong.utils.UIUtils;
import com.example.administrator.policetong.utils.Util;
import com.example.administrator.policetong.utils.Utils;
import com.example.administrator.policetong.utils.VersionUtils;
import com.example.administrator.policetong.view.OkGoUpdateHttpUtil;
import com.master.permissionhelper.PermissionHelper;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.UpdateCallback;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private ImageView notice;
    private ImageView mMainMore;
    private ImageView tv_email;
    private ImageView tv_jbtz;
    private ImageView tv_aqpc;
    private ImageView tv_zfzg;
    private ImageView tv_rcqw;
    private ImageView tv_dltz;
    private ImageView tv_manage;
    private ImageView iv_study;
    private ImageView iv_tingchechang;
    private ImageView iv_shigu;
    private ImageView tv_tx;
    private TextView ac_username;
    private static final int BAIDU_READ_PHONE_STATE = 100;
    private Timer timer;
    private String json = "";
    private List<NoticeBean.MsgArrayBean> msgArrayBeans;
    private List<GongGaoBean> data = new ArrayList<>();

    private int test;

    @SuppressLint("ObsoleteSdkInt")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            setContentView(R.layout.activity_main);
            initView();
            initListener();
            sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
            if (Build.VERSION.SDK_INT >= 23) {
                showContacts();
            } else {
                dingwei();
            }
            updateVision();
//            updata_mag();
//            timer = new Timer();
//            timer.schedule(new TimerTask() {
//                @Override
//                public void run() {
////                    get_notice2();
//                    getGuard();
//                }
//            }, 0, 10000);
        } catch (Exception e) {

        }
    }


    private void updata_mag() {
        myPermission();
    }

    public void showContacts() {
        PermissionHelper permissionHelper = new PermissionHelper(this, new String[]{
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        }, 100);
        permissionHelper.request(new PermissionHelper.PermissionCallback() {
            @Override
            public void onPermissionGranted() {
                dingwei();
            }

            @Override
            public void onIndividualPermissionGranted(String[] grantedPermission) {
                showContacts();
            }

            @Override
            public void onPermissionDenied() {
                showContacts();
            }

            @Override
            public void onPermissionDeniedBySystem() {

            }
        });
    }

    private void dingwei() {
    }

    private void getGuard() {
        Map<String, String> map = new HashMap<>();
        map.put("userid", String.valueOf(userInfo.getUser().getUid()));
        disposable = Network.getPoliceApi(false).getNotice(RequestBody.create(MediaType.parse("application/json"), new JSONObject(map).toString()))
                .compose(BaseActivity.<BaseBean<List<GongGaoBean>>>applySchedulers())
                .subscribe(new Consumer<BaseBean<List<GongGaoBean>>>() {
                    @Override
                    public void accept(BaseBean<List<GongGaoBean>> bean) throws Exception {
                        if (!bean.getErrMsg().equals("SUCCESS")) {
                            return;
                        }
                        if (data != null && data.size() > 0) {
                            data.clear();
                        }
                        data = bean.getData();
                        String s = data.get(data.size() - 1).getMsg();
                        if (!s.equals(json) && !json.equals("")) {
                            NotificationUtils notificationUtils = new NotificationUtils(MainActivity.this);
                            notificationUtils.sendNotification("公告通知", "您有一条新的公告通知,请及时处理!");
                            json = s;
                            showDialog(data.get(0).getMsg(), Utils.stampToDate(data.get(0).getBeginTime()));
                        } else {
                            if (json.equals("")) {
                                json = s;
                                showDialog(String.valueOf(data.size()));
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("accept: ", "");
                    }
                });
    }

    public void get_notice2() {
        noticelist3 = new ArrayList<>();
        Map map = new HashMap();
        map.put("userid", "101");
        getNetInfo.NetInfo(this, "Selectmessage", new JSONObject(map), new getNetInfo.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject object) throws JSONException {
                Log.e("onSuccess: ", object.toString());
                if (object.getString("ERRMSG").equals("成功")) {
                    JSONArray jsonArray = object.getJSONArray("MsgArray");
                    if (msgArrayBeans != null && msgArrayBeans.size() > 0) {
                        msgArrayBeans.clear();
                    }
                    msgArrayBeans = GsonUtil.parseJsonArrayWithGson(jsonArray.toString(), NoticeBean.MsgArrayBean.class);
                    Collections.sort(msgArrayBeans, new Comparator<NoticeBean.MsgArrayBean>() {
                        @Override
                        public int compare(NoticeBean.MsgArrayBean bean1, NoticeBean.MsgArrayBean bean2) {
                            return bean2.getDate().compareTo(bean1.getDate());
                        }
                    });
                    if (msgArrayBeans.size() > 0) {
                        NoticeBean.MsgArrayBean bean = msgArrayBeans.get(0);
                        String s = bean.toString();
                        if (!s.equals(json) && !json.equals("")) {
                            NotificationUtils notificationUtils = new NotificationUtils(MainActivity.this);
                            notificationUtils.sendNotification("公告通知", "您有一条新的公告通知,请及时处理!");
                            json = s;
                            showDialog(bean.getMsg(), bean.getDate());
                        } else {
                            if (json.equals("")) {
                                json = s;
                                showDialog(String.valueOf(msgArrayBeans.size()));
                            }
                        }
                    }

                } else {
                    LoadingDialog.showToast_shibai(MainActivity.this);
                }

            }

            @Override
            public void onError(VolleyError volleyError) {
                LoadingDialog.showToast_shibai(MainActivity.this);
            }
        });
    }

    private void showDialog(String msg, String time) {
        new LovelyStandardDialog(MainActivity.this, LovelyStandardDialog.ButtonLayout.VERTICAL)
                .setTopColorRes(R.color.ic_launcher_background)
                .setButtonsColorRes(R.color.ic_launcher_background)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(getString(R.string.app_name) + "-公告")
                .setMessage(msg + "\n\n" + time)
                .setPositiveButton(android.R.string.ok, null)
                .setPositiveButton("退出", null)
                .show();
    }

    private void showDialog(String i) {
        new LovelyStandardDialog(MainActivity.this, LovelyStandardDialog.ButtonLayout.VERTICAL)
                .setTopColorRes(R.color.ic_launcher_background)
                .setButtonsColorRes(R.color.ic_launcher_background)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(getString(R.string.app_name))
                .setMessage("当前服务器共有" + i + "条未到期的公告，请确认是否需要查看。")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton("点击查看", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EventBus.getDefault().postSticky(data);
                        startActivity(new Intent(MainActivity.this, NoticeActivity.class));
                    }
                })
                .show();
    }


    List<Notice_bean> noticelist, noticelist2, noticelist3;


    @Override
    protected void onRestart() {
        super.onRestart();
        showContacts();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(MainActivity.this, ModulesActivity.class);
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.tv_jbtz:
                bundle.putInt("id", 2);
                break;
            case R.id.tv_aqpc:
                bundle.putInt("id", 3);
                break;
            case R.id.tv_zfzg:
                bundle.putInt("id", 4);
                break;
            case R.id.tv_rcqw:
                bundle.putInt("id", 5);
                break;
            case R.id.tv_dltz:
                bundle.putInt("id", 6);
                break;
            case R.id.tv_manage:
                bundle.putInt("id", 7);
                break;
            case R.id.tv_study:
                bundle.putInt("id", 8);
                break;
            case R.id.iv_shigu:
                bundle.putInt("id", 9);
                break;
            case R.id.iv_tingchecgang:
                bundle.putInt("id", 10);
                break;

        }
        intent.putExtra("data", bundle);
        startActivity(intent);
//        UIUtils.t("暂时无法使用该功能！",false,UIUtils.T_ERROR);
    }

    SharedPreferences sharedPreferences;

    private void initListener() {
        mMainMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MorePopupWindow mWindow = new MorePopupWindow(MainActivity.this, new MorePopupWindow.PopCallback() {
                    @Override
                    public void onSuccess(int i) {
                        switch (i) {
                            case 0:
                                finish();
                                SPUtils.saveBoolean("isLogin",false);
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                break;
                            case 1:
                                startActivity(new Intent(MainActivity.this, ChangePwdActivity.class));
                                break;
                        }

                    }
                });
                mWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        WindowManager.LayoutParams lp = getWindow().getAttributes();
                        lp.alpha = 1.0f;
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        getWindow().setAttributes(lp);
                    }
                });
                PopupWindowCompat.showAsDropDown(mWindow, mMainMore, 0, 0, Gravity.START);
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 0.6f;
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getWindow().setAttributes(lp);
            }
        });
    }

    private void initView() {
        notice = findViewById(R.id.tv_notice);
        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().postSticky(data);
                startActivity(new Intent(MainActivity.this, NoticeActivity.class));
            }
        });
        mMainMore = findViewById(R.id.main_more);
        tv_email =  findViewById(R.id.tv_email);
        tv_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, OnePassCardActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.tv_more_pass_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MorePassCardListActivity.class);
                startActivity(intent);
            }
        });
        tv_jbtz =  findViewById(R.id.tv_jbtz);
        tv_jbtz.setOnClickListener(this);
        tv_aqpc =  findViewById(R.id.tv_aqpc);
        tv_aqpc.setOnClickListener(this);
        tv_zfzg =  findViewById(R.id.tv_zfzg);
        tv_zfzg.setOnClickListener(this);
        tv_rcqw =  findViewById(R.id.tv_rcqw);
        tv_rcqw.setOnClickListener(this);
        tv_dltz =  findViewById(R.id.tv_dltz);
        tv_dltz.setOnClickListener(this);
        ac_username =  findViewById(R.id.ac_username);
        iv_study = findViewById(R.id.tv_study);
        iv_study.setOnClickListener(this);
        iv_tingchechang = findViewById(R.id.iv_tingchecgang);
        iv_shigu = findViewById(R.id.iv_shigu);
        tv_tx = findViewById(R.id.ac_photo);
        iv_tingchechang.setOnClickListener(this);
        iv_shigu.setOnClickListener(this);
        tv_manage = findViewById(R.id.tv_manage);
        tv_manage.setOnClickListener(this);
        ac_username.setText(userInfo.getUser().getTruename());
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


    //Android6.0申请权限的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
//                    dingwei();
//                } else {
//                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                    builder.setMessage("您的定位权限已被禁止，请到 “应用信息 -> 权限” 中授予所有权限！");
//                    builder.setPositiveButton("去手动授权", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Intent intent = new Intent();
//                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                            intent.addCategory(Intent.CATEGORY_DEFAULT);
//                            intent.setData(Uri.parse("package:" + getPackageName()));
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//                            startActivity(intent);
//                            finish();
//                        }
//                    });
//                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//
//                        }
//                    });
//                    builder.show();
//                }
                break;
            case REQUEST_EXTERNAL_STORAGE:
//                showDownloadProgressDialog(MainActivity.this);
                break;
            case 3:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    installAPK();
//                } else {
//                    //跳转到安装未知应用界面
//                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
//                    startActivityForResult(intent, 5);//on line 380
//                }
                break;
            case 1:
//                update();
                break;
            case 5:
//                installAPK();
                break;
            default:
                break;
        }
    }

    //更新版本
    private void updateVision() {
        String mUpdateUrl = Consts.URL_GETVERSION;
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        new UpdateAppManager
                .Builder()
                //必须设置，当前Activity
                .setActivity(MainActivity.this)
                //必须设置，实现httpManager接口的对象
                .setHttpManager(new OkGoUpdateHttpUtil())
                //必须设置，更新地址
                .setUpdateUrl(mUpdateUrl)
                //以下设置，都是可选
                //设置请求方式，默认get
                .setPost(false)
                //添加自定义参数，默认version=1.0.0（app的versionName）；apkKey=唯一表示（在AndroidManifest.xml配置）
//                .setParams(params)
                //设置点击升级后，消失对话框，默认点击升级后，对话框显示下载进度
                .hideDialogOnDownloading()
                //设置头部，不设置显示默认的图片，设置图片后自动识别主色调，然后为按钮，进度条设置颜色
//                .setTopPic(R.mipmap.ic_launcher)
                //为按钮，进度条设置颜色，默认从顶部图片自动识别。
                .setThemeColor(MainActivity.this.getResources().getColor(R.color.colorMain))
                //设置apk下砸路径，默认是在下载到sd卡下/Download/1.0.0/test.apk
                .setTargetPath(path)
                //不显示通知栏进度条
                .dismissNotificationProgress()
                //是否忽略版本
                //.showIgnoreVersion()

                .build()
                //检测是否有新版本
                .checkNewApp(new UpdateCallback() {

                    @Override
                    protected UpdateAppBean parseJson(String json) {
                        UpdateAppBean updateAppBean = new UpdateAppBean();

                        int visionCurrent = VersionUtils.getVersion(MainActivity.this);
                        com.alibaba.fastjson.JSONObject resultJson = JSON.parseObject(json);
//                        //新版本版本号
                        String newVersion = resultJson.getJSONObject("data").getString("version");

                        updateAppBean
//                                .setUpdate("Yes")
                                //（必须）新版本号，
                                .setNewVersion("最新")
                                //（必须）下载地址
                                .setApkFileUrl("https://pic.jjedd.net:9000/app/app.apk")
                                //（必须）更新内容
                                .setUpdateLog("更新内容：\n\r\r\r\r1.优化了部分功能。\n\r\r\r\r2.修复了部分bug。\n\r\r\r\r请耐心等待更新完成！")
                                //大小，不设置不显示大小，可以不设置
//                                .setTargetSize(jsonObject.optString("target_size"))
                                //是否强制更新，可以不设置
                                .setConstraint(true);
                        //设置md5，可以不设置
//                                    .setNewMd5(jsonObject.optString("new_md51"));
                        double newversioncode = Double.parseDouble(newVersion);
                        int cc = (int) (newversioncode);
                        if (cc == visionCurrent) {
                            return updateAppBean.setUpdate("No");
                        }
                        if (visionCurrent < cc) {
                            return updateAppBean.setUpdate("Yes");
                        }
                        return updateAppBean.setUpdate("No");

                    }


                    /**
                     * 网络请求之前
                     */
                    @Override
                    public void onBefore() {
//                        CProgressDialogUtils.showProgressDialog((Activity)mContext);
                    }

                    /**
                     * 网路请求之后
                     */
                    @Override
                    public void onAfter() {
//                        CProgressDialogUtils.cancelProgressDialog((Activity)mContext);
                    }


                });
    }


    private int INSTALL_PACKAGES_REQUESTCODE = 5;

    private void update() {
        RequestQueue requestQueue = null;
        int versionCode = 0;
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        }
        try {
            PackageInfo packageInfo = this.getApplication().getPackageManager().getPackageInfo(this.getPackageName(), 0);
            Log.e("当前版本号", packageInfo.versionCode + "");
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Map<String, String> map = new HashMap<>();
        String downloadUrl =Consts.URL_GETVERSION;
        final int finalVersionCode = versionCode;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, downloadUrl, new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject jsonObject) {
                Log.e("返回的更新信息", jsonObject.toString());
                try {
                    String version = jsonObject.getString("version");
                    if (Integer.valueOf(version) > finalVersionCode) {
                        final AlertDialog updatedialog = new AlertDialog.Builder(MainActivity.this).create();
                        // 设置对话框标题
                        updatedialog.setTitle("升级提示");
                        // 设置对话框消息
                        updatedialog.setMessage("检测到新版本，是否更新？");
                        // 添加选择按钮并注册监听
                        updatedialog.setButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    verifyStoragePermissions(MainActivity.this, jsonObject.getString("path"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        updatedialog.setButton2("忽略", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                updatedialog.dismiss();
                            }
                        });
                        // 显示对话框
                        updatedialog.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(request);
    }

    File file;

    @SuppressLint("LongLogTag")
    private void showDownloadProgressDialog(Context context, String url) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("提示");
        progressDialog.setMessage("正在下载...");
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);                    //设置不可点击界面之外的区域让对话框小时
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);         //进度条类型
        progressDialog.show();
        String downloadUrl = "http://" + Util.loadSetting(MainActivity.this).getUrl() + ":" + Util.loadSetting(MainActivity.this).getUrlPort() + "/pointsman/app" + url; //这里写你的apk url地址
        new DownloadAPK(progressDialog).execute(downloadUrl);
    }


    private void checkIsAndroidO() {
        if (Build.VERSION.SDK_INT >= 26) {
            boolean b = getPackageManager().canRequestPackageInstalls();
            if (b) {
                installAPK();
            } else {
                //请求安装未知应用来源的权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, INSTALL_PACKAGES_REQUESTCODE);
            }
        } else {
            installAPK();
        }

    }

    public void installAPK() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT > 23) {
            //第二个参数是你的包名+.FileProvider（你也可以直接去AndroidMenifest中把你刚才写的provider的authorities拷贝过来，Fileprovider开头大小写不限）
            Uri contentUri = FileProvider.getUriForFile(MainActivity.this, "com.example.administrator.policetong.fileprovider", file);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        startActivity(intent);
    }


    // 6.0 以上申请SD卡读写权限
    public void myPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(this, permissions, 1);
            } else {
//                update();
            }
        } else {
//            update();
        }
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 9;
    private static String[] PERMISSIONS_STORAGE = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};

    public void verifyStoragePermissions(Activity activity, String url) {
        try { //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            } else {
                showDownloadProgressDialog(MainActivity.this, url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void help(View view) {
        Intent intent = new Intent(MainActivity.this, HelpActivity.class);
        startActivity(intent);
    }

    private class DownloadAPK extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;

        public DownloadAPK(ProgressDialog progressDialog) {
            this.progressDialog = progressDialog;
        }

        @Override
        protected String doInBackground(String... params) {
//　　　　　　　//根据url获取网络数据生成apk文件
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + "pointsman.apk");
                if (!file.exists()) {
                    file.createNewFile();
                }
                int fileLength = connection.getContentLength();
                FileOutputStream fos = new FileOutputStream(file);
                InputStream is = connection.getInputStream();
                byte[] buf = new byte[1024 * 5];
                int len;
                int total = 0;
                while ((len = is.read(buf)) != -1) {
                    total += len;
                    publishProgress((int) (total * 100 / fileLength));
                    fos.write(buf, 0, len);
                }
                is.close();
                fos.close();
            } catch (MalformedURLException e) {
            } catch (IOException e) {
                //有人跟我说打这个e特别消耗内存
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //这里 改变ProgressDialog的进度值
            progressDialog.setProgress(values[0]);
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//　　　　　　　到这里说明下载完成，判断文件是否存在，如果存在，执行安装apk的操作
            progressDialog.dismiss();
            checkIsAndroidO();
        }

    }

}
