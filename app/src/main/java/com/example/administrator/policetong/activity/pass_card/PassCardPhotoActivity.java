package com.example.administrator.policetong.activity.pass_card;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.base.BaseActivity;
import com.example.administrator.policetong.bean.new_bean.MorePassBean;
import com.example.administrator.policetong.bean.new_bean.PassCardInfo;
import com.example.administrator.policetong.bean.new_bean.PhotoBean;
import com.example.administrator.policetong.utils.imageloader.ILFactory;
import com.example.administrator.policetong.utils.imageloader.ILoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PassCardPhotoActivity extends BaseActivity {

    private TextView title_name;
    private Toolbar tl_custom;
    private RecyclerView mRecyclerView;
    private BaseQuickAdapter<PhotoBean,BaseViewHolder> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_card_photo);
        initView();
        initToolbar();
        int type = getIntent().getBundleExtra("data").getInt("type");
        if (type==1) {
            PassCardInfo passCardInfo = (PassCardInfo) getIntent().getBundleExtra("data").getSerializable("data");
            init(passCardInfo);
        }else {
            MorePassBean.CarDateBean passCardInfo = (MorePassBean.CarDateBean) getIntent().getBundleExtra("data").getSerializable("data");
            init(passCardInfo);
        }

    }

    private void init(MorePassBean.CarDateBean passCardInfo) {
        if (passCardInfo==null){
            return;
        }
        List<PhotoBean> photoBeans=new ArrayList<>();
        photoBeans.add(new PhotoBean("驾驶证照片",passCardInfo.getJszpic()));
        photoBeans.add(new PhotoBean("行驶证照片",passCardInfo.getXszpic()));
        photoBeans.add(new PhotoBean("保险照片",passCardInfo.getBxpic()));
        photoBeans.add(new PhotoBean("车前侧照片",passCardInfo.getClpic()));
        photoBeans.add(new PhotoBean("车后侧照片",passCardInfo.getCwpic()));
        photoBeans.add(new PhotoBean("车左侧照片",passCardInfo.getZcpic()));
        photoBeans.add(new PhotoBean("车右侧照片",passCardInfo.getYcpic()));
        photoBeans.add(new PhotoBean("车内部照片",passCardInfo.getNbpic()));
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        mAdapter=new BaseQuickAdapter<PhotoBean, BaseViewHolder>(R.layout.photo_layout_item) {
            @Override
            protected void convert(BaseViewHolder helper, PhotoBean item) {
                helper.setText(R.id.tv_name,item.getName());
                ILFactory.getLoader().loadNet((ImageView) helper.getView(R.id.iv_photo),String.valueOf("https://pic.jjedd.net:9000/"+item.getImage()),new ILoader.Options(R.drawable.bg_loading, R.drawable.empty_img));
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setNewData(photoBeans);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                showPicture("https://pic.jjedd.net:9000/"+mAdapter.getData().get(position).getImage(),null,0);
            }
        });
    }

    private void init(PassCardInfo passCardInfo) {
        if (passCardInfo==null){
            return;
        }
        List<PhotoBean> photoBeans=new ArrayList<>();
        photoBeans.add(new PhotoBean("驾驶证照片",passCardInfo.getJszpic()));
        photoBeans.add(new PhotoBean("行驶证照片",passCardInfo.getXszpic()));
        photoBeans.add(new PhotoBean("保险照片",passCardInfo.getBxpic()));
        photoBeans.add(new PhotoBean("车前侧照片",passCardInfo.getClpic()));
        photoBeans.add(new PhotoBean("车后侧照片",passCardInfo.getCwpic()));
        photoBeans.add(new PhotoBean("车左侧照片",passCardInfo.getZcpic()));
        photoBeans.add(new PhotoBean("车右侧照片",passCardInfo.getYcpic()));
        photoBeans.add(new PhotoBean("车内部照片",passCardInfo.getNbpic()));
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        mAdapter=new BaseQuickAdapter<PhotoBean, BaseViewHolder>(R.layout.photo_layout_item) {
            @Override
            protected void convert(BaseViewHolder helper, PhotoBean item) {
                helper.setText(R.id.tv_name,item.getName());
                ILFactory.getLoader().loadNet((ImageView) helper.getView(R.id.iv_photo),String.valueOf("https://pic.jjedd.net:9000/"+item.getImage()),new ILoader.Options(R.drawable.bg_loading, R.drawable.empty_img));
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setNewData(photoBeans);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                showPicture("https://pic.jjedd.net:9000/"+mAdapter.getData().get(position).getImage(),null,0);
            }
        });
    }

    private void initToolbar() {
        setupToolBar(tl_custom, false);
        title_name.setText("照片详情");
    }

    private void initView() {
        title_name = findViewById(R.id.title_name);
        tl_custom = findViewById(R.id.tl_custom);
        mRecyclerView =  findViewById(R.id.mRecyclerView);
    }
}
