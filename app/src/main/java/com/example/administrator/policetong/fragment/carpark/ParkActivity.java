package com.example.administrator.policetong.fragment.carpark;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.base.BaseActivity;
import com.example.administrator.policetong.base.Consts;
import com.example.administrator.policetong.bean.new_bean.CarParkBean;
import com.example.administrator.policetong.fragment.manage.Study_manage;
import com.example.administrator.policetong.network.DoNet;
import com.example.administrator.policetong.new_bean.StudyBean;
import com.example.administrator.policetong.utils.GsonUtil;
import com.example.administrator.policetong.view.NoDataOrNetError;

import org.xutils.http.RequestParams;

import java.util.List;

public class ParkActivity extends BaseActivity {


    private TextView mTitleName;
    private Toolbar mTlCustom;
    private RecyclerView mRecyclerView;
    private MyAdapter adapter;
    private View notDataView;
    private View NetErrorView;
    private int pageSize = 10;
    private int pageIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park);
        initView();
        setupToolBar(mTlCustom,false);
        mTitleName.setText("车辆列表");
        init();
        getNetData(true);
    }

    private void init() {
        adapter=new MyAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getNetData(false);
            }
        },mRecyclerView);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter mAdapter, View view, int position) {
                if (adapter.getData().get(position).getStatus()==0) {
                    showPopueWindow(position);
                }
            }
        });
    }


    public void getNetData(final boolean needDialog) {
        if (needDialog) {
            pageIndex = 1;
        }
        DoNet doNet = new DoNet() {
            @Override
            public void doWhat(String response, int id) {
                if (!GsonUtil.verifyResult_show(response)) {
                    return;
                }
                Log.e("doWhat: ", response);
                com.alibaba.fastjson.JSONObject json = JSON.parseObject(response).getJSONObject("data");
                if (json.getIntValue("total") == 0&&needDialog) {
                    adapter.setEmptyView(notDataView);
                    return;
                }
                com.alibaba.fastjson.JSONArray jsonArray = json.getJSONArray("data");
                List<CarParkBean> data = GsonUtil.parseJsonArrayWithGson(jsonArray.toString(), CarParkBean.class);
                adapter.setNewData(data);
                if (data==null||data.size()==0){
                    adapter.setEmptyView(notDataView);
                }else {
                    if (data.size() < pageSize) {
                        adapter.loadMoreEnd();
                    } else {
                        adapter.loadMoreComplete();
                        pageIndex++;
                    }
                }

            }
        };
        doNet.setOnErrorListener(new DoNet.OnErrorListener() {
            @Override
            public void onError(int code) {
                adapter.setEmptyView(NetErrorView);
            }
        });
        RequestParams requestParams = new RequestParams(Consts.URL_RCQWLIST);
        requestParams.addParameter("limit", pageSize);
        requestParams.addParameter("page", pageIndex);
        doNet.doGet(Consts.URL_VEHICLELIST, this, needDialog);
    }

    @SuppressLint("SetTextI18n")
    private void showPopueWindow(final int id) {
        final CarParkBean carParkBean = adapter.getData().get(id);
        View popView = View.inflate(this, R.layout.buttonpopwind_car_park, null);
        ((TextView) popView.findViewById(R.id.number)).setText(id + 1 + "");
        final PopupWindow popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.anim_menu_bottombar);
        popupWindow.setOutsideTouchable(true);
        popView.findViewById(R.id.tvItem1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent intent = new Intent(ParkActivity.this, ParkOutActivity.class);
                intent.putExtra("id",String.valueOf(adapter.getData().get(id).getId()));
                intent.putExtra("name",adapter.getData().get(id).getOwnername());
                intent.putExtra("carNo",adapter.getData().get(id).getPlateno());
                startActivityForResult(intent,200);

            }

        });
        popView.findViewById(R.id.tvItem2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                showPicture(carParkBean.getInpic().get(0),carParkBean.getInpic(),0);
            }
        });
        if (carParkBean.getOutpic()==null||carParkBean.getOutpic().size()==0){
            popView.findViewById(R.id.tvItem3).setVisibility(View.GONE);
        }else {
            popView.findViewById(R.id.tvItem3).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    showPicture(carParkBean.getOutpic().get(0),carParkBean.getOutpic(),0);
                }

            });
        }
        popView.findViewById(R.id.tvItem4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        popupWindow.showAtLocation(popView, Gravity.BOTTOM, 0, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==200) {
            if (resultCode==666){
               getNetData(true);
            }
        }
    }

    private void initView() {
        mTitleName =  findViewById(R.id.title_name);
        mTlCustom =  findViewById(R.id.tl_custom);
        mRecyclerView =  findViewById(R.id.mRecyclerView);
        NetErrorView = NoDataOrNetError.netError(mRecyclerView, this);
        NetErrorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNetData(true);
            }
        });
        notDataView=NoDataOrNetError.noData(mRecyclerView, this, "当前没有数据呦！");
    }

    private class MyAdapter extends BaseQuickAdapter<CarParkBean,BaseViewHolder> {


        public MyAdapter() {
            super(R.layout.car_park_layout_item);
        }

        @Override
        protected void convert(BaseViewHolder helper, CarParkBean bean) {
            helper.setText(R.id.tvTitle,bean.getOwnername());
            if (bean.getStatus()==0){
                helper.setText(R.id.tvStatus,"现在场");
                helper.setBackgroundColor(R.id.tvStatus,ParkActivity.this.getResources().getColor(R.color.colorMain));
            }else {
                helper.setText(R.id.tvStatus,"已出场");
                helper.setBackgroundColor(R.id.tvStatus,ParkActivity.this.getResources().getColor(R.color.colorRed));
                helper.setVisible(R.id.ll_car_park,true);
                helper.setText(R.id.tvItem9,"出场时间："+bean.getOuttime());
                helper.setText(R.id.tvItem10,"出场描述："+bean.getOutdesc());
                helper.setText(R.id.tvItem11,"出场经办理人员："+bean.getOutuid());
            }
            helper.setText(R.id.tvItem1,"入场时间："+bean.getIntime());
            helper.setText(R.id.tvItem2,"车主电话："+bean.getTel());
            helper.setText(R.id.tvItem3,"车牌号码："+bean.getPlateno());
            helper.setText(R.id.tvItem4,"发动机号码："+bean.getEngineno());
            helper.setText(R.id.tvItem5,"车架号："+bean.getFrameno());
            helper.setText(R.id.tvItem6,"扣车原因："+bean.getIndesc());
            helper.setText(R.id.tvItem7,"停车场："+bean.getParkid());
            helper.setText(R.id.tvItem8,"入场经办理人员："+bean.getInuid());
        }
    }
}
