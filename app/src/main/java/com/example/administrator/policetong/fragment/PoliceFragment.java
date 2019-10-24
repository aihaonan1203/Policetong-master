package com.example.administrator.policetong.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.activity.ManageActivity;
import com.example.administrator.policetong.base.BaseFragment;
import com.example.administrator.policetong.base.Consts;
import com.example.administrator.policetong.bean.new_bean.PointBean;
import com.example.administrator.policetong.network.DoNet;
import com.example.administrator.policetong.utils.GsonUtil;
import com.example.administrator.policetong.utils.UIUtils;
import com.example.administrator.policetong.utils.Util;
import com.luck.picture.lib.entity.LocalMedia;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class PoliceFragment extends BaseFragment implements View.OnClickListener {

    private EditText tvTitle;
    private EditText tvPoliceTime;
    private Button btnPoliceTime;
    private EditText tvPoliceOnTime;
    private Button btnPoliceOnTime;
    private TextView tvAddInfo;
    private RecyclerView mRecyclerView;
    private EditText tvContent;
    private TextView tvContext;
    private Button btnSubmit;
    private List<PointBean> dataList;
    private int itemId=1;
    private List<PointBean> dptList;
    private View dialogView;
    private Spinner mSpinner;
    private Switch mSwitch;
    private Button dialog_btnSubmit;
    private StringBuilder sb;
    private AlertDialog dialog;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_police_layout, container, false);
        initView(view);
        init();
        Objects.requireNonNull(getActivity()).findViewById(R.id.ac_tv_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ManageActivity.class).putExtra("type", 7));
            }
        });
        return view;
    }

    private void init() {
        Util.RequestOption(getActivity(), "biPostpoint", new Util.OptionCallBack() {
            @Override
            public void CallBack(List<PointBean> list) {
                if (list.size() != 0) {
                    dataList = list;
                }
            }
        });
        getDpt();
    }

    public void getDpt() {
        DoNet doNet = new DoNet() {
            @Override
            public void doWhat(String response, int id) {
                Log.e("doWhat: ", response);
                if (!GsonUtil.verifyResult_show(response)){
                    return;
                }
                dptList = GsonUtil.parseJsonArrayWithGson(JSON.parseObject(response).getJSONObject("data").getJSONArray("info").toString(), PointBean.class);
            }
        };
        doNet.setOnErrorListener(new DoNet.OnErrorListener() {
            @Override
            public void onError(int code) {

            }
        });
        doNet.doGet(Consts.URL_GETDPT, getActivity(), true);
    }

    private void initView(View view) {
        dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.police_input_select_number,null,false);
        mSpinner=dialogView.findViewById(R.id.mSpinner);
        mSwitch=dialogView.findViewById(R.id.mSwitch);
        dialog_btnSubmit=dialogView.findViewById(R.id.btnSubmit);
        dialog_btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSwitch.isChecked()){
                    sb.append(getResources().getStringArray(R.array.personNumber)[mSpinner.getSelectedItemPosition()]).append(",").append("需要调灯");
                }else {
                    sb.append(getResources().getStringArray(R.array.personNumber)[mSpinner.getSelectedItemPosition()]).append(",").append("不需调灯");
                }
                String content=tvContext.getText().toString().trim()+"\n"+sb.toString();
                tvContext.setText(content);
                dialog.dismiss();
                itemId++;
            }
        });
        dialog = new AlertDialog.Builder(getActivity())
                .setView(dialogView).create();
        tvTitle =  view.findViewById(R.id.tvTitle);
        tvPoliceTime =  view.findViewById(R.id.tvPoliceTime);
        btnPoliceTime =  view.findViewById(R.id.btnPoliceTime);
        btnPoliceTime.setOnClickListener(this);
        tvPoliceOnTime =  view.findViewById(R.id.tvPoliceOnTime);
        tvContext =  view.findViewById(R.id.tvContext);
        btnPoliceOnTime =  view.findViewById(R.id.btnPoliceOnTime);
        btnPoliceOnTime.setOnClickListener(this);
        tvAddInfo =  view.findViewById(R.id.tvAddInfo);
        mRecyclerView =  view.findViewById(R.id.mRecyclerView);
        tvContent =  view.findViewById(R.id.tvContent);
        btnSubmit =  view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        tvAddInfo.setOnClickListener(this);
    }

    @Override
    public void getPhoto(List<LocalMedia> selectList) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btnPoliceTime:
                selectTime(tvPoliceTime);
                break;
            case R.id.btnPoliceOnTime:
                selectTime(tvPoliceOnTime);
                break;
            case R.id.btnSubmit:
                submit();
                break;
            case R.id.tvAddInfo:
                sb = new StringBuilder();
                Util.setRadioDateIntoDialog(getActivity(),tvAddInfo, dataList, new Util.SelectStringCallBack() {
                    @Override
                    public void selectItem(String Id) {
                        sb.append(itemId).append(".").append(Id).append(" 由 ");
                        Util.setRadioDateIntoDialog(getActivity(), dptList, new Util.SelectStringCallBack() {
                            @Override
                            public void selectItem(String itemId) {
                                sb.append(itemId).append(" 负责,需要");
                                dialog.show();
                            }
                        });
                    }
                });
                break;
        }
    }

    private void submit() {
        String title = tvTitle.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(getContext(), "标题不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String policeTime = tvPoliceTime.getText().toString().trim();
        if (TextUtils.isEmpty(policeTime)) {
            Toast.makeText(getContext(), "任务时间不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String policeOnTime = tvPoliceOnTime.getText().toString().trim();
        if (TextUtils.isEmpty(policeOnTime)) {
            Toast.makeText(getContext(), "到岗时间不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String context = tvContext.getText().toString().trim();
        if (TextUtils.isEmpty(context)) {
            Toast.makeText(getContext(), "任务详情不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String cotent = tvContent.getText().toString().trim();
        if (TextUtils.isEmpty(cotent)) {
            Toast.makeText(getContext(), "备注信息不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        DoNet doNet = new DoNet() {
            @Override
            public void doWhat(String response, int id) {
                closeDialog();
                if (!GsonUtil.verifyResult_show(response)) {
                    return;
                }
                UIUtils.t(JSON.parseObject(response).getString("message"), false, UIUtils.T_SUCCESS);
                Objects.requireNonNull(getActivity()).finish();
                startActivity(new Intent(getActivity(),ManageActivity.class).putExtra("type",7));
            }
        };
        doNet.setOnErrorListener(new DoNet.OnErrorListener() {
            @Override
            public void onError(int code) {
                closeDialog();
            }
        });
        com.alibaba.fastjson.JSONObject jsonObject =new com.alibaba.fastjson.JSONObject();
        jsonObject.put("title",title);
        jsonObject.put("task_time", policeTime);
        jsonObject.put("work_time", policeOnTime);
        jsonObject.put("content", context);
        jsonObject.put("remarks", cotent);
        doNet.doPost(jsonObject, Consts.URL_JBWEDIT, getActivity(), false);
    }

    private void selectTime(final EditText tvTime){
        DatePickDialog dialog = new DatePickDialog(getActivity());
        //设置上下年分限制
        dialog.setYearLimt(5);
        //设置标题
        dialog.setTitle("选择时间");
        //设置类型
        dialog.setType(DateType.TYPE_YMDHM);
        //设置消息体的显示格式，日期格式
        dialog.setMessageFormat("yyyy-MM-dd hh:mm");
        //设置点击确定按钮回调
        dialog.setOnSureLisener(new OnSureLisener() {
            @Override
            public void onSure(Date date) {
                @SuppressLint("SimpleDateFormat") String string=new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
                tvTime.setText(string);
            }
        });
        dialog.show();
    }
}
