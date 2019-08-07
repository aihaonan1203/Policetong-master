package com.example.administrator.policetong.fragment.manage;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.base.BaseFragment;
import com.example.administrator.policetong.base.Consts;
import com.example.administrator.policetong.bean.Daily_bean;
import com.example.administrator.policetong.network.DoNet;
import com.example.administrator.policetong.utils.GsonUtil;
import com.example.administrator.policetong.view.NoDataOrNetError;
import com.luck.picture.lib.entity.LocalMedia;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class DailyService_manage extends BaseFragment {
    List<Daily_bean> listbean;
    private RecyclerView mRecyclerView;
    private MyAdapter adapter;
    private View notDataView;
    private int pageSize=10;
    private int pageIndex=1;
    private View NetErrorView;

    public DailyService_manage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_path_parameter_manage, container, false);
        ((TextView) Objects.requireNonNull(getActivity()).findViewById(R.id.title_name)).setText("日常勤务目录");
        view.setClickable(true);
        initView(view);
        getNetData(true);
        return view;
    }

    public void getNetData(boolean needDialog) {
        if (needDialog){
            pageIndex=1;
        }
        DoNet doNet=new DoNet() {
            @Override
            public void doWhat(String response, int id) {
                if (!GsonUtil.verifyResult_show(response)){
                    return;
                }
                Log.e("doWhat: ",response);
                JSONObject json = JSON.parseObject(response).getJSONObject("data");
                if (json.getIntValue("total")==0){
                    adapter.setEmptyView(notDataView);
                    return;
                }
                com.alibaba.fastjson.JSONArray jsonArray = json.getJSONArray("data");
                List<Daily_bean> data = GsonUtil.parseJsonArrayWithGson(jsonArray.toString(), Daily_bean.class);
                adapter.setNewData(data);
                if (data.size()<pageSize){
                    adapter.loadMoreEnd();
                }else {
                    adapter.loadMoreComplete();
                    pageIndex++;
                }
            }
        };
        doNet.setOnErrorListener(new DoNet.OnErrorListener() {
            @Override
            public void onError(int code) {
                adapter.setEmptyView(NetErrorView);
            }
        });
        RequestParams requestParams=new RequestParams(Consts.URL_RCQWLIST);
        requestParams.addParameter("limit",pageSize);
        requestParams.addParameter("page",pageIndex);
        doNet.doGet(Consts.URL_RCQWLIST,getActivity(), needDialog);
    }



    @SuppressLint("SetTextI18n")
    private void showPopueWindow(final int id) {
        View popView = View.inflate(getActivity(), R.layout.buttonpopwind, null);
        Button bt_camera = popView.findViewById(R.id.btn_pop_camera);
        Button bt_cancle = popView.findViewById(R.id.btn_pop_cancel);
        ((TextView) popView.findViewById(R.id.number)).setText(id + 1 + "");
        final PopupWindow popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        //点击外部popueWindow消失
        popupWindow.setAnimationStyle(R.style.anim_menu_bottombar);
        popupWindow.setOutsideTouchable(true);
        bt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }

        });
        bt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        //popupWindow消失屏幕变为不透明
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1.0f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
        //popupWindow出现屏幕变为半透明
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.5f;
        getActivity().getWindow().setAttributes(lp);
        popupWindow.showAtLocation(popView, Gravity.BOTTOM, 0, 0);

    }



    private void initView(View view) {
        notDataView= NoDataOrNetError.noData(mRecyclerView, getActivity(), "当前没有数据呦！");
        NetErrorView = NoDataOrNetError.netError(mRecyclerView, getActivity());
        NetErrorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNetData(true);
            }
        });
        listbean=new ArrayList<>();
        mRecyclerView =view.findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter=new MyAdapter();
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                showPopueWindow(position);
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getNetData(false);
            }
        },mRecyclerView);
    }


    @Override
    public void getPhoto(List<LocalMedia> selectList) {

    }

    private class MyAdapter extends BaseQuickAdapter<Daily_bean,BaseViewHolder> {


        public MyAdapter() {
            super(R.layout.dailyservice_item);
        }

        @Override
        protected void convert(BaseViewHolder helper, Daily_bean bean) {
            helper.setText(R.id.ds_item_starttime,"开始时间："+bean.getStart_time());
            helper.setText(R.id.ds_item_endtime,"结束时间："+bean.getEnd_time());
            helper.setText(R.id.ds_item_jingli,"出动警力："+bean.getUsers_id());
            helper.setText(R.id.ds_item_paddr,"道路："+bean.getBiroad_id());
            helper.setText(R.id.ds_item_licheng,"里程/参照物："+bean.getReference());
            helper.setText(R.id.ds_item_qwtype,"勤务类型："+bean.getBidutytype_id());
            helper.setText(R.id.ds_item_adccwfxw,"重点查处违法行为："+bean.getBiillegalacts_id());
            helper.setText(R.id.ds_item_context,"简要内容："+bean.getContent());
            helper.setText(R.id.ds_item_time,"创建时间："+bean.getWork_time());
        }
    }
}
