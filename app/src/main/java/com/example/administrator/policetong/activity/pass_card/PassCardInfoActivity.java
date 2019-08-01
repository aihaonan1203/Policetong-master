package com.example.administrator.policetong.activity.pass_card;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.base.BaseActivity;
import com.example.administrator.policetong.base.Consts;
import com.example.administrator.policetong.bean.new_bean.PassCardInfo;
import com.example.administrator.policetong.network.DoNet;
import com.example.administrator.policetong.utils.GsonUtil;
import com.example.administrator.policetong.utils.Utils;

public class PassCardInfoActivity extends BaseActivity {

    private TextView title_name;
    private Toolbar tl_custom;
    private TextView tv_pass_info_name;
    private TextView tv_pass_info_phone;
    private TextView tv_pass_info_wantTime;
    private TextView tv_pass_info_startPoint;
    private TextView tv_pass_info_endPoint;
    private TextView tv_pass_info_applyTime;
    private TextView tv_pass_info_status;
    private TextView tv_pass_info_result;
    private TextView tv_pass_info_resultTime;
    private TextView tv_pass_info_resultPerson;
    private TextView tv_pass_info_zpTime;
    private TextView tv_pass_info_point;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_card_info);
        initView();
        initToolbar();
        getPassCard(true);
    }


    private void initToolbar() {
        setupToolBar(tl_custom, false);
        title_name.setText("通行证详情");
    }

    public void getPassCard(boolean needDialog) {
        DoNet doNet = new DoNet() {
            @Override
            public void doWhat(String response, int id) {
                Log.e("doWhat: ", response);
                if (!GsonUtil.verifyResult_show(response)){
                    return;
                }
                PassCardInfo cardInfo = GsonUtil.parseJsonWithGson(JSON.parseObject(response).getJSONObject("data").getJSONObject("info").toString(), PassCardInfo.class);
                refresh(cardInfo);
            }
        };
        doNet.setOnErrorListener(new DoNet.OnErrorListener() {
            @Override
            public void onError() {

            }
        });
        doNet.doGet(Consts.URL_ONEDETAIL + getIntent().getStringExtra("card_id"), this, needDialog);
    }

    private void refresh(PassCardInfo cardInfo) {
        tv_pass_info_name.setText(String.valueOf(cardInfo.getName()));
        tv_pass_info_phone.setText(String.valueOf(cardInfo.getPhone()));
        tv_pass_info_wantTime.setText(String.valueOf(Utils.stampToDate(cardInfo.getWanttime())));
        tv_pass_info_startPoint.setText(String.valueOf(cardInfo.getStartpoint()));
        tv_pass_info_endPoint.setText(String.valueOf(cardInfo.getEndpoint()));
        tv_pass_info_applyTime.setText(String.valueOf(Utils.stampToDate(Long.valueOf(cardInfo.getAddtime()))));
        if (cardInfo.getResult()==0){
            tv_pass_info_status.setText("正在审核");
        }else {
            tv_pass_info_status.setText("审核完毕");
            tv_pass_info_resultTime.setText(String.valueOf(Utils.stampToDate(cardInfo.getEndtime())));
            tv_pass_info_zpTime.setText(String.valueOf(Utils.stampToDate(cardInfo.getEndtime())));
            tv_pass_info_resultPerson.setText(cardInfo.getZprname());
            tv_pass_info_point.setText(cardInfo.getLinelist());
        }
        tv_pass_info_result.setText(Utils.getTextStatus(cardInfo.getResult()));

    }

    private void initView() {
        title_name = findViewById(R.id.title_name);
        tl_custom = findViewById(R.id.tl_custom);
        tv_pass_info_name =  findViewById(R.id.tv_pass_info_name);
        tv_pass_info_phone =  findViewById(R.id.tv_pass_info_phone);
        tv_pass_info_wantTime =  findViewById(R.id.tv_pass_info_wantTime);
        tv_pass_info_startPoint =  findViewById(R.id.tv_pass_info_startPoint);
        tv_pass_info_endPoint =  findViewById(R.id.tv_pass_info_endPoint);
        tv_pass_info_applyTime =  findViewById(R.id.tv_pass_info_applyTime);
        tv_pass_info_status =  findViewById(R.id.tv_pass_info_status);
        tv_pass_info_result =  findViewById(R.id.tv_pass_info_result);
        tv_pass_info_resultTime =  findViewById(R.id.tv_pass_info_resultTime);
        tv_pass_info_resultPerson =  findViewById(R.id.tv_pass_info_resultPerson);
        tv_pass_info_zpTime =  findViewById(R.id.tv_pass_info_zpTime);
        tv_pass_info_point =  findViewById(R.id.tv_pass_info_point);
        mRecyclerView =  findViewById(R.id.mRecyclerView);
    }
}
