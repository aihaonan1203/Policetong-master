package com.example.administrator.policetong.fragment.manage;


import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.activity.ModulesActivity;
import com.example.administrator.policetong.base.BaseActivity;
import com.example.administrator.policetong.base.BaseBean;
import com.example.administrator.policetong.base.BaseFragment;
import com.example.administrator.policetong.base.Consts;
import com.example.administrator.policetong.bean.PoliceConfirmed_bean;
import com.example.administrator.policetong.bean.PoliceMent;
import com.example.administrator.policetong.fragment.Fragment_manage;
import com.example.administrator.policetong.network.DoNet;
import com.example.administrator.policetong.network.Network;
import com.example.administrator.policetong.new_bean.JingBaoBean;
import com.example.administrator.policetong.new_bean.StudyBean;
import com.example.administrator.policetong.utils.GsonUtil;
import com.example.administrator.policetong.utils.LoadingDialog;
import com.example.administrator.policetong.view.NoDataOrNetError;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;


public class PoliceConfirmed_manage extends BaseFragment {

    List<PoliceConfirmed_bean> listbean;
    private RecyclerView mRecyclerView;
    private MyAdapter adapter;
    private View notDataView;
    private View NetErrorView;
    private int pageSize = 10;
    private int pageIndex = 1;

    public PoliceConfirmed_manage() {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_visitrectification_manage, container, false);
        ((TextView) Objects.requireNonNull(getActivity()).findViewById(R.id.title_name)).setText("警保卫台账列表");
        view.setClickable(true);
        initView(view);
        getNetData(true);
        return view;
    }

    public void getNetData(boolean needDialog) {
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
                if (json.getIntValue("total") == 0) {
                    adapter.setEmptyView(notDataView);
                    return;
                }
                com.alibaba.fastjson.JSONArray jsonArray = json.getJSONArray("data");
                List<PoliceConfirmed_bean> data = GsonUtil.parseJsonArrayWithGson(jsonArray.toString(), PoliceConfirmed_bean.class);
                if (data==null||data.size()==0){
                    adapter.setEmptyView(notDataView);
                }else {
                    adapter.addData(data);
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
        RequestParams requestParams = new RequestParams(Consts.URL_JBWLIST);
        requestParams.addParameter("limit", pageSize);
        requestParams.addParameter("page", pageIndex);
        doNet.doGet(requestParams.toString(), getActivity(), needDialog);
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
                WindowManager.LayoutParams lp = Objects.requireNonNull(getActivity()).getWindow().getAttributes();
                lp.alpha = 1.0f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
        //popupWindow出现屏幕变为半透明
        WindowManager.LayoutParams lp = Objects.requireNonNull(getActivity()).getWindow().getAttributes();
        lp.alpha = 0.5f;
        getActivity().getWindow().setAttributes(lp);
        popupWindow.showAtLocation(popView, Gravity.BOTTOM, 0, 0);

    }



    private void initView(View view) {
        notDataView= NoDataOrNetError.noData(mRecyclerView, getActivity(), "当前没有数据呦！");
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
        NetErrorView = NoDataOrNetError.netError(mRecyclerView, getActivity());
        NetErrorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNetData(true);
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

    private class MyAdapter extends BaseQuickAdapter<PoliceConfirmed_bean,BaseViewHolder> {


        public MyAdapter() {
            super(R.layout.visitrectification_item);
        }

        @Override
        protected void convert(BaseViewHolder helper, PoliceConfirmed_bean bean) {
            helper.setText(R.id.pc_item_txt1,"任务标题："+bean.getTitle());
            helper.setText(R.id.pc_item_txt2,"任务时间："+bean.getTask_time());
            helper.setText(R.id.pc_item_txt3,"到岗时间："+bean.getWork_time());
            String content = bean.getContent();
            helper.setText(R.id.pc_item_txt4,"任务详情：\n"+ content.replace("\\n","\n"));
            helper.setText(R.id.pc_item_txt5,"备注信息："+bean.getRemarks());
        }
    }
}
