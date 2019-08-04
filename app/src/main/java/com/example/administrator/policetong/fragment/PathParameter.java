package com.example.administrator.policetong.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.activity.ManageActivity;
import com.example.administrator.policetong.base.Consts;
import com.example.administrator.policetong.bean.new_bean.PointBean;
import com.example.administrator.policetong.network.DoNet;
import com.example.administrator.policetong.utils.GsonUtil;
import com.example.administrator.policetong.utils.NetworkChangeListener;
import com.example.administrator.policetong.utils.UIUtils;
import com.example.administrator.policetong.utils.Util;

import java.util.List;
import java.util.Objects;

/**
 * 道路台账
 */
public class PathParameter extends Fragment implements View.OnClickListener {


    private EditText pp_name;
    private EditText pp_nature;
    private EditText pp_grade;
    private EditText pp_pavement;
    private EditText pp_start;
    private EditText pp_end;
    private EditText pp_distance;
    private Button pp_add_submit;
    private Button p_name,p_nature,p_pavement,p_grade;
    private int road_id,biroadtype_id,biroadgrade_id;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_path_parameter, container, false);
        initView(view);
        return view;
    }


    private void initView(View view) {
        pp_name = (EditText) view.findViewById(R.id.pp_name);
        pp_nature = (EditText) view.findViewById(R.id.pp_nature);
        pp_grade = (EditText) view.findViewById(R.id.pp_grade);
        pp_pavement = (EditText) view.findViewById(R.id.pp_pavement);
        pp_start = (EditText) view.findViewById(R.id.pp_start);
        pp_end = (EditText) view.findViewById(R.id.pp_end);
        pp_distance = (EditText) view.findViewById(R.id.pp_distance);
        pp_add_submit = (Button) view.findViewById(R.id.pp_add_submit);
        p_grade = (Button) view.findViewById(R.id.p_grade);
        pp_add_submit.setOnClickListener(this);
        p_name=view.findViewById(R.id.pp_btn_name);
        p_nature=view.findViewById(R.id.pp_btn_nature);
        p_pavement=view.findViewById(R.id.pp_btn_pavement);
        Objects.requireNonNull(getActivity()).findViewById(R.id.ac_tv_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),ManageActivity.class).putExtra("type",1));
            }
        });
        Util.RequestOption(getActivity(), "biRoad", new Util.OptionCallBack() {
            @Override
            public void CallBack(List<PointBean> list) {
                if (list.size()!=0){
                    Util.setRadioDateIntoDialog(getActivity(), pp_name, p_name, list, new Util.SelectOpintCallBack() {
                        @Override
                        public void selectItem(int itemId) {
                            road_id=itemId;
                        }
                    });
                }else {
                    p_name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getActivity(), "服务器没有数据，无法选择，请手动输入", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        Util.RequestOption(getActivity(), "biRoadtype", new Util.OptionCallBack() {
            @Override
            public void CallBack(List<PointBean> list) {
                if (list.size()!=0){
                    Util.setRadioDateIntoDialog(getActivity(),pp_nature,p_nature,list,new Util.SelectOpintCallBack() {
                        @Override
                        public void selectItem(int itemId) {
                            biroadtype_id=itemId;
                        }
                    });
                }else {
                    p_name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getActivity(), "服务器没有数据，无法选择，请手动输入", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        Util.RequestOption(getActivity(), "biRoadgrade", new Util.OptionCallBack() {
            @Override
            public void CallBack(List<PointBean> list) {
                if (list.size()!=0){
                    Util.setRadioDateIntoDialog(getActivity(),pp_grade,p_grade,list,new Util.SelectOpintCallBack() {
                        @Override
                        public void selectItem(int itemId) {
                            biroadgrade_id=itemId;
                        }
                    });
                }else {
                    p_name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getActivity(), "服务器没有数据，无法选择，请手动输入", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        Util.RequestOption(getActivity(), "biRoadcondition", new Util.OptionCallBack() {
            @Override
            public void CallBack(List<PointBean> list) {
                if (list.size()!=0){
                    Util.setRadioDateIntoDialog(getActivity(),pp_pavement,p_pavement,list,null);
                }else {
                    p_name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getActivity(), "服务器没有数据，无法选择，请手动输入", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pp_add_submit:
                submit();
                break;
        }
    }

    public void get_data_form_server(String pavement,String start,String end,String distance) {
        com.alibaba.fastjson.JSONObject jsonObject=new com.alibaba.fastjson.JSONObject();
        jsonObject.put("road_id",road_id);
        jsonObject.put("biroadtype_id",biroadtype_id);
        jsonObject.put("biroadgrade_id",biroadgrade_id);
        jsonObject.put("pavement",pavement);
        jsonObject.put("startpoint",start);
        jsonObject.put("endpoint",end);
        jsonObject.put("mileage",distance);
        DoNet doNet = new DoNet() {
            @Override
            public void doWhat(String response, int id) {
                if (!GsonUtil.verifyResult_show(response)) {
                    return;
                }
                UIUtils.t(JSON.parseObject(response).getString("message"),false,UIUtils.T_SUCCESS);
                Objects.requireNonNull(getActivity()).finish();
            }
        };
        doNet.doPost(jsonObject,Consts.URL_ROADTZADD, getActivity(), true);
    }

    private void submit() {
        // validate
        String name = pp_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getContext(), "名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String grade = pp_grade.getText().toString().trim();
        if (TextUtils.isEmpty(grade)) {
            Toast.makeText(getContext(), "等级不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String start = pp_start.getText().toString().trim();
        if (TextUtils.isEmpty(start)) {
            Toast.makeText(getContext(), "起点不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String end = pp_end.getText().toString().trim();
        if (TextUtils.isEmpty(end)) {
            Toast.makeText(getContext(), "终点不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String distance = pp_distance.getText().toString().trim();
        if (TextUtils.isEmpty(distance)) {
            Toast.makeText(getContext(), "里程不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String nature=pp_nature.getText().toString();
        if (TextUtils.isEmpty(nature)) {
            Toast.makeText(getContext(), "道路性质不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String pavement=pp_pavement.getText().toString();
        if (TextUtils.isEmpty(pavement)) {
            Toast.makeText(getContext(), "路面状况不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!NetworkChangeListener.onAvailable){
            Toast.makeText(getActivity(), "您的网络出现了问题", Toast.LENGTH_SHORT).show();
            return;
        }
        get_data_form_server(pavement,start,end,distance);
    }
}
