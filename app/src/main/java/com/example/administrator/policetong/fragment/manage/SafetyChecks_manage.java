package com.example.administrator.policetong.fragment.manage;


import android.annotation.SuppressLint;
import android.os.Bundle;
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
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.base.BaseFragment;
import com.example.administrator.policetong.base.Consts;
import com.example.administrator.policetong.bean.Road_bean;
import com.example.administrator.policetong.bean.SafetyChecks_bean;
import com.example.administrator.policetong.network.DoNet;
import com.example.administrator.policetong.new_bean.AccidentBean;
import com.example.administrator.policetong.utils.GsonUtil;
import com.example.administrator.policetong.view.NoDataOrNetError;
import com.luck.picture.lib.entity.LocalMedia;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class SafetyChecks_manage extends BaseFragment {


    List<SafetyChecks_bean> listbean;
    private RecyclerView mRecyclerView;
    private MyAdapter adapter;
    private View notDataView;
    private int pageSize = 10;
    private int pageIndex = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_safety_checks_manage, container, false);
        ((TextView) Objects.requireNonNull(getActivity()).findViewById(R.id.title_name)).setText("安全排查台账");
        view.setClickable(true);
        initView(view);
        getNetData(true);
        return view;
    }

    public void getNetData(boolean needDialog) {
        DoNet doNet=new DoNet() {
            @Override
            public void doWhat(String response, int id) {
                if (!GsonUtil.verifyResult_show(response)){
                    return;
                }
                Log.e("doWhat: ",response);
                com.alibaba.fastjson.JSONArray jsonArray = JSON.parseObject(response).getJSONObject("data").getJSONArray("data");
                if (jsonArray==null||jsonArray.size()==0){
                    adapter.setEmptyView(notDataView);
                    return;
                }
                List<SafetyChecks_bean> data = GsonUtil.parseJsonArrayWithGson(jsonArray.toString(), SafetyChecks_bean.class);
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
        RequestParams requestParams = new RequestParams(Consts.URL_AQYHPCLIST);
        requestParams.addParameter("limit", pageSize);
        requestParams.addParameter("page", pageIndex);
        doNet.doGet(requestParams.toString() ,getActivity(),needDialog);
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
                SafetyChecks_bean bean = adapter.getData().get(id);
                String[] photos = bean.getPic().split(",");
                ArrayList<String> photoList = new ArrayList<>(Arrays.asList(photos));
                showManyPicture(photoList.get(0),photoList,0);
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
        listbean=new ArrayList<>();
        mRecyclerView =view.findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter=new MyAdapter();
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                showPopueWindow(position);
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getNetData(false);
            }
        }, mRecyclerView);
    }

    @Override
    public void getPhoto(List<LocalMedia> selectList) {

    }

    private class MyAdapter extends BaseQuickAdapter<SafetyChecks_bean,BaseViewHolder> {


        public MyAdapter() {
            super(R.layout.safetychecks_item);
        }

        @Override
        protected void convert(BaseViewHolder helper, SafetyChecks_bean bean) {
            helper.setText(R.id.pc_item_txt1,"日期："+bean.getCreate_time());
            helper.setText(R.id.pc_item_txt2,"街道："+bean.getBiroad_id());
            helper.setText(R.id.pc_item_txt3,"里程："+bean.getMileage());
            helper.setText(R.id.pc_item_txt4,"所属行政单位："+bean.getBiunitnature_id());
            helper.setText(R.id.pc_item_txt5,"发现单位："+bean.getBiorganization_id());
            helper.setText(R.id.pc_item_txt6,"是否报告："+bean.getSummit());
            helper.setText(R.id.pc_item_txt7,"是否整改："+bean.getReform());
            helper.setVisible(R.id.pc_item_txt9,true);
            helper.setText(R.id.pc_item_txt9,"整改详情："+bean.getDetail());
            helper.setText(R.id.pc_item_txt8,"整改日期："+bean.getReform_time());
        }
    }
}