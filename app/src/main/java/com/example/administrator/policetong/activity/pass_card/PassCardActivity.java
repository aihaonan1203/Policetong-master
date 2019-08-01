package com.example.administrator.policetong.activity.pass_card;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.base.BaseActivity;
import com.example.administrator.policetong.base.Consts;
import com.example.administrator.policetong.bean.new_bean.PassCardBean;
import com.example.administrator.policetong.network.DoNet;
import com.example.administrator.policetong.utils.GsonUtil;
import com.example.administrator.policetong.utils.Utils;

import java.util.List;

import retrofit2.http.PATCH;

public class PassCardActivity extends BaseActivity {

    private TextView title_name;
    private Toolbar tl_custom;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private BaseQuickAdapter<PassCardBean,BaseViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_card);
        initView();
        initToolbar();
        initSwipeRefreshLayout(mSwipeRefreshLayout,false);
        getPassList(true);
        init();
    }

    private void init() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new BaseQuickAdapter<PassCardBean,BaseViewHolder>(R.layout.pass_card_layout) {
            @Override
            protected void convert(BaseViewHolder helper, PassCardBean item) {
                helper.setText(R.id.tv_pass_card_name,String.valueOf(item.getName()));
                helper.setText(R.id.tv_pass_card_phone,String.valueOf(item.getPhone()));
                helper.setText(R.id.tv_pass_card_want_time,String.valueOf(Utils.stampToDate(item.getWanttime())));
                helper.setText(R.id.tv_pass_card_start,String.valueOf(item.getStartpoint()));
                helper.setText(R.id.tv_pass_card_end,String.valueOf(item.getEndpoint()));
                if (item.getResult()==1){
                    helper.setText(R.id.tv_pass_card_time,String.valueOf(Utils.stampToDate(item.getTxstarttime())+"-"+Utils.stampToDate(item.getTxendtime())));
                }else {
                    helper.setText(R.id.tv_pass_card_time,"");
                }
                Utils.setTextStatus(item.getResult(),((TextView)helper.getView(R.id.tv_pass_card_status)));
            }
        };
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter mAdapter, View view, int position) {
                startActivity(new Intent(PassCardActivity.this,PassCardInfoActivity.class).putExtra("card_id",String.valueOf(adapter.getData().get(position).getId())));
            }
        });
    }

    private void initToolbar() {
        setupToolBar(tl_custom, false);
        title_name.setText("单通行证审核");
    }

    private void initView() {
        title_name = findViewById(R.id.title_name);
        tl_custom = findViewById(R.id.tl_custom);
        mRecyclerView =  findViewById(R.id.mRecyclerView);
        mSwipeRefreshLayout =  findViewById(R.id.mSwipeRefreshLayout);
    }

    @Override
    protected void onRefreshLoadData() {
        super.onRefreshLoadData();
        getPassList(false);
    }

    public void getPassList(boolean needDialog) {
        DoNet doNet=new DoNet() {
            @Override
            public void doWhat(String response, int id) {
                if (mSwipeRefreshLayout.isRefreshing()){
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                if (!GsonUtil.verifyResult_show(response)){
                    return;
                }
                List<PassCardBean> passCardBeans = GsonUtil.parseJsonArrayWithGson(JSON.parseObject(response).getJSONObject("data").getJSONArray("list").toString(), PassCardBean.class);
                Log.e("doWhat: ",passCardBeans.size()+"" );
                adapter.setNewData(passCardBeans);
            }
        };
        doNet.setOnErrorListener(new DoNet.OnErrorListener() {
            @Override
            public void onError() {

            }
        });
        doNet.doGet(Consts.URL_ONE_LIST ,this,needDialog);
    }
}
