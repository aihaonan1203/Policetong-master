package com.example.administrator.policetong.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.policetong.R;
import com.example.administrator.policetong.activity.SelectPathActivity;
import com.example.administrator.policetong.base.BaseFragment;
import com.example.administrator.policetong.bean.EvenMsg;
import com.example.administrator.policetong.bean.PathBean;
import com.example.administrator.policetong.bean.PoliceMent;
import com.example.administrator.policetong.utils.LoadingDialog;
import com.example.administrator.policetong.utils.Util;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class PoliceFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private Button btnPath;
    private EditText safetyLicheng;
    private EditText pcAddTime;
    private Button pcTime;
    private RecyclerView rvRecyclerView;
    private Button btnJingli;
    private RecyclerView rvRecyclerView2;
    private TextView tv_title;
    private String[] array;
    private List<PoliceMent> list=new ArrayList<>();
    private MyAdapter myAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_police_layout, container, false);
        view.setClickable(true);
        initView(view);
        Util.RequestOption(getActivity(), "option9", new Util.RequestOptionCallBack() {
            @Override
            public void CallBack(List<String> list) {
                array = list.toArray(new String[0]);
            }
        });
        return view;
    }

    private void showDialog() {
        View view1=LayoutInflater.from(getActivity()).inflate(R.layout.jingli_layout,null,false);
        final EditText dialog_pq=view1.findViewById(R.id.dialog_pq);
        final EditText dialog_rs=view1.findViewById(R.id.dialog_rs);
        final EditText dialog_gd=view1.findViewById(R.id.pc_add_time);
        Button dialog_btn=view1.findViewById(R.id.pc_time);
        Button dialog_yes=view1.findViewById(R.id.dialog_ok);
        Button dialog_no=view1.findViewById(R.id.dialog_no);
        final RadioButton rb_yes=view1.findViewById(R.id.rb_yes);
        RadioButton rb_no=view1.findViewById(R.id.rb_no);
        Util.setRadioDateIntoDialog(getActivity(),dialog_gd,dialog_btn,array);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).setView(view1).create();
        dialog.show();
        dialog_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String renshu = dialog_rs.getText().toString();
                String paiqian = dialog_pq.getText().toString();
                String gandian = dialog_gd.getText().toString();
                if (!renshu.isEmpty()&&!paiqian.isEmpty()&&!gandian.isEmpty()){
                    dialog.dismiss();
                    if (rb_yes.isChecked()){
                        list.add(new PoliceMent(list.size()+1,gandian,paiqian,1,renshu+"人","(调灯)"));
                    }else {
                        list.add(new PoliceMent(list.size()+1,gandian,paiqian,1,renshu+"人",""));
                    }
                    myAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(getActivity(), "不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


    @Override
    public void getPhoto(List<LocalMedia> selectList) {
    }

    @SuppressLint("SetTextI18n")
    private void initView(View view) {
        btnPath = view.findViewById(R.id.btn_path);
        recyclerView = view.findViewById(R.id.rv_RecyclerView);
        safetyLicheng = view.findViewById(R.id.sg_canyu);
        pcAddTime = view.findViewById(R.id.pc_add_time);
        pcTime = view.findViewById(R.id.pc_time);
        tv_title = view.findViewById(R.id.tv_title);
        btnJingli = view.findViewById(R.id.btn_jingli);
        rvRecyclerView2 = view.findViewById(R.id.rv_RecyclerView2);
        rvRecyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
        myAdapter = new MyAdapter(getActivity(), list);
        rvRecyclerView2.setAdapter(myAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        tv_title.setText("第二大队" + LoadingDialog.getTime4() + "安全保卫工作方案");
        btnPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SelectPathActivity.class));
            }
        });
        btnJingli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXX(EvenMsg<List<PathBean>> messageEvent) {
        List<PathBean> data = messageEvent.getData();
        recyclerView.setAdapter(new RevyZltjAdapter(getActivity(), data));
    }

    private class RevyZltjAdapter extends RecyclerView.Adapter<RevyZltjAdapter.ViewHolder> {

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
        }

        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.yllistview_layout_item, parent, false);
            ViewHolder hol = new ViewHolder(view);
            return hol;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.tv_name.setText(data.get(position).getName());
            if (position == data.size() - 1) {
                holder.tv_xian.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private TextView tv_name;
            private TextView tv_xian;

            public ViewHolder(View itemView) {
                super(itemView);
                tv_name = itemView.findViewById(R.id.tv_name);
                tv_xian = itemView.findViewById(R.id.tv_xian);
            }
        }
    }



    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private Context mContext;
        private List<PoliceMent> ments;

        public MyAdapter(Context mContext, List<PoliceMent> ments) {
            this.mContext = mContext;
            this.ments=ments;
        }

        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.yllistview_layout_item, parent, false);
            ViewHolder hol = new ViewHolder(view);
            return hol;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            PoliceMent bean = ments.get(position);
            holder.tv_name.setText(bean.getPlace()+":"+bean.getSquName()+bean.getNumer()+bean.getDiaodeng());
            holder.tv_xian.setVisibility(View.GONE);
        }

        @Override
        public int getItemCount() {
            return ments.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private TextView tv_name;
            private TextView tv_xian;

            public ViewHolder(View itemView) {
                super(itemView);
                tv_name = itemView.findViewById(R.id.tv_name);
                tv_xian = itemView.findViewById(R.id.tv_xian);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
