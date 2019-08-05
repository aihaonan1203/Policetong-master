package com.example.administrator.policetong.activity.pass_card.more;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.activity.pass_card.one.OneCheckPassCardActivity;
import com.example.administrator.policetong.activity.pass_card.one.OneSelectPointActivity;
import com.example.administrator.policetong.base.BaseActivity;
import com.example.administrator.policetong.base.Consts;
import com.example.administrator.policetong.bean.new_bean.MorePassBean;
import com.example.administrator.policetong.network.DoNet;
import com.example.administrator.policetong.utils.GsonUtil;
import com.example.administrator.policetong.utils.UIUtils;
import com.example.administrator.policetong.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SureCheckActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 九江市交警警务台账
     */
    private TextView mTitleName;
    private Toolbar mTlCustom;
    private TextView mTvName;
    private TextView mTvPhone;
    private TextView mTvYjd;
    private TextView mTvYjz;
    private TextView tv_start_time1;
    private TextView tv_start_time2;
    private TextView tv_end_time1;
    private TextView tv_end_time2;
    private TextView mTvSelectPoint;
    private TextView mTvPoints;
    private Button mBtnSubmit;
    private String lins="";
    private String startHM;
    private String endHM;
    private String startYMD;
    private String endYMD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sure_check);
        initView();
        initToolbar();
        getPassCard(true);
    }

    private void initToolbar() {
        setupToolBar(mTlCustom, false);
        mTitleName.setText("大队复审");
    }

    public void getPassCard(boolean needDialog) {
        DoNet doNet = new DoNet() {
            @Override
            public void doWhat(String response, int id) {
                Log.e("doWhat: ", response);
                if (!GsonUtil.verifyResult_show(response)){
                    return;
                }
                MorePassBean cardInfo = GsonUtil.parseJsonWithGson(JSON.parseObject(response).getJSONObject("data").toString(), MorePassBean.class);
                refresh(cardInfo);
            }
        };
        doNet.setOnErrorListener(new DoNet.OnErrorListener() {
            @Override
            public void onError(int code) {

            }
        });
        doNet.doGet(Consts.URL_TXZDETAIL + getIntent().getStringExtra("id"), this, needDialog);
    }

    private void refresh(MorePassBean cardInfo) {
        mTvName.setText(cardInfo.getTxzform().getName());
        mTvPhone.setText(cardInfo.getTxzform().getPhone());
        mTvYjd.setText(cardInfo.getTxzform().getFqdesc());
        mTvYjz.setText(cardInfo.getTxzform().getZxdesc());
    }


    private void initView() {
        mTitleName =  findViewById(R.id.title_name);
        mTlCustom =  findViewById(R.id.tl_custom);
        mTvName =  findViewById(R.id.tv_name);
        mTvPhone =  findViewById(R.id.tv_phone);
        mTvYjd =  findViewById(R.id.tv_yjd);
        mTvYjz =  findViewById(R.id.tv_yjz);
        tv_start_time1 =  findViewById(R.id.tv_start_time1);
        tv_start_time2 =  findViewById(R.id.tv_start_time2);
        tv_end_time1 =  findViewById(R.id.tv_end_time1);
        tv_end_time2 =  findViewById(R.id.tv_end_time2);
        mTvSelectPoint =  findViewById(R.id.tv_select_point);
        mTvPoints =  findViewById(R.id.tv_points);
        mBtnSubmit =  findViewById(R.id.btn_submit);
        mBtnSubmit.setOnClickListener(this);
        tv_start_time1.setOnClickListener(this);
        tv_start_time2.setOnClickListener(this);
        tv_end_time1.setOnClickListener(this);
        tv_end_time2.setOnClickListener(this);
        mTvSelectPoint.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                if (TextUtils.isEmpty(startYMD)||TextUtils.isEmpty(startHM)||TextUtils.isEmpty(endHM)||TextUtils.isEmpty(endYMD)) {
                    UIUtils.t("请选择时间",false,UIUtils.T_WARNING);
                    return;
                }
                if (TextUtils.isEmpty(lins)) {
                    UIUtils.t("请选择道路",false,UIUtils.T_WARNING);
                    return;
                }
                DoNet doNet = new DoNet() {
                    @Override
                    public void doWhat(String response, int id) {
                        if (!GsonUtil.verifyResult_show(response)) {
                            return;
                        }
                        UIUtils.t(JSON.parseObject(response).getString("message"),false,UIUtils.T_SUCCESS);
                        setResult(666);
                        finish();
                    }
                };
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("id",getIntent().getStringExtra("id"));
                jsonObject.put("txstarttime",startYMD+" "+startHM);
                jsonObject.put("txendtime",endYMD+" "+endHM);
                jsonObject.put("line",lins);
                doNet.doPost(jsonObject,Consts.URL_FUSHEN,this, true);
                break;
            case R.id.tv_start_time1:
                try {
                    selectYearMorthDay(tv_start_time1,"1");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_start_time2:
                try {
                    selectHM(tv_start_time2,"1");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_end_time1:
                try {
                    selectYearMorthDay(tv_end_time1,"2");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_end_time2:
                try {
                    selectHM(tv_end_time2,"2");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_select_point:
                startActivityForResult(new Intent(SureCheckActivity.this, OneSelectPointActivity.class).putExtra("id", lins), 200);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            if (resultCode == 666) {
                mTvPoints.setText(data.getStringExtra("name"));
                lins = data.getStringExtra("id");
            }
        }
    }

    private void selectYearMorthDay(final TextView textView, final String time) throws ParseException {
        DatePickDialog dialog = new DatePickDialog(this);
        //设置上下年分限制
        dialog.setYearLimt(5);
        //设置标题
        dialog.setTitle("选择时间");
        //设置类型
        dialog.setType(DateType.TYPE_YMD);
        //设置消息体的显示格式，日期格式
        dialog.setMessageFormat("yyyy-MM-dd HH:mm");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfOne = new SimpleDateFormat("yyyy-MM-dd");
        long now = System.currentTimeMillis();
        dialog.setStartDate(new Date(sdfOne.parse(sdfOne.format(now)).getTime()));
        //设置点击确定按钮回调
        dialog.setOnSureLisener(new OnSureLisener() {
            @Override
            public void onSure(Date date) {
                @SuppressLint("SimpleDateFormat") String string = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
                textView.setText(string);
                if ("1".equals(time)) {
                    startYMD = string;
                }else {
                    endYMD = string;
                }
            }
        });
        dialog.show();
    }

    private void selectHM(final TextView textView, final String time) throws ParseException {
        DatePickDialog dialog = new DatePickDialog(this);
        //设置上下年分限制
        dialog.setYearLimt(5);
        //设置标题
        dialog.setTitle("选择时间");
        //设置类型
        dialog.setType(DateType.TYPE_HM);
        if (time.equals("1")){
            dialog.setStartDate(new Date(Utils.getStringToDate("00:00","HH:mm")));
        }else {
            dialog.setStartDate(new Date(Utils.getStringToDate("06:00","HH:mm")));
        }
        //设置点击确定按钮回调
        dialog.setOnSureLisener(new OnSureLisener() {
            @Override
            public void onSure(Date date) {
                @SuppressLint("SimpleDateFormat") String string = new SimpleDateFormat("HH:mm").format(date);
                textView.setText(string);
                if ("1".equals(time)) {
                    startHM = string;
                }else {
                    endHM = string;
                }
            }
        });
        dialog.show();
    }
}
