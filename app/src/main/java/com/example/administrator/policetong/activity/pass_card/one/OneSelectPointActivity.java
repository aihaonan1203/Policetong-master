package com.example.administrator.policetong.activity.pass_card.one;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.base.BaseActivity;
import com.example.administrator.policetong.base.Consts;
import com.example.administrator.policetong.bean.new_bean.PassCardBean;
import com.example.administrator.policetong.bean.new_bean.PointBean;
import com.example.administrator.policetong.network.DoNet;
import com.example.administrator.policetong.utils.GsonUtil;
import com.example.administrator.policetong.utils.UIUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OneSelectPointActivity extends BaseActivity implements View.OnClickListener {


    private TextView title_name;
    private Toolbar tl_custom;
    private RecyclerView mRecyclerView;
    private Button btn_create;
    private BaseQuickAdapter<PointBean,BaseViewHolder> adapter;
    private List<String> idList=new ArrayList<>();
    private List<String> addList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_point);
        String id = getIntent().getStringExtra("id");
        if (!id.isEmpty()){
            String[] ids = id.split(",");
            idList=new ArrayList<>(Arrays.asList(ids));
            addList.addAll(idList);
        }
        initView();
        initToolbar();
        init();
        getPointList(true);
    }


    private void initToolbar() {
        setupToolBar(tl_custom, false);
        title_name.setText("道路选择");
    }

    private void init() {
        adapter=new BaseQuickAdapter<PointBean, BaseViewHolder>(R.layout.layout_label_item) {
            @Override
            protected void convert(BaseViewHolder helper, final PointBean item) {
                helper.setText(R.id.tv_tagName,item.getName());
                if (idList.contains(item.getId()+"")){
                    item.setCheck(true);
                    idList.remove(item.getId()+"");
                }
                if (item.isCheck()){
                    ((CheckBox)helper.getView(R.id.cb_select)).setChecked(true);
                }else {
                    ((CheckBox)helper.getView(R.id.cb_select)).setChecked(false);
                }
                helper.getView(R.id.cb_select).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.isCheck()){
                            item.setCheck(false);
                            addList.remove(item.getId()+"");
                        }else {
                            item.setCheck(true);
                            addList.add(item.getId()+"");
                        }
                    }
                });
            }
        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter Adapter, View view, int position) {
                if (adapter.getData().get(position).isCheck()) {
                    adapter.getData().get(position).setCheck(false);
                    addList.remove(adapter.getData().get(position).getId()+"");
                }else {
                    adapter.getData().get(position).setCheck(true);
                    addList.add(adapter.getData().get(position).getId()+"");
                }
                adapter.notifyItemChanged(position);
            }
        });
    }


    public void getPointList(boolean needDialog) {
        DoNet doNet = new DoNet() {
            @Override
            public void doWhat(String response, int id) {
                if (!GsonUtil.verifyResult_show(response)) {
                    return;
                }
                Log.e("doWhat: ", response);
                List<PointBean> pointBeans = GsonUtil.parseJsonArrayWithGson(JSON.parseObject(response).getJSONArray("data").toString(), PointBean.class);
                adapter.setNewData(pointBeans);
            }
        };
        doNet.doGet(Consts.URL_ROADLISt, this, needDialog);
    }

    private void initView() {
        title_name =  findViewById(R.id.title_name);
        tl_custom =  findViewById(R.id.tl_custom);
        mRecyclerView =  findViewById(R.id.mRecyclerView);
        btn_create =  findViewById(R.id.btn_create);

        btn_create.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create:
                StringBuilder names=new StringBuilder();
                StringBuilder ids=new StringBuilder();

                for (int i = 0; i < addList.size(); i++) {
                    for (int j = 0; j < adapter.getData().size(); j++) {
                        if (String.valueOf(adapter.getData().get(j).getId()).equals(addList.get(i))){
                            names.append(adapter.getData().get(j).getName()).append("➡");
                            ids.append(adapter.getData().get(j).getId()).append(",");
                        }
                    }
                }
                if (names.length()==0){
                    UIUtils.t("请选择道路！",false,UIUtils.T_WARNING);
                    return;
                }
                names.deleteCharAt(names.length()-1);
                ids.deleteCharAt(ids.length()-1);
                Intent intent=new Intent();
                intent.putExtra("name",names.toString());
                intent.putExtra("id",ids.toString());
                setResult(666,intent);
                finish();
                break;
        }
    }
}
