package com.example.administrator.policetong.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.activity.LabelManageActivity;
import com.example.administrator.policetong.activity.ManageActivity;
import com.example.administrator.policetong.activity.ModulesActivity;
import com.example.administrator.policetong.base.App;
import com.example.administrator.policetong.base.BaseFragment;
import com.example.administrator.policetong.base.Consts;
import com.example.administrator.policetong.bean.new_bean.PointBean;
import com.example.administrator.policetong.network.DoNet;
import com.example.administrator.policetong.utils.GsonUtil;
import com.example.administrator.policetong.utils.LoadingDialog;
import com.example.administrator.policetong.utils.NetworkChangeListener;
import com.example.administrator.policetong.utils.UIUtils;
import com.example.administrator.policetong.utils.Util;
import com.luck.picture.lib.entity.LocalMedia;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;

/**
 * 日常勤务
 */
public class DailyService extends BaseFragment implements View.OnClickListener {


    private EditText ds_start_time;
    private EditText ds_end_time;
    private EditText ds_police;
    private EditText ds_paddr;
    private EditText ds_licheng;
    private EditText ds_qwtype;
    private Button ds_select, ds_paddr_btn, ds_qwtype_btn,btn_ds_police;
    private EditText ds_text;
    private EditText ds_context;
    private Button ds_add_submit;
    private Button ds_start_select_time;
    private Button ds_end_select_time;
    private ModulesActivity activity;

