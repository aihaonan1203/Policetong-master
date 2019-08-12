package com.example.administrator.policetong;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import com.example.administrator.policetong.activity.ChangePwdActivity;
import com.example.administrator.policetong.activity.LoginActivity;
import com.example.administrator.policetong.activity.ModulesActivity;
import com.example.administrator.policetong.activity.NoticeActivity;
import com.example.administrator.policetong.activity.pass_card.more.MorePassCardListActivity;
import com.example.administrator.policetong.activity.pass_card.one.OnePassCardActivity;
import com.example.administrator.policetong.base.BaseActivity;
import com.example.administrator.policetong.base.Consts;
import com.example.administrator.policetong.bean.NoticeBean;
import com.example.administrator.policetong.network.DoNet;
import com.example.administrator.policetong.new_bean.GongGaoBean;
import com.example.administrator.policetong.new_bean.StudyBean;
import com.example.administrator.policetong.utils.GsonUtil;
import com.example.administrator.policetong.utils.MorePopupWindow;
import com.example.administrator.policetong.utils.SPUtils;
import com.example.administrator.policetong.utils.VersionUtils;
import com.example.administrator.policetong.view.OkGoUpdateHttpUtil;
import com.master.permissionhelper.PermissionHelper;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.UpdateCallback;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.greenrobot.eventbus.EventBus;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mMainMore;
    private List<GongGaoBean> data = new ArrayList<>();
    private List<NoticeBean> noticeData;

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
            showContacts();
            updateVision();
            getNotice();
        } catch (Exception e) {

        }
    }

    private void getNotice() {
        DoNet doNet = new DoNet() {
            @Override
            public void doWhat(String response, int id) {
                if (!GsonUtil.verifyResult_show(response)) {
                    return;
                }
                Log.e("doWhat: ", response);
                com.alibaba.fastjson.JSONObject json = JSON.parseObject(response).getJSONObject("data");
                com.alibaba.fastjson.JSONArray jsonArray = json.getJSONArray("data");
                noticeData = GsonUtil.parseJsonArrayWithGson(jsonArray.toString(), NoticeBean.class);
                if (noticeData ==null|| noticeData.size()==0){
                    return;
                }
                NoticeBean noticeBean = noticeData.get(0);
                String content=noticeBean.getContent().replace("&lt;p&gt;","  ");
                String string=content.replace("&lt;/p&gt;","  ");
                noticeBean.setContent(string);
                if (noticeBean.getId()!=SPUtils.getInt("notice_id",-1)){
                    showDialog(noticeBean.getContent(),noticeBean.getCreate_time(),noticeBean.getId());
                }
            }
        };
        RequestParams requestParams = new RequestParams(Consts.URL_RCQWLIST);
        requestParams.addParameter("limit", 20);
        requestParams.addParameter("page", 1);
        doNet.doGet(Consts.URL_NOTICELIST, MainActivity.this, false);
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

    private void showDialog(String msg, String time, final int id) {
        new LovelyStandardDialog(MainActivity.this, LovelyStandardDialog.ButtonLayout.VERTICAL)
                .setTopColorRes(R.color.ic_launcher_background)
                .setButtonsColorRes(R.color.ic_launcher_background)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(getString(R.string.app_name) + "-公告")
                .setMessage(msg + "\n\n" + time)
                .setNegativeButton("不再提示", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SPUtils.saveInt("notice_id",id);
                    }
                })
                .setPositiveButton("收到",null)
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
        ImageView notice = findViewById(R.id.tv_notice);
        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().postSticky(noticeData);
                startActivity(new Intent(MainActivity.this, NoticeActivity.class));
            }
        });
        mMainMore = findViewById(R.id.main_more);
        ImageView tv_email = findViewById(R.id.tv_email);
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
        ImageView tv_jbtz = findViewById(R.id.tv_jbtz);
        tv_jbtz.setOnClickListener(this);
        ImageView tv_aqpc = findViewById(R.id.tv_aqpc);
        tv_aqpc.setOnClickListener(this);
        ImageView tv_zfzg = findViewById(R.id.tv_zfzg);
        tv_zfzg.setOnClickListener(this);
        ImageView tv_rcqw = findViewById(R.id.tv_rcqw);
        tv_rcqw.setOnClickListener(this);
        ImageView tv_dltz = findViewById(R.id.tv_dltz);
        tv_dltz.setOnClickListener(this);
        TextView ac_username = findViewById(R.id.ac_username);
        ImageView iv_study = findViewById(R.id.tv_study);
        iv_study.setOnClickListener(this);
        ImageView iv_tingchechang = findViewById(R.id.iv_tingchecgang);
        ImageView iv_shigu = findViewById(R.id.iv_shigu);
        iv_tingchechang.setOnClickListener(this);
        iv_shigu.setOnClickListener(this);
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
}
