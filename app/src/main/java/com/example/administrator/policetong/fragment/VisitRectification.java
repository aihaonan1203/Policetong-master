package com.example.administrator.policetong.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.activity.ManageActivity;
import com.example.administrator.policetong.activity.PreviewActivity;
import com.example.administrator.policetong.base.App;
import com.example.administrator.policetong.base.BaseFragment;
import com.example.administrator.policetong.base.Consts;
import com.example.administrator.policetong.bean.EvenMsg;
import com.example.administrator.policetong.bean.new_bean.PointBean;
import com.example.administrator.policetong.network.DoNet;
import com.example.administrator.policetong.new_bean.VisitBean;
import com.example.administrator.policetong.utils.GsonUtil;
import com.example.administrator.policetong.utils.UIUtils;
import com.example.administrator.policetong.utils.Util;
import com.google.gson.Gson;
import com.luck.picture.lib.entity.LocalMedia;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class VisitRectification extends BaseFragment implements View.OnClickListener {

    private TextView tv_photo;
    private EditText visit_unitname;
    private EditText visit_unit;
    private EditText visit_purpose;
    private EditText visit_context;
    private EditText visit_time;
    private Button vr_danwei;
    private Button vr_mudi;
    private Button vr_xz;
    private int biunitnature_id;
    private int bivisitpurpose_id;

    public VisitRectification() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_visitrectification_add, container, false);
        view.setClickable(true);
        initView(view);
        Objects.requireNonNull(getActivity()).findViewById(R.id.ac_tv_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ManageActivity.class).putExtra("type", 3));
            }
        });
        return view;
    }

    private void initView(View view) {
        visit_unitname =  view.findViewById(R.id.visit_unitname);
        visit_unit =  view.findViewById(R.id.visit_unit);
        visit_purpose =  view.findViewById(R.id.visit_purpose);
        visit_context =  view.findViewById(R.id.visit_context);
        Button visit_submit = view.findViewById(R.id.visit_submit);
        vr_danwei =  view.findViewById(R.id.vr_unit);
        vr_mudi =  view.findViewById(R.id.vr_mudi);
        vr_xz =  view.findViewById(R.id.vr_unit_xz);
        visit_time=view.findViewById(R.id.visit_time);
        Button visit_select_time = view.findViewById(R.id.vr_select_time);
        visit_submit.setOnClickListener(this);
        visit_select_time.setOnClickListener(this);
        tv_photo=view.findViewById(R.id.tv_photo);
        tv_photo=view.findViewById(R.id.tv_photo);
        tv_photo.setText(String.format(getResources().getString(R.string.photo),"0"));
        view.findViewById(R.id.iv_take_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeOnePhoto();
            }
        });

        Util.RequestOption(getActivity(), "biUnitnature", new Util.OptionCallBack() {
            @Override
            public void CallBack(List<PointBean> list) {
                if (list.size() != 0) {
                    Util.setRadioDateIntoDialog(getActivity(), visit_unit, vr_xz, list, new Util.SelectOpintCallBack() {
                        @Override
                        public void selectItem(int itemId) {
                            biunitnature_id = itemId;
                        }
                    });
                } else {
                    vr_danwei.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getActivity(), "服务器没有数据，无法选择，请手动输入", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        Util.RequestOption(getActivity(), "biVisitpurpose", new Util.OptionCallBack() {
            @Override
            public void CallBack(List<PointBean> list) {
                if (list.size() != 0) {
                    Util.setRadioDateIntoDialog(getActivity(), visit_purpose, vr_mudi, list, new Util.SelectOpintCallBack() {
                        @Override
                        public void selectItem(int itemId) {
                            bivisitpurpose_id = itemId;
                        }
                    });
                } else {
                    vr_danwei.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getActivity(), "服务器没有数据，无法选择，请手动输入", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        Util.RequestOption(getActivity(), "biOrganization", new Util.OptionCallBack() {
            @Override
            public void CallBack(List<PointBean> list) {
                if (list.size() != 0) {
                    Util.setRadioDateIntoDialog(getActivity(), visit_unitname, vr_danwei, list,null);
                } else {
                    vr_danwei.setOnClickListener(new View.OnClickListener() {
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
            case R.id.visit_submit:
                submit();
                break;
            case R.id.vr_select_time:
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
                        @SuppressLint("SimpleDateFormat") String string=new SimpleDateFormat("yyyy-MM-dd hh:mm").format(date);
                        visit_time.setText(string);
                    }
                });
                dialog.show();
                break;
        }
    }
    String unitname,context,purpose,unit;
    private void submit() {
        if (selectList==null||selectList.size()==0){
            Toast.makeText(getActivity(), "请先选择上传的图片!", Toast.LENGTH_SHORT).show();
            return;
        }
        // validate
        final String time = visit_time.getText().toString().trim();
        if (TextUtils.isEmpty(time)) {
            Toast.makeText(getContext(), "时间不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        unitname = visit_unitname.getText().toString().trim();
        if (TextUtils.isEmpty(unitname)) {
            Toast.makeText(getContext(), "走访单位不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        context = visit_context.getText().toString().trim();
        if (TextUtils.isEmpty(context)) {
            Toast.makeText(getContext(), "内容简要不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        purpose=visit_purpose.getText().toString();
        if (TextUtils.isEmpty(purpose)) {
            Toast.makeText(getContext(), "走访目的不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        unit=visit_unit.getText().toString();
        if (TextUtils.isEmpty(unitname)) {
            Toast.makeText(getContext(), "单位性质不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        setDialog();
        PostFormBuilder builder = new PostFormBuilder();
        for (int i = 0; i < selectList.size(); i++) {
            builder.addFile("file[]", new File(selectList.get(i).getPath()).getName(), new File(selectList.get(i).getPath()));
        }
        builder.url("https://api.jjedd.net:9000/v1/uploadImg")
                .addHeader("token", App.userInfo.getToken())
                .addHeader("user", App.userInfo.getUser().getUser())
                .addHeader("Content-Type", "multipart/form-data")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id, int code) {
                        closeDialog();
                        UIUtils.t("图片上传失败",false,UIUtils.T_ERROR);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        com.alibaba.fastjson.JSONObject json = JSON.parseObject(response);
                        DoNet doNet = new DoNet() {
                            @Override
                            public void doWhat(String response, int id) {
                                closeDialog();
                                if (!GsonUtil.verifyResult_show(response)) {
                                    return;
                                }
                                UIUtils.t(JSON.parseObject(response).getString("message"), false, UIUtils.T_SUCCESS);
                                Objects.requireNonNull(getActivity()).finish();
                                startActivity(new Intent(getActivity(), ManageActivity.class).putExtra("type", 3));
                            }
                        };
                        doNet.setOnErrorListener(new DoNet.OnErrorListener() {
                            @Override
                            public void onError(int code) {
                                closeDialog();
                            }
                        });
                        JSONObject jsonObject =new JSONObject();
//                        jsonObject.put("work_time", time);
                        jsonObject.put("zfunit", unitname);
                        jsonObject.put("biunitnature_id", biunitnature_id);
                        jsonObject.put("bivisitpurpose_id", bivisitpurpose_id);
                        jsonObject.put("detail", context);
                        jsonObject.put("pic", json.getJSONObject("data").getString("filepath"));
                        doNet.doPost(jsonObject, Consts.URL_ZFXCZGADD, getActivity(), false);
                    }
                });
    }


    private List<LocalMedia> selectList = new ArrayList<>();
    @Override
    public void getPhoto(List<LocalMedia> selectList) {
        this.selectList.addAll(selectList);
        tv_photo.setText(String.format(getResources().getString(R.string.photo), this.selectList.size() + ""));
        selectList.clear();
    }
}
