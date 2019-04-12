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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.administrator.policetong.MainActivity;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.activity.ModulesActivity;
import com.example.administrator.policetong.httppost.getNetInfo;
import com.example.administrator.policetong.utils.LoadingDialog;
import com.example.administrator.policetong.utils.LocationUtil;
import com.example.administrator.policetong.utils.NetworkChangeListener;
import com.example.administrator.policetong.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class PoliceConfirmed extends Fragment implements View.OnClickListener {


    private EditText pc_add_time;
    private EditText pc_add_paddr;
    private EditText pc_add_unit;
    private EditText pc_add_remark;
    private Button pc_add_submit;
    private Button pc_add_gd;
    private Button pc_add_dw;


    LocationUtil locationUtil;
    boolean state=false;
    double latitude,lontitude;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_police_confirmed_add, container, false);
        view.setClickable(true);
        sharedPreferences=getActivity().getSharedPreferences("PoliceConfirmed_data",Context.MODE_PRIVATE);
        initView(view);
        return view;
    }
    RadioButton rb1,rb2;
    SharedPreferences sharedPreferences;
    private void initView(View view) {
        pc_add_time = (EditText) view.findViewById(R.id.pc_add_time);
        pc_add_paddr = (EditText) view.findViewById(R.id.pc_add_paddr);
        pc_add_unit = (EditText) view.findViewById(R.id.pc_add_unit);
        pc_add_remark = (EditText) view.findViewById(R.id.pc_add_remark);
        pc_add_submit = (Button) view.findViewById(R.id.pc_add_submit);
        pc_add_gd=view.findViewById(R.id.pc_add_gd);
        pc_add_dw=view.findViewById(R.id.pc_add_dw);
        pc_add_submit.setOnClickListener(this);
        rb1=view.findViewById(R.id.pc_r1);
        rb2=view.findViewById(R.id.pc_r2);
        pc_add_time.setText(LoadingDialog.getTime());
        pc_add_paddr.setText(sharedPreferences.getString("paddr",""));
        pc_add_unit.setText(sharedPreferences.getString("unit",""));
        Util.RequestOption(getActivity(), "option9", new Util.RequestOptionCallBack() {
            @Override
            public void CallBack(List<String> list) {
                if (list.size()!=0){
                    int size = list.size();
                    String[] arr =list.toArray(new String[size]);
                    Util.setRadioDateIntoDialog(getActivity(),pc_add_paddr,pc_add_gd,arr);
                }else {
                    pc_add_gd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getActivity(), "服务器没有数据，无法选择，请手动输入", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                Util.RequestOption(getActivity(), "option1", new Util.RequestOptionCallBack() {
                    @Override
                    public void CallBack(List<String> list) {
                        if (list.size()!=0){
                            int size = list.size();
                            String[] arr =list.toArray(new String[size]);
                            Util.setRadioDateIntoDialog(getActivity(),pc_add_unit,pc_add_dw,arr);
                        }else {
                            pc_add_dw.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pc_add_submit:
                submit();
                break;
        }
    }

    private void submit() {
        ModulesActivity activity= (ModulesActivity) getActivity();
        state=activity.tv_state;
        latitude=activity.tv_latitude;
        lontitude=activity.tv_lontitude;
        // validate
        if (!state){
            Toast.makeText(getActivity(), "位置信息获取失败，请检查网络", Toast.LENGTH_SHORT).show();
            return;
        }
        String time = pc_add_time.getText().toString().trim();
        if (TextUtils.isEmpty(time)) {
            Toast.makeText(getContext(), "到岗时间不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String paddr = pc_add_paddr.getText().toString().trim();
        if (TextUtils.isEmpty(paddr)) {
            Toast.makeText(getContext(), "岗点不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String unit = pc_add_unit.getText().toString().trim();
        if (TextUtils.isEmpty(unit)) {
            Toast.makeText(getContext(), "勤务单位不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String remark = pc_add_remark.getText().toString().trim();
        if (remark.equals("")){
            remark="无";
        }
        String tiaodeng;
        if (rb1.isChecked()){
            tiaodeng="是";
        }else {
            tiaodeng="否";
        }
        if (!NetworkChangeListener.onAvailable){
            Toast.makeText(getActivity(), "您的网络出现了问题", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showDialog(getActivity(),"正在提交内容！");
        submitData(time,paddr,unit,tiaodeng,remark);
    }

    private void submitData(String time, final String paddr, final String unit, String tiaodeng, String remark) {
        Map info=new HashMap();
        SharedPreferences sp=getActivity().getSharedPreferences("userinfo",Context.MODE_PRIVATE);
        info.put("username",sp.getString("username",""));
        info.put("userid",sp.getString("userid",""));
        info.put("place",paddr);
        info.put("longitude",lontitude+"");
        info.put("latitude",latitude+"");
        info.put("unit",unit);
        info.put("turn",tiaodeng);
        info.put("date",time);
        info.put("other",remark);
        info.put("group",sp.getString("group",""));
        info.put("detachment",sp.getString("detachment",""));
        getNetInfo.NetInfo(getActivity(), "atdservlet", new JSONObject(info), new getNetInfo.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject object) throws JSONException {
                if (object.getString("RESULT").equals("S")){
                    Toast.makeText(getActivity(), "提交成功", Toast.LENGTH_SHORT).show();
                    LoadingDialog.disDialog();
                    sharedPreferences.edit().putString("paddr",paddr).apply();
                    sharedPreferences.edit().putString("unit",unit).apply();
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
}
