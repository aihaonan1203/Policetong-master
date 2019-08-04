package com.example.administrator.policetong.activity.pass_card.more;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.base.BaseActivity;
import com.example.administrator.policetong.base.Consts;
import com.example.administrator.policetong.bean.new_bean.MorePassBean;
import com.example.administrator.policetong.network.DoNet;
import com.example.administrator.policetong.utils.GsonUtil;
import com.example.administrator.policetong.utils.UIUtils;
import com.example.administrator.policetong.view.ClearWriteEditText;

public class MoreCheckActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 九江市交警警务台账
     */
    private TextView mTitleName;
    private TextView mTitleRightTxt;
    private Toolbar mTlCustom;
    private TextView mTvName;
    private TextView mTvPhone;
    private TextView mTvYj;
    private Spinner mMSpinner;
    private ClearWriteEditText mEtRemark;
    private Button mBtnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_check);
        initView();
        initToolbar();
        getPassCard(true);
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
                assert cardInfo != null;
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
        mTvYj.setText(cardInfo.getTxzform().getFqdesc());
        mTvName.setText(cardInfo.getTxzform().getName());
        mTvPhone.setText(cardInfo.getTxzform().getPhone());
    }

    private void initToolbar() {
        setupToolBar(mTlCustom, false);
        mTitleName.setText("中队审核");
    }

    private void initView() {
        mTitleName =  findViewById(R.id.title_name);
        mTitleRightTxt =  findViewById(R.id.title_right_txt);
        mTlCustom =  findViewById(R.id.tl_custom);
        mTvName =  findViewById(R.id.tv_name);
        mTvPhone =  findViewById(R.id.tv_phone);
        mTvYj =  findViewById(R.id.tv_yj);
        mMSpinner =  findViewById(R.id.mSpinner);
        mEtRemark =  findViewById(R.id.et_remark);
        mBtnSubmit =  findViewById(R.id.btn_submit);
        mBtnSubmit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_submit:
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
                jsonObject.put("result",mMSpinner.getSelectedItemPosition()+1);
                jsonObject.put("zxdesc",mEtRemark.getText().toString().trim());
                doNet.doPost(jsonObject,Consts.URL_SHENHE, this, true);
                break;
        }
    }
}
