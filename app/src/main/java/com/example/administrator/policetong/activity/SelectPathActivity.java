package com.example.administrator.policetong.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.policetong.R;
import com.example.administrator.policetong.base.BaseActivity;
import com.example.administrator.policetong.bean.EvenMsg;
import com.example.administrator.policetong.bean.PathBean;
import com.example.administrator.policetong.utils.LoadingDialog;
import com.example.administrator.policetong.utils.Util;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class SelectPathActivity extends BaseActivity {

    private ImageView acArrowBack;
    private ListView rvRecyclerView;
    private TextView acTvOk;
    private List<PathBean> pathBeans=new ArrayList<>();
    private List<PathBean> mData=new ArrayList<>();
    private RevyZltjAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_path);
        initView();
        initData();
    }

    private void initData() {
        LoadingDialog.showDialog(this, "正在获取道路信息...");
        Util.RequestOption(this, "option5", new Util.RequestOptionCallBack() {
            @Override
            public void CallBack(List<String> list) {
                LoadingDialog.disDialog();
                for (int i = 0; i < 20; i++) {
                    pathBeans.add(new PathBean(i+"",list.get(0)+i,false));
                }
                adapter = new RevyZltjAdapter(SelectPathActivity.this, pathBeans);
                rvRecyclerView.setAdapter(adapter);
            }
        });
        rvRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                pathBeans.get(i).setCheck(true);
                mData.add(new PathBean(pathBeans.get(i).getName(),String.valueOf(mData.size()+1)));
                pathBeans.get(i).setNumber(String.valueOf(mData.size()));
                adapter.setData(pathBeans);
            }
        });
        rvRecyclerView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mData.size()==0){
                    return true;
                }
                if (pathBeans.get(i).isCheck()){
                    pathBeans.get(i).setCheck(false);
                    for (int j = mData.size()-1; j >= 0 ; j--) {
                        if (pathBeans.get(i).getName().equals(mData.get(j).getName())){
                            mData.remove(j);
                        }
                    }
                    for (int j = 0; j < mData.size(); j++) {
                        mData.get(j).setNumber(String.valueOf(j+1));
                        for (int k = 0; k < pathBeans.size(); k++) {
                            if (pathBeans.get(k).getName().equals(mData.get(j).getName())){
                                pathBeans.get(k).setNumber(String.valueOf(j+1));
                            }
                        }
                    }
                    adapter.setData(pathBeans);
                }
                return true;
            }
        });
    }

    private void initView() {
        acTvOk = findViewById(R.id.ac_tv_ok);
        acArrowBack = findViewById(R.id.ac_arrow_back);
        rvRecyclerView = findViewById(R.id.rv_RecyclerView);
        acArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        acTvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new EvenMsg<>("path",mData));
                finish();
            }
        });
    }



    private class RevyZltjAdapter extends BaseAdapter {

        private Context mContext;
        private List<PathBean> data;

        public RevyZltjAdapter(Context mContext, List<PathBean> data) {
            this.mContext = mContext;
            setData(data);
        }

        public void setData(List<PathBean> data) {
            if (data == null) {
                data = new ArrayList<>();
            }
            this.data = data;
            notifyDataSetChanged();
        }


        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (view==null){
                view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.path_layout_item,viewGroup,false);
                holder=new ViewHolder(view);
                view.setTag(holder);
            }else {
                holder= (ViewHolder) view.getTag();
            }
            PathBean bean = data.get(position);
            if (bean.isCheck()){
                holder.tv_xian.setVisibility(View.VISIBLE);
                holder.tv_xian.setText(bean.getNumber());
            }else {
                holder.tv_xian.setVisibility(View.INVISIBLE);
            }
            holder.tv_name.setText(data.get(position).getName());
            return view;
        }

        private class ViewHolder {
            private TextView tv_name;
            private TextView tv_xian;

            public ViewHolder(View itemView) {
                tv_name=itemView.findViewById(R.id.tv_name);
                tv_xian=itemView.findViewById(R.id.tv_xian);
            }
        }
    }
}
