package com.example.administrator.policetong.fragment;


import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.activity.ModulesActivity;
import com.example.administrator.policetong.activity.PreviewActivity;
import com.example.administrator.policetong.base.BaseFragment;
import com.example.administrator.policetong.bean.EvenMsg;
import com.example.administrator.policetong.httppost.getNetInfo;
import com.example.administrator.policetong.utils.LoadingDialog;
import com.example.administrator.policetong.utils.NetworkChangeListener;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class StuddyFragment extends BaseFragment implements View.OnClickListener {

    SharedPreferences sp;
    private EditText stu_study_time;
    private EditText stu_zd;
    private EditText stu_time;
    private EditText stu_context;
    private Button stu_submit;
    private Button btn_preview;
    private ImageView iv_take_photo;
    private TextView tv_photo;

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
        stu_study_time = (EditText) view.findViewById(R.id.stu_study_time);
        stu_zd = (EditText) view.findViewById(R.id.stu_zd);
        stu_time = (EditText) view.findViewById(R.id.stu_time);
        stu_context = (EditText) view.findViewById(R.id.stu_context);
        stu_submit = (Button) view.findViewById(R.id.stu_submit);
        btn_preview = (Button) view.findViewById(R.id.btn_preview);
        iv_take_photo=view.findViewById(R.id.iv_take_photo);
        tv_photo=view.findViewById(R.id.tv_photo);
        stu_submit.setOnClickListener(this);
        sp=getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        stu_study_time.setText(sp.getString("username",""));
        stu_zd.setText(sp.getString("detachment",""));
        stu_time.setText(LoadingDialog.getTime());
        tv_photo.setText(String.format(getResources().getString(R.string.photo),"0"));
        iv_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });
        btn_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectList==null||selectList.size()==0){
                    Toast.makeText(getActivity(), "请先选择照片!!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                EventBus.getDefault().postSticky(new EvenMsg<>("",selectList));
                startActivity(new Intent(getActivity(),PreviewActivity.class));
            }
        });
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
        String name = stu_study_time.getText().toString().trim();
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

    @Override
    public void getPhoto(List<LocalMedia> selectList) {
        tv_photo.setText(String.format(getResources().getString(R.string.photo),selectList.size()+""));
    }

}