    boolean state;
    private Button iv_take_photo;
    private TextView tv_photo;
    private ArrayList<String> idPoliceList=new ArrayList<>();
    private ArrayList<String> idDoList=new ArrayList<>();
    private StringBuilder users_id=new StringBuilder();
    private StringBuilder biillegalacts_id=new StringBuilder();
    private int biroad_id;
    private int bidutytype_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily, container, false);
        initView(view);
        Objects.requireNonNull(getActivity()).findViewById(R.id.ac_tv_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),ManageActivity.class).putExtra("type",4));
            }
        });
        return view;
    }

    private void initView(View view) {
        ds_start_time =  view.findViewById(R.id.ds_start_time);
        ds_end_time =  view.findViewById(R.id.ds_end_time);
        ds_police =  view.findViewById(R.id.ds_police);
        ds_paddr =  view.findViewById(R.id.ds_paddr);
        ds_licheng =  view.findViewById(R.id.ds_licheng);
        ds_qwtype =  view.findViewById(R.id.ds_qwtype);
        ds_select =  view.findViewById(R.id.ds_select);
        ds_start_select_time =  view.findViewById(R.id.ds_start_select_time);
        ds_end_select_time =  view.findViewById(R.id.ds_end_select_time);
        ds_start_select_time.setOnClickListener(this);
        ds_end_select_time.setOnClickListener(this);
        ds_paddr_btn =  view.findViewById(R.id.ds_btn_paddr);
        ds_qwtype_btn =  view.findViewById(R.id.ds_btn_qwtype);
        btn_ds_police =  view.findViewById(R.id.btn_ds_police);
        ds_text =  view.findViewById(R.id.ds_text);
        ds_context =  view.findViewById(R.id.ds_context);
        ds_add_submit =  view.findViewById(R.id.ds_add_submit);
        ds_end_time.setText(LoadingDialog.getTime5());
        ds_start_time.setText(LoadingDialog.getTime5());
        ds_add_submit.setOnClickListener(this);
        iv_take_photo = view.findViewById(R.id.iv_take_photo);
        tv_photo = view.findViewById(R.id.tv_photo);
        tv_photo.setText(String.format(getResources().getString(R.string.photo), "0"));
        iv_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeOnePhoto();
            }
        });
        Util.RequestOption(getActivity(), "biRoad", new Util.OptionCallBack() {
            @Override
            public void CallBack(List<PointBean> list) {
                if (list.size() != 0) {
                    Util.setRadioDateIntoDialog(getActivity(), ds_paddr, ds_paddr_btn, list, new Util.SelectOpintCallBack() {
                        @Override
                        public void selectItem(int itemId) {
                            biroad_id =itemId;
                        }
                    });
                } else {
                    ds_paddr_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getActivity(), "服务器没有数据，无法选择，请手动输入", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        Util.RequestOption(getActivity(), "biDutytype", new Util.OptionCallBack() {
            @Override
            public void CallBack(List<PointBean> list) {
                if (list.size() != 0) {
                    Util.setRadioDateIntoDialog(getActivity(), ds_qwtype, ds_qwtype_btn, list, new Util.SelectOpintCallBack() {
                        @Override
                        public void selectItem(int itemId) {
                            bidutytype_id=itemId;
                        }
                    });
                } else {
                    ds_qwtype_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getActivity(), "服务器没有数据，无法选择，请手动输入", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        ds_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LabelManageActivity.class);
                intent.putExtra("userIdList", idDoList);
                intent.putExtra("type","2");
                startActivityForResult(intent,200);
            }
        });

        btn_ds_police.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LabelManageActivity.class);
                intent.putExtra("userIdList", idPoliceList);
                intent.putExtra("type","1");
                startActivityForResult(intent,200);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==200) {
            if (resultCode==666){
                idPoliceList = data.getStringArrayListExtra("idList");
                for (int i = 0; i < idPoliceList.size(); i++) {
                    users_id.append(idPoliceList.get(i)).append(",");
                }
                users_id.deleteCharAt(users_id.length()-1);
                ds_police.setText(String.valueOf(data.getStringArrayListExtra("nameList")));
            }
            if (resultCode==999){
                idDoList = data.getStringArrayListExtra("idList");
                for (int i = 0; i < idDoList.size(); i++) {
                    biillegalacts_id.append(idDoList.get(i)).append(",");
                }
                biillegalacts_id.deleteCharAt(biillegalacts_id.length()-1);
                ds_text.setText(String.valueOf(data.getStringArrayListExtra("nameList")));
            }
        }
    }

    String starttime, endtime,licheng, context;


    private void submit() {
        starttime = ds_start_time.getText().toString().trim();
        if (TextUtils.isEmpty(starttime)) {
            Toast.makeText(getContext(), "开始时间不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        endtime = ds_end_time.getText().toString().trim();
        if (TextUtils.isEmpty(endtime)) {
            Toast.makeText(getContext(), "结束时间不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(ds_police.getText().toString().trim())) {
            Toast.makeText(getContext(), "出动警力不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(ds_paddr.getText().toString().trim())) {
            Toast.makeText(getContext(), "道路不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        licheng = ds_licheng.getText().toString().trim();
        if (TextUtils.isEmpty(licheng)) {
            Toast.makeText(getContext(), "里程/参照物不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        context = ds_context.getText().toString().trim();
        if (TextUtils.isEmpty(context)) {
            Toast.makeText(getContext(), "简要内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ds_text.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), "您还没有选择重点查处违法行为", Toast.LENGTH_SHORT).show();
            return;
        }
        if (ds_qwtype.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "勤务类型不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectList ==null|| selectList.size()==0){
            Toast.makeText(getActivity(), "请先选择上传的图片!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!NetworkChangeListener.onAvailable){
            Toast.makeText(getActivity(), "您的网络出现了问题", Toast.LENGTH_SHORT).show();
            return;
        }
        PostFormBuilder builder = new PostFormBuilder();
        for (int i = 0; i < selectList.size(); i++) {
            builder.addFile("file[]", new File(selectList.get(0).getPath()).getName(), new File(selectList.get(i).getPath()));
        }
        builder.url("https://api.jjedd.net:9000/v1/uploadImg")
                .addHeader("token", App.userInfo.getToken())
                .addHeader("user", App.userInfo.getUser().getUser())
                .addHeader("Content-Type", "multipart/form-data")
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id, int code) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        com.alibaba.fastjson.JSONObject json = JSON.parseObject(response);
                        DoNet doNet = new DoNet() {
                            @Override
                            public void doWhat(String response, int id) {
                                if (!GsonUtil.verifyResult_show(response)) {
                                    return;
                                }
                                UIUtils.t(JSON.parseObject(response).getString("message"), false, UIUtils.T_SUCCESS);
                                Objects.requireNonNull(getActivity()).finish();
                                startActivity(new Intent(getActivity(), ManageActivity.class).putExtra("type", 4));
                            }
                        };
                        com.alibaba.fastjson.JSONObject jsonObject =new com.alibaba.fastjson.JSONObject();

                        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = new Date(System.currentTimeMillis());
                        jsonObject.put("work_time", simpleDateFormat.format(date));
                        jsonObject.put("start_time", ds_start_time.getText().toString());
                        jsonObject.put("end_time", ds_end_time.getText().toString());
                        jsonObject.put("users_id", users_id);
                        jsonObject.put("biroad_id", String.valueOf(biroad_id));
                        jsonObject.put("reference", licheng);
                        jsonObject.put("bidutytype_id", String.valueOf(bidutytype_id));
                        jsonObject.put("biillegalacts_id", biillegalacts_id);
                        jsonObject.put("content", context);
                        jsonObject.put("pic", json.getJSONObject("data").getString("filepath"));
                        doNet.doPost(jsonObject, Consts.URL_RCQWADD, getActivity(), true);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ds_add_submit:
                submit();
                break;
            case R.id.ds_start_select_time:
                DatePickDialog dialog = new DatePickDialog(getActivity());
                //设置上下年分限制
                dialog.setYearLimt(5);
                //设置标题
                dialog.setTitle("选择时间");
                //设置类型
                dialog.setType(DateType.TYPE_YMDHM);
                //设置消息体的显示格式，日期格式
                dialog.setMessageFormat("yyyy-MM-dd HH:mm");
                //设置点击确定按钮回调
                dialog.setOnSureLisener(new OnSureLisener() {
                    @Override
                    public void onSure(Date date) {
                        @SuppressLint("SimpleDateFormat") String string = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
                        ds_start_time.setText(string);
                    }
                });
                dialog.show();
                break;
            case R.id.ds_end_select_time:
                DatePickDialog dialog2 = new DatePickDialog(getActivity());
                //设置上下年分限制
                dialog2.setYearLimt(5);
                //设置标题
                dialog2.setTitle("选择时间");
                //设置类型
                dialog2.setType(DateType.TYPE_YMDHM);
                //设置消息体的显示格式，日期格式
                dialog2.setMessageFormat("yyyy-MM-dd HH:mm");
                //设置点击确定按钮回调
                dialog2.setOnSureLisener(new OnSureLisener() {
                    @Override
                    public void onSure(Date date) {
                        @SuppressLint("SimpleDateFormat") String string = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
                        ds_end_time.setText(string);
                    }
                });
                dialog2.show();
                break;
        }
    }

    private List<LocalMedia> selectList=new ArrayList<>();
    @Override
    public void getPhoto(List<LocalMedia> selectList) {
        tv_photo.setText(String.format(getResources().getString(R.string.photo), selectList.size() + ""));
        this.selectList.addAll(selectList);
        selectList.clear();
    }
}
