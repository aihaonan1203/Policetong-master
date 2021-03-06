package com.example.administrator.policetong.activity.pass_card.more;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.activity.pass_card.one.OneCheckPassCardActivity;
import com.example.administrator.policetong.base.BaseActivity;
import com.example.administrator.policetong.base.Consts;
import com.example.administrator.policetong.bean.new_bean.PassCardBean;
import com.example.administrator.policetong.network.DoNet;
import com.example.administrator.policetong.utils.GsonUtil;
import com.example.administrator.policetong.utils.Utils;
import com.example.administrator.policetong.view.NoDataOrNetError;

import org.xutils.http.RequestParams;

import java.util.List;

public class MorePassCardListActivity extends BaseActivity {

    private TextView title_name;
    private Toolbar tl_custom;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private BaseQuickAdapter<PassCardBean,BaseViewHolder> adapter;
    private View notDataView;
    private View NetErrorView;
    private int pageSize = 10;
    private int pageIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_pass_card_list);
        initView();
        initToolbar();
        initSwipeRefreshLayout(mSwipeRefreshLayout,false);
        getPassList(true);
        init();

    }

    private void init() {
        notDataView = NoDataOrNetError.noData(mRecyclerView, this, "暂时没有通行证数据呦！");
        NetErrorView = NoDataOrNetError.netError(mRecyclerView, this);
        NetErrorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPassList(true);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new BaseQuickAdapter<PassCardBean,BaseViewHolder>(R.layout.pass_card_layout) {
            @Override
            protected void convert(BaseViewHolder helper, final PassCardBean item) {
                helper.setText(R.id.tv_pass_card_name,String.valueOf(item.getName()));
                helper.setText(R.id.tv_pass_card_phone,String.valueOf(item.getPhone()));
                helper.setText(R.id.tv_pass_card_want_time,item.getWanttime());
                helper.setText(R.id.tv_pass_card_start,String.valueOf(item.getStartpoint()));
                helper.setText(R.id.tv_pass_card_end,String.valueOf(item.getEndpoint()));
                if (item.getStatus()==0){
                    helper.setImageResource(R.id.iv_check,R.drawable.icon_zhipai);
                }else if (item.getStatus()==3){
                    helper.setGone(R.id.iv_check,false);
                }else{
                    helper.setImageResource(R.id.iv_check,R.drawable.icon_sh);
                }
                helper.getView(R.id.iv_check).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (item.getStatus()){
                            case 0:
                                startActivityForResult(new Intent(MorePassCardListActivity.this,AssignActivity.class)
                                        .putExtra("name",item.getName())
                                        .putExtra("phone",item.getPhone())
                                        .putExtra("id",item.getId()+"")
                                        .putExtra("wxId",item.getWx_uid()),200);
                                break;
                            case 1:
                                startActivityForResult(new Intent(MorePassCardListActivity.this,MoreCheckActivity.class)
                                        .putExtra("name",item.getName())
                                        .putExtra("phone",item.getPhone())
                                        .putExtra("id",item.getId()+""),200);
                                break;
                            case 2:
                                startActivityForResult(new Intent(MorePassCardListActivity.this,SureCheckActivity.class)
                                        .putExtra("id",item.getId()+""),200);
                                break;
                            case 3:
                                break;
                        }
                    }
                });
                if (item.getResult()==1||item.getResult()==2){
                    helper.setText(R.id.tv_pass_card_time,item.getTxstarttime()+"-"+item.getTxendtime());
                    helper.getView(R.id.iv_check).setVisibility(View.GONE);
                }else {
                    helper.setText(R.id.tv_pass_card_time,"");
                    helper.getView(R.id.iv_check).setVisibility(View.VISIBLE);
                }
                Utils.setTextResult(item.getResult(),((TextView)helper.getView(R.id.tv_pass_card_status)));
                Utils.setTextStatus(item.getStatus(),((TextView)helper.getView(R.id.tv_pass_card_result)));
            }
        };
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter mAdapter, View view, int position) {
                startActivity(new Intent(MorePassCardListActivity.this,MorePassCardInfoActivity.class).putExtra("id",String.valueOf(adapter.getData().get(position).getId())));
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getPassList(false);
            }
        }, mRecyclerView);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPassList(true);
            }
        });
    }

    private void initToolbar() {
        setupToolBar(tl_custom, false);
        title_name.setText("批量通行证列表");
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

    public void getPassList(final boolean needDialog) {
        if (needDialog) {
            pageIndex = 1;
        }
        DoNet doNet=new DoNet() {
            @Override
            public void doWhat(String response, int id) {
                if (mSwipeRefreshLayout.isRefreshing()){
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                if (!GsonUtil.verifyResult_show(response)){
                    return;
                }
                Log.e("doWhat: ",response);
                JSONArray jsonArray = JSON.parseObject(response).getJSONObject("data").getJSONArray("list");
                if (jsonArray==null||jsonArray.size()==0){
                    adapter.setEmptyView(notDataView);
                    return;
                }
                List<PassCardBean> passCardBeans = GsonUtil.parseJsonArrayWithGson(jsonArray.toString(), PassCardBean.class);
                if (passCardBeans==null||passCardBeans.size()==0){
                    adapter.setEmptyView(notDataView);
                }else {
                    if (needDialog){
                        adapter.setNewData(passCardBeans);
                        if (passCardBeans.size() < pageSize){
                            adapter.loadMoreEnd();
                        }else {
                            pageIndex++;
                        }
                    }else {
                        adapter.addData(passCardBeans);
                        if (passCardBeans.size() < pageSize) {
                            adapter.loadMoreEnd();
                        } else {
                            adapter.loadMoreComplete();
                            pageIndex++;
                        }
                    }
                }
            }
        };
        doNet.setOnErrorListener(new DoNet.OnErrorListener() {
            @Override
            public void onError(int code) {
                if (code==400){
                    adapter.setEmptyView(NoDataOrNetError.noData(mRecyclerView, MorePassCardListActivity.this, "您没有审核通行证的权限呦！"));
                }else {
                    adapter.setEmptyView(NetErrorView);
                }
            }
        });
        RequestParams requestParams = new RequestParams(Consts.URL_TXZLIST);
        requestParams.addParameter("limit", pageSize);
        requestParams.addParameter("page", pageIndex);
        doNet.doGet(requestParams.toString(), this, needDialog);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            if (resultCode == 666) {
                getPassList(true);
            }
        }
    }
}
