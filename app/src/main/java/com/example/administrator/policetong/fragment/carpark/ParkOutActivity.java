package com.example.administrator.policetong.fragment.carpark;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.activity.ManageActivity;
import com.example.administrator.policetong.base.App;
import com.example.administrator.policetong.base.BaseActivity;
import com.example.administrator.policetong.base.Consts;
import com.example.administrator.policetong.network.DoNet;
import com.example.administrator.policetong.utils.GsonUtil;
import com.example.administrator.policetong.utils.UIUtils;
import com.luck.picture.lib.entity.LocalMedia;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;

public class ParkOutActivity extends BaseActivity implements View.OnClickListener, BaseActivity.PhotoCallBack {


    private EditText etName;
    private EditText etCarNo;
    private EditText etContent;
    private TextView tv_photo;
    private TextView tvImage;
    private Button btnSubmit;
    private int type=0;
    private String voucher;
    private TextView titleName;
    private Toolbar tlCustom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park_out);
        initView();
        titleName.setText("车辆驶出登记");
        setupToolBar(tlCustom,false);
        setPhoto(this);
    }


    private void submit() {
        if (voucher==null||voucher.isEmpty()){
            UIUtils.t("请拍摄凭证照片",false,UIUtils.T_WARNING);
            return;
        }
        if (etContent.getText().toString().trim().isEmpty()){
            UIUtils.t("请输入出场描述！",false,UIUtils.T_WARNING);
            return;
        }
        if (selectList.size()==0){
            UIUtils.t("请拍摄出场照片！",false,UIUtils.T_WARNING);
            return;
        }
        setDialog();
        PostFormBuilder builder = new PostFormBuilder();
        for (int i = 0; i < selectList.size(); i++) {
            builder.addFile("file[]", new File(selectList.get(i).getPath()).getName(), new File(selectList.get(i).getPath()));
        }
        builder.url("https://api.jjedd.net:9000/v1/uploadImg")
                .addHeader("token", App.userInfo.getToken())
                .addHeader("user", App.userInfo.getUser().getUser())
                .addHeader("Content-Type", "multipart/form-data")
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id, int code) {
                        closeDialog();
                        UIUtils.t("图片上传失败",false,UIUtils.T_ERROR);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        com.alibaba.fastjson.JSONObject json = JSON.parseObject(response);
                        DoNet doNet = new DoNet() {
                            @Override
                            public void doWhat(String response, int id) {
                                closeDialog();
                                if (!GsonUtil.verifyResult_show(response)) {
                                    return;
                                }
                                UIUtils.t(JSON.parseObject(response).getString("message"), false, UIUtils.T_SUCCESS);
                                setResult(666);
                                finish();
                            }
                        };
                        doNet.setOnErrorListener(new DoNet.OnErrorListener() {
                            @Override
                            public void onError(int code) {
                                closeDialog();
                            }
                        });
                        com.alibaba.fastjson.JSONObject jsonObject =new com.alibaba.fastjson.JSONObject();
                        jsonObject.put("id",getIntent().getStringExtra("id"));
                        jsonObject.put("outdesc", etContent.getText().toString().trim());
                        jsonObject.put("voucher", voucher);
                        jsonObject.put("outpic", json.getJSONObject("data").getString("filepath"));
                        doNet.doPost(jsonObject, Consts.URL_CAR_OUT,ParkOutActivity.this, false);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSubmit:
                submit();
                break;
            case R.id.iv_take_photo1:
                type = 1;
                takeOnePhoto();
                break;
            case R.id.iv_take_photo2:
                type = 2;
                takeOnePhoto();
                break;
        }
    }



    private List<LocalMedia> selectList=new ArrayList<>();
    @Override
    public void getPhoto(final List<LocalMedia> selectList) {
        if (type==2){
            tv_photo.setText(String.format(getResources().getString(R.string.photo), selectList.size() + ""));
            this.selectList.addAll(selectList);
            selectList.clear();
        }else {
            setDialog();
            PostFormBuilder builder = new PostFormBuilder();
            builder.addFile("file[]", new File(   selectList.get(0).getPath()).getName(),new File(selectList.get(0).getPath()));
            builder.url("https://api.jjedd.net:9000/v1/uploadImg")
                    .addHeader("token", App.userInfo.getToken())
                    .addHeader("user", App.userInfo.getUser().getUser())
                    .addHeader("Content-Type", "multipart/form-data")
                    .build()
                    .execute(new StringCallback() {

                        @Override
                        public void onError(Call call, Exception e, int id, int code) {
                            selectList.clear();
                            closeDialog();
                            UIUtils.t("图片上传失败",false,UIUtils.T_ERROR);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            closeDialog();
                            Drawable drawable=getResources().getDrawable(R.drawable.icon_right);
                            drawable.setBounds(0, 0, 50, 50);
                            tvImage.setCompoundDrawables(null,null,drawable,null);
                            selectList.clear();
                            com.alibaba.fastjson.JSONObject json = JSON.parseObject(response);
                            voucher=json.getJSONObject("data").getString("filepath");
                        }
                    });
        }

    }

    private void initView() {
        titleName =  findViewById(R.id.title_name);
        tlCustom =  findViewById(R.id.tl_custom);
        etName =  findViewById(R.id.etName);
        tvImage =  findViewById(R.id.tvImage);
        tvImage =  findViewById(R.id.tvImage);
        etCarNo =  findViewById(R.id.etCarNo);
        etContent =  findViewById(R.id.etContent);
        tv_photo =  findViewById(R.id.tv_photo);
        btnSubmit =  findViewById(R.id.btnSubmit);
        tv_photo.setText(String.format(getResources().getString(R.string.photo), "0"));
        etName.setText(getIntent().getStringExtra("name"));
        etCarNo.setText(getIntent().getStringExtra("carNo"));
        btnSubmit.setOnClickListener(this);
        findViewById(R.id.iv_take_photo1).setOnClickListener(this);
        findViewById(R.id.iv_take_photo2).setOnClickListener(this);
    }
}
