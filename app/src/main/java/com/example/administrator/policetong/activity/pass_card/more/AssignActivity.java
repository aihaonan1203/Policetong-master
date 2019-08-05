package com.example.administrator.policetong.activity.pass_card.more;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.base.BaseActivity;
import com.example.administrator.policetong.base.Consts;
import com.example.administrator.policetong.bean.new_bean.PointBean;
import com.example.administrator.policetong.network.DoNet;
import com.example.administrator.policetong.utils.GsonUtil;
import com.example.administrator.policetong.utils.UIUtils;
import com.example.administrator.policetong.view.ClearWriteEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssignActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTitleName;
    private Toolbar mTlCustom;
    private TextView mTvName;
    private TextView mTvPhone;
    private TextView mTvWx;
    private Spinner mMSpinner;
    private ClearWriteEditText mEtYj;
    private Button mBtnSubmit;
    private List<PointBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign);
        initView();
        initToolbar();
        init();
        getDpt(true);
    }

    public void getDpt(boolean needDialog) {
        DoNet doNet = new DoNet() {
            @Override
            public void doWhat(String response, int id) {
                Log.e("doWhat: ", response);
                if (!GsonUtil.verifyResult_show(response)){
                    return;
                }
                list = GsonUtil.parseJsonArrayWithGson(JSON.parseObject(response).getJSONObject("data").getJSONArray("info").toString(), PointBean.class);
                List<Map<String,String>> data=new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    Map<String,String> map=new HashMap<>();
                    map.put("name", list.get(i).getName());
                    data.add(map);
                }
                SimpleAdapter simpleAdapter=new SimpleAdapter(AssignActivity.this,data,R.layout.sipper_layout_dpt_item,new String[]{"name"},new int[]{R.id.tv_name});
                mMSpinner.setAdapter(simpleAdapter);
            }
        };
        doNet.setOnErrorListener(new DoNet.OnErrorListener() {
            @Override
            public void onError(int code) {

            }
        });
        doNet.doGet(Consts.URL_GETDPT, this, needDialog);
    }


    private void init() {
        mTvName.setText(getIntent().getStringExtra("name"));
        mTvPhone.setText(getIntent().getStringExtra("phone"));
        mTvWx.setText(getIntent().getStringExtra("wxId"));
    }

    private void initToolbar() {
        setupToolBar(mTlCustom, false);
        mTitleName.setText("指派中队");
    }

    private void initView() {
        mTitleName =  findViewById(R.id.title_name);
        mTlCustom =  findViewById(R.id.tl_custom);
        mTvName =  findViewById(R.id.tv_name);
        mTvPhone =  findViewById(R.id.tv_phone);
        mTvWx =  findViewById(R.id.tv_wx);
        mMSpinner =  findViewById(R.id.mSpinner);
        mEtYj =  findViewById(R.id.et_yj);
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
                jsonObject.put("fqdesc",mEtYj.getText().toString().trim());
                jsonObject.put("zxbm",list.get(mMSpinner.getSelectedItemPosition()).getId() );
                doNet.doPost(jsonObject,Consts.URL_ZHIPAI, this, true);
                break;
        }
    }
}
