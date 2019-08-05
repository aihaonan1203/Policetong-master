package com.example.administrator.policetong.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.httppost.getNetInfo;
import com.example.administrator.policetong.utils.LoadingDialog;
import com.example.administrator.policetong.utils.NetworkChangeListener;
import com.example.administrator.policetong.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Button p_name,p_nature,p_pavement;

    public PathParameter() {
        // Required empty public constructor
    }


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
        pp_add_submit.setOnClickListener(this);
        p_name=view.findViewById(R.id.pp_btn_name);
        p_nature=view.findViewById(R.id.pp_btn_nature);
        p_pavement=view.findViewById(R.id.pp_btn_pavement);
        Util.RequestOption(getActivity(), "option5", new Util.RequestOptionCallBack() {
            @Override
            public void CallBack(List<String> list) {
                if (list.size()!=0){
                    int size = list.size();
                    String[] arr =list.toArray(new String[size]);
                    Util.setRadioDateIntoDialog(getActivity(),pp_name,p_name,arr);
                }else {
                    p_name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getActivity(), "服务器没有数据，无法选择，请手动输入", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                Util.RequestOption(getActivity(), "option10", new Util.RequestOptionCallBack() {
                    @Override
                    public void CallBack(List<String> list) {
                        if (list.size()!=0){
                            int size = list.size();
                            String[] arr =list.toArray(new String[size]);
                            Util.setRadioDateIntoDialog(getActivity(),pp_pavement,p_pavement,arr);
                        }else {
                            p_pavement.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(getActivity(), "服务器没有数据，无法选择，请手动输入", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        Util.RequestOption(getActivity(), "option8", new Util.RequestOptionCallBack() {
                            @Override
                            public void CallBack(List<String> list) {
                                if (list.size()!=0){
                                    int size = list.size();
                                    String[] arr =list.toArray(new String[size]);
                                    Util.setRadioDateIntoDialog(getActivity(),pp_nature,p_nature,arr);
                                }else {
                                    p_nature.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Toast.makeText(getActivity(), "服务器没有数据，无法选择，请手动输入", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });
                    }
                });
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

    public void get_data_form_server(String name,String nature,String grade,String pavement,String start,String end,String distance) {
        Map info=new HashMap();
        SharedPreferences sp=getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        info.put("username",sp.getString("username",""));
//        info.put("userid",sp.getString("userid",""));
        info.put("name",name);
        info.put("nature",nature);
        info.put("grade",grade);
        info.put("pavement",pavement);
        info.put("start",start);
        info.put("end",end);
        info.put("distance",distance);
        info.put("date", LoadingDialog.getTime());
        info.put("group",sp.getString("group",""));
        info.put("detachment",sp.getString("detachment",""));
        getNetInfo.NetInfo(getActivity(), "insertroad", new JSONObject(info), new getNetInfo.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject object) throws JSONException {
                Log.e("onSuccess: ",object.toString() );
                if (object.getString("RESULT").equals("S")){
                    Toast.makeText(getActivity(), "提交成功", Toast.LENGTH_SHORT).show();
                    LoadingDialog.disDialog();
                    getActivity().finish();
                }else {
                    Toast.makeText(getActivity(), "提交失败", Toast.LENGTH_SHORT).show();
                    LoadingDialog.disDialog();
                }
            }

            @Override
            public void onError(VolleyError volleyError) {
                LoadingDialog.disDialog();
                Toast.makeText(getActivity(), "提交失败，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
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
        LoadingDialog.showDialog(getActivity(),"正在提交...");
        get_data_form_server(name,nature,grade,pavement,start,end,distance);
    }
}
