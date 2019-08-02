package com.example.administrator.policetong.activity.pass_card.one;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.base.BaseActivity;
import com.example.administrator.policetong.base.Consts;
import com.example.administrator.policetong.network.DoNet;
import com.example.administrator.policetong.utils.GsonUtil;
import com.example.administrator.policetong.utils.UIUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OneCheckPassCardActivity extends BaseActivity implements View.OnClickListener {

    private TextView title_name;
    private Toolbar tl_custom;
    private TextView tv_name;
    private TextView tv_phone;
    private RadioButton rb_pass;
    private RadioButton rb_refuse;
    private TextView tv_start_time;
    private TextView tv_points;
    private TextView tv_end_time;
    private TextView tv_select_point;
    private EditText et_remark;
    private LinearLayout ll_content;
    private Button btn_submit;
    private String startTime="";
    private String endTime="";
    private String id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_pass_card);
        initView();
        initToolbar();
    }


    private void initToolbar() {
        setupToolBar(tl_custom, false);
        title_name.setText("单个通行证审核");
    }

    private void initView() {
        title_name = findViewById(R.id.title_name);
        tl_custom = findViewById(R.id.tl_custom);
        tv_name = findViewById(R.id.tv_name);
        tv_phone = findViewById(R.id.tv_phone);
        rb_pass = findViewById(R.id.rb_pass);
        ll_content = findViewById(R.id.ll_content);
        rb_refuse = findViewById(R.id.rb_refuse);
        tv_points = findViewById(R.id.tv_points);

        tv_start_time = findViewById(R.id.tv_start_time);
        tv_start_time.setOnClickListener(this);
        tv_end_time = findViewById(R.id.tv_end_time);
        tv_end_time.setOnClickListener(this);
        tv_select_point = findViewById(R.id.tv_select_point);
        tv_select_point.setOnClickListener(this);
        et_remark = findViewById(R.id.et_remark);
        btn_submit = findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);


        tv_name.setText(getIntent().getStringExtra("name"));
        tv_phone.setText(getIntent().getStringExtra("phone"));

        rb_pass.setOnClickListener(this);
        rb_refuse.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                submit();
                break;
            case R.id.tv_start_time:
                selectTime(tv_start_time,"1");
                break;
            case R.id.tv_end_time:
                selectTime(tv_end_time,"2");
                break;
            case R.id.tv_select_point:
                startActivityForResult(new Intent(OneCheckPassCardActivity.this, OneSelectPointActivity.class).putExtra("id", id), 200);
                break;
            case R.id.rb_pass:
                ll_content.setVisibility(View.VISIBLE);
                break;
            case R.id.rb_refuse:
                ll_content.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            if (resultCode == 666) {
                tv_points.setText(data.getStringExtra("name"));
                id = data.getStringExtra("id");
            }
        }
    }


    private void selectTime(final TextView textView, final String time) {
        DatePickDialog dialog = new DatePickDialog(this);
        //设置上下年分限制
        dialog.setYearLimt(5);
        //设置标题
        dialog.setTitle("选择时间");
        //设置类型
        dialog.setType(DateType.TYPE_ALL);
        //设置消息体的显示格式，日期格式
        dialog.setMessageFormat("yyyy-MM-dd HH:mm");
        //设置点击确定按钮回调
        dialog.setOnSureLisener(new OnSureLisener() {
            @Override
            public void onSure(Date date) {
                @SuppressLint("SimpleDateFormat") String string = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                textView.setText(string);
                if ("1".equals(time)) {
                    startTime=string;
                }else {
                    endTime=string;
                }
            }
        });
        dialog.show();
    }

    private void submit() {
        // validate
        String name = tv_select_point.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            UIUtils.t("请选择道路",false,UIUtils.T_WARNING);
            return;
        }

        if (rb_pass.isChecked()){
            if (TextUtils.isEmpty(startTime)||TextUtils.isEmpty(endTime)) {
                UIUtils.t("请选择时间",false,UIUtils.T_WARNING);
                return;
            }

            if (TextUtils.isEmpty(id)) {
                UIUtils.t("请选择道路",false,UIUtils.T_WARNING);
                return;
            }
        }else {
            if (et_remark.getText().toString().trim().isEmpty()){
                UIUtils.t("请输入拒绝理由！",false,UIUtils.T_WARNING);
                return;
            }
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
        if (rb_pass.isChecked()){
            jsonObject.put("result","1");
            jsonObject.put("line",id);
            jsonObject.put("txstarttime",startTime);
            jsonObject.put("txendtime",endTime);
        }else {
            jsonObject.put("result","2");
        }
        jsonObject.put("failure_cause", et_remark.getText().toString().trim());
        doNet.doPost(jsonObject,Consts.URL_ONESHENHE, this, true);
    }
}
