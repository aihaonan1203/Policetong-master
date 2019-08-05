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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.base.BaseFragment;
import com.example.administrator.policetong.base.Consts;
import com.example.administrator.policetong.bean.SafetyChecks_bean;
import com.example.administrator.policetong.network.DoNet;
import com.example.administrator.policetong.new_bean.VisitBean;
import com.example.administrator.policetong.utils.GsonUtil;
import com.example.administrator.policetong.view.NoDataOrNetError;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class VisitRectification_manage extends BaseFragment {

    List<VisitBean> listbean;
    private RecyclerView mRecyclerView;
    private MyAdapter adapter;
    private View notDataView;

    public VisitRectification_manage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_visitrectification_manage, container, false);
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
                List<VisitBean> data = GsonUtil.parseJsonArrayWithGson(jsonArray.toString(), VisitBean.class);
                if (data.size()==0){
                    adapter.setEmptyView(notDataView);
                    return;
                }
                adapter.setNewData(data);
            }
        };
        doNet.doGet(Consts.URL_ZFXCZGLIST ,getActivity(),needDialog);
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
    }


    @Override
    public void getPhoto(List<LocalMedia> selectList) {

    }

    private class MyAdapter extends BaseQuickAdapter<VisitBean,BaseViewHolder> {


        public MyAdapter() {
            super(R.layout.visitrectification_item);
        }

        @Override
        protected void convert(BaseViewHolder helper, VisitBean bean) {
            helper.setText(R.id.pc_item_txt1,"走访单位："+bean.getZfunit());
            helper.setText(R.id.pc_item_txt2,"单位性质："+bean.getBiunitnature_id());
            helper.setText(R.id.pc_item_txt3,"走访目的："+bean.getBivisitpurpose_id());
            helper.setText(R.id.pc_item_txt4,"简要内容："+bean.getDetail());
            helper.setText(R.id.pc_item_txt5,"走访时间："+bean.getCreate_time());
        }
    }
}
