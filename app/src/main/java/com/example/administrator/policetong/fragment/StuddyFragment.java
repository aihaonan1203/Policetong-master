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
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.httppost.getNetInfo;
import com.example.administrator.policetong.utils.LoadingDialog;
import com.example.administrator.policetong.utils.NetworkChangeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class StuddyFragment extends Fragment implements View.OnClickListener {

    SharedPreferences sp;
    private EditText stu_name;
    private EditText stu_zd;
    private EditText stu_time;
    private EditText stu_context;
    private Button stu_submit;

    public StuddyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_studdy, container, false);
        view.setClickable(true);
        initView(view);
        return view;
    }

    private void initView(View view) {
        stu_name = (EditText) view.findViewById(R.id.stu_name);
        stu_zd = (EditText) view.findViewById(R.id.stu_zd);
        stu_time = (EditText) view.findViewById(R.id.stu_time);
        stu_context = (EditText) view.findViewById(R.id.stu_context);
        stu_submit = (Button) view.findViewById(R.id.stu_submit);
        stu_submit.setOnClickListener(this);
        sp=getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        stu_name.setText(sp.getString("username"," "));
        stu_zd.setText(sp.getString("detachment"," "));
        stu_time.setText(LoadingDialog.getTime());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stu_submit:
                submit();
                break;
        }
    }

    private void submit() {
        // validate
        String name = stu_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getContext(), "姓名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String zd = stu_zd.getText().toString().trim();
        if (TextUtils.isEmpty(zd)) {
            Toast.makeText(getContext(), "所属中队能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String time = stu_time.getText().toString().trim();
        if (TextUtils.isEmpty(time)) {
            Toast.makeText(getContext(), "学习时间不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String context = stu_context.getText().toString().trim();
        if (TextUtils.isEmpty(context)) {
            Toast.makeText(getContext(), "学习内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (NetworkChangeListener.onAvailable){
            LoadingDialog.showDialog(getActivity(),"正在提交...");
            setDataToService(name,zd,time,context);
        }else {
            Toast.makeText(getActivity(), "您的网络出现了问题", Toast.LENGTH_SHORT).show();
        }
    }

    private void setDataToService(String name, String zd, String time, String context) {
        Map info=new HashMap();
        info.put("username",name);
        info.put("userid",sp.getString("userid",""));
        info.put("date",time);
        info.put("content",context);
        info.put("detachment",zd);
        getNetInfo.NetInfo(getActivity(), "insertstudys", new JSONObject(info), new getNetInfo.VolleyCallback() {
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
}
