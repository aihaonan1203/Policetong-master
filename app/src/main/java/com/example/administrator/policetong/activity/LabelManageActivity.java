package com.example.administrator.policetong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.base.App;
import com.example.administrator.policetong.base.BaseActivity;
import com.example.administrator.policetong.base.Consts;
import com.example.administrator.policetong.network.DoNet;
import com.example.administrator.policetong.new_bean.DepBean;
import com.example.administrator.policetong.utils.GsonUtil;
import com.example.administrator.policetong.utils.UIUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LabelManageActivity extends BaseActivity {

    private TextView title_name;
    private Toolbar tl_custom;
    private RecyclerView mRecyclerView;
    private BaseQuickAdapter<DepBean,BaseViewHolder> adapter;
    private List<String> tagIdList;
    private Button btn_sbmit;
    private String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label_manage);
        type = getIntent().getStringExtra("type");
        if (type.equals("1")){
            tagIdList= getIntent().getStringArrayListExtra("userIdList");
        }
        if (tagIdList==null) {
            tagIdList=new ArrayList<>();
        }
        initView();
        initToolbar();
        init();
        getPageTagList();
    }


    private void init() {
        adapter = new BaseQuickAdapter<DepBean,BaseViewHolder>(R.layout.layout_label_item) {
            @Override
            protected void convert(BaseViewHolder helper, DepBean item) {
                if (type.equals("1")){
                    helper.setText(R.id.tv_tagName,item.getTruename());
                }else if (type.equals("2")){
                    helper.setText(R.id.tv_tagName,item.getName());
                }
                if (tagIdList.contains(String.valueOf(item.getUid()))) {
                    item.setCheck(true);
                    tagIdList.remove(String.valueOf(item.getUid()));
                }
                if (item.isCheck()){
                    helper.setChecked(R.id.cb_select,true);
                }else {
                    helper.setChecked(R.id.cb_select,false);
                }
            }
        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter mAdapter, View view, int position) {
                DepBean bean = adapter.getData().get(position);
                if (adapter.getData().get(position).isCheck()){
                    bean.setCheck(false);
                }else {
                    bean.setCheck(true);
                }
                adapter.notifyItemChanged(position);
            }
        });
        btn_sbmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> idList=new ArrayList<>();
                ArrayList<String> nameList=new ArrayList<>();
                if (type.equals("1")){
                    for (int i = 0; i < adapter.getData().size(); i++) {
                        DepBean bean = adapter.getData().get(i);
                        if (bean.isCheck()){
                            idList.add(bean.getUid()+"");
                            nameList.add(bean.getTruename()+"");
                        }
                    }
                    Intent intent=new Intent();
                    intent.putExtra("idList", idList);
                    intent.putExtra("nameList", nameList);
                    setResult(666,intent);
                }else {
                    for (int i = 0; i < adapter.getData().size(); i++) {
                        DepBean bean = adapter.getData().get(i);
                        if (bean.isCheck()){
                            idList.add(bean.getId()+"");
                            nameList.add(bean.getName()+"");
                        }
                    }
                    Intent intent=new Intent();
                    intent.putExtra("idList", idList);
                    intent.putExtra("nameList", nameList);
                    setResult(999,intent);
                }
                if (idList.size()==0||nameList.size()==0){
                    UIUtils.t("不得为空，请选择",false,UIUtils.T_WARNING);
                    return;
                }
                finish();
            }
        });
    }

    private void initToolbar() {
        setupToolBar(tl_custom, false);
        title_name.setText("请选择");
    }

    private void initView() {
        title_name = findViewById(R.id.title_name);
        tl_custom = findViewById(R.id.tl_custom);
        btn_sbmit = findViewById(R.id.btn_create);
        mRecyclerView = findViewById(R.id.mRecyclerView);
    }

    public void getPageTagList() {

        DoNet doNet=new DoNet() {
            @Override
            public void doWhat(String response, int id) {
                if (!GsonUtil.verifyResult_show(response)) {
                    return;
                }
                List<DepBean> pointBeans = GsonUtil.parseJsonArrayWithGson(JSON.parseObject(response).getJSONArray("data").toString(), DepBean.class);
                if (pointBeans.size()!=0){
                    adapter.setNewData(pointBeans);
                }
            }
        };
        if (type.equals("1")){
            doNet.doGet(Consts.COMMOM_URL+"getDptUsers?dpt_id="+ App.userInfo.getUser().getDpt_id(),this,true);
        }else {
            doNet.doGet(Consts.COMMOM_URL+"biIllegalacts",this,true);
        }
    }


}
