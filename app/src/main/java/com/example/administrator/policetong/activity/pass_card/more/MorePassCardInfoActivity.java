package com.example.administrator.policetong.activity.pass_card.more;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.activity.pass_card.PassCardPhotoActivity;
import com.example.administrator.policetong.base.BaseActivity;
import com.example.administrator.policetong.base.Consts;
import com.example.administrator.policetong.bean.new_bean.MorePassBean;
import com.example.administrator.policetong.network.DoNet;
import com.example.administrator.policetong.utils.GsonUtil;
import com.example.administrator.policetong.utils.Utils;

public class MorePassCardInfoActivity extends BaseActivity {

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
    private BaseQuickAdapter<MorePassBean.CarDateBean,BaseViewHolder> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_pass_card_info);
        initView();
        init();
        initToolbar();
        getPassCard(true);
    }

    private void init() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter=new BaseQuickAdapter<MorePassBean.CarDateBean, BaseViewHolder>(R.layout.pass_card_info_item) {
            @Override
            protected void convert(BaseViewHolder helper, final MorePassBean.CarDateBean item) {
                helper.setText(R.id.tv_car_number,item.getCarno());
                helper.setText(R.id.tv_car_type,item.getHuowuname());
                helper.setText(R.id.icon_car_no,String.valueOf("序号："+item.getId()));
                helper.getView(R.id.tv_look_info).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle=new Bundle();
                        bundle.putInt("type",2);
                        bundle.putSerializable("data",item);
                        startActivity(new Intent(MorePassCardInfoActivity.this,PassCardPhotoActivity.class).putExtra("data",bundle));
                    }
                });
            }
        };
        View headView= LayoutInflater.from(this).inflate(R.layout.pass_card_info_head,mRecyclerView,false);
        initHeadView(headView);
        mAdapter.addHeaderView(headView);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void initHeadView(View view) {
        tv_pass_info_name =  view.findViewById(R.id.tv_pass_info_name);
        tv_pass_info_phone = view. findViewById(R.id.tv_pass_info_phone);
        tv_pass_info_wantTime =  view.findViewById(R.id.tv_pass_info_wantTime);
        tv_pass_info_startPoint =  view.findViewById(R.id.tv_pass_info_startPoint);
        tv_pass_info_endPoint =  view.findViewById(R.id.tv_pass_info_endPoint);
        tv_pass_info_applyTime =  view.findViewById(R.id.tv_pass_info_applyTime);
        tv_pass_info_status =  view.findViewById(R.id.tv_pass_info_status);
        tv_pass_info_result =  view.findViewById(R.id.tv_pass_info_result);
        tv_pass_info_resultTime =  view.findViewById(R.id.tv_pass_info_resultTime);
        tv_pass_info_resultPerson =  view.findViewById(R.id.tv_pass_info_resultPerson);
        tv_pass_info_zpTime =  view.findViewById(R.id.tv_pass_info_zpTime);
        tv_pass_info_point =  view.findViewById(R.id.tv_pass_info_point);
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
        tv_pass_info_name.setText(String.valueOf(cardInfo.getTxzform().getName()));
        tv_pass_info_phone.setText(String.valueOf(cardInfo.getTxzform().getPhone()));
        tv_pass_info_wantTime.setText(cardInfo.getTxzform().getWanttime());
        tv_pass_info_startPoint.setText(String.valueOf(cardInfo.getTxzform().getStartpoint()));
        tv_pass_info_endPoint.setText(String.valueOf(cardInfo.getTxzform().getEndpoint()));
        tv_pass_info_applyTime.setText(String.valueOf(Utils.stampToDate(Long.valueOf(cardInfo.getTxzform().getAddtime()))));
        if (cardInfo.getTxzform().getResult()==0){
            tv_pass_info_status.setText("正在审核");
        }else {
            tv_pass_info_status.setText("审核完毕");
            tv_pass_info_resultTime.setText(String.valueOf(Utils.stampToDate(cardInfo.getTxzform().getEndtime())));
            tv_pass_info_zpTime.setText(String.valueOf(Utils.stampToDate(cardInfo.getTxzform().getEndtime())));
            tv_pass_info_resultPerson.setText(cardInfo.getTxzform().getZprname());
            tv_pass_info_point.setText(cardInfo.getTxzform().getLinelist());
        }
        tv_pass_info_result.setText(Utils.getTextStatus(cardInfo.getTxzform().getResult()));
        mAdapter.addData(cardInfo.getCar_date());
    }

    private void initView() {
        title_name = findViewById(R.id.title_name);
        tl_custom = findViewById(R.id.tl_custom);
        mRecyclerView =  findViewById(R.id.mRecyclerView);
    }
}
