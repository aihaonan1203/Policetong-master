package com.example.administrator.policetong.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.activity.ModulesActivity;
import com.example.administrator.policetong.activity.PreviewActivity;
import com.example.administrator.policetong.base.App;
import com.example.administrator.policetong.base.BaseActivity;
import com.example.administrator.policetong.base.BaseBean;
import com.example.administrator.policetong.base.BaseFragment;
import com.example.administrator.policetong.base.Consts;
import com.example.administrator.policetong.bean.EvenMsg;
import com.example.administrator.policetong.bean.new_bean.PointBean;
import com.example.administrator.policetong.httppost.getNetInfo;
import com.example.administrator.policetong.network.DoNet;
import com.example.administrator.policetong.network.Network;
import com.example.administrator.policetong.utils.GsonUtil;
import com.example.administrator.policetong.utils.LoadingDialog;
import com.example.administrator.policetong.utils.NetworkChangeListener;
import com.example.administrator.policetong.utils.UIUtils;
import com.example.administrator.policetong.utils.Util;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.functions.Consumer;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class SafetyChecks extends BaseFragment implements View.OnClickListener {

    private Button safety_add_submit;
    private EditText safety_time;
    private EditText safety_paddr;
    private EditText safety_licheng;
    private EditText safety_unit;
    private EditText safety_fxunit;
    private EditText safety_shangbao;
    private EditText safety_zhenggai;
    private EditText safety_zgtime;
    private Button safety_select_time;
    private EditText safety_xq;
    private static String SD_CARD_TEMP_DIR;
    private Button unit_btn,fxunit_btn,paddr_btn;
    private File file;
    private Button btn_preview;
    private ImageView iv_take_photo;
    private TextView tv_photo;
    private int biroad_id;
    private int biorganization_id;
    private int biunitnature_id;
    private String xiangqing;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_safety_checks_add, container, false);
        view.setClickable(true);
        initView(view);
        return view;
    }

    private void initView(View view) {
        safety_add_submit=view.findViewById(R.id.sg_add_submit);
        safety_add_submit.setOnClickListener(this);
        safety_time = (EditText) view.findViewById(R.id.sg_time);
        safety_paddr = (EditText) view.findViewById(R.id.sg_type);
        safety_xq = (EditText) view.findViewById(R.id.safety_xq);
        safety_licheng = (EditText) view.findViewById(R.id.jb_lingdao);
        safety_unit = (EditText) view.findViewById(R.id.sg_car_type);
        safety_fxunit = (EditText) view.findViewById(R.id.sg_shoushang);
        safety_shangbao = (EditText) view.findViewById(R.id.sg_chesun);
        safety_zhenggai = (EditText) view.findViewById(R.id.safety_zhenggai);
        safety_zgtime = (EditText) view.findViewById(R.id.safety_zgtime);
        safety_select_time = (Button) view.findViewById(R.id.sg_select_time);
        safety_select_time.setOnClickListener(this);
        safety_time.setText(LoadingDialog.getTime());
        safety_zgtime.setText(LoadingDialog.getTime());
        fxunit_btn=view.findViewById(R.id.sg_shoushang_s);
        paddr_btn=view.findViewById(R.id.sg_type_btn);
        unit_btn=view.findViewById(R.id.safety_car_type_select);
        btn_preview = (Button) view.findViewById(R.id.btn_preview);
        iv_take_photo=view.findViewById(R.id.iv_take_photo);
        tv_photo=view.findViewById(R.id.tv_photo);
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
        Util.RequestOption(getActivity(), "biRoad", new Util.OptionCallBack() {
            @Override
            public void CallBack(List<PointBean> list) {
                if (list.size()!=0){
                    Util.setRadioDateIntoDialog(getActivity(), safety_paddr, paddr_btn, list, new Util.SelectOpintCallBack() {
                        @Override
                        public void selectItem(int itemId) {
                            biroad_id=itemId;
                        }
                    });
                }else {
                    paddr_btn.setOnClickListener(new View.OnClickListener() {
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
                if (list.size()!=0){
                    Util.setRadioDateIntoDialog(getActivity(), safety_fxunit, fxunit_btn, list, new Util.SelectOpintCallBack() {
                        @Override
                        public void selectItem(int itemId) {
                            biorganization_id=itemId;
                        }
                    });
                }else {
                    fxunit_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getActivity(), "服务器没有数据，无法选择，请手动输入", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        Util.RequestOption(getActivity(), "biUnitnature", new Util.OptionCallBack() {
            @Override
            public void CallBack(List<PointBean> list) {
                if (list.size()!=0){
                    Util.setRadioDateIntoDialog(getActivity(), safety_unit, unit_btn, list, new Util.SelectOpintCallBack() {
                        @Override
                        public void selectItem(int itemId) {
                            biunitnature_id=itemId;
                        }
                    });
                }else {
                    unit_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getActivity(), "服务器没有数据，无法选择，请手动输入", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        iv_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeOnePhoto();
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


    private void submit() {
        // validate
        time = safety_time.getText().toString().trim();
        if (TextUtils.isEmpty(time)) {
            Toast.makeText(getContext(), "时间不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        paddr = safety_paddr.getText().toString().trim();
        if (TextUtils.isEmpty(paddr)) {
            Toast.makeText(getContext(), "道路不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        licheng = safety_licheng.getText().toString().trim();
        if (TextUtils.isEmpty(licheng)) {
            Toast.makeText(getContext(), "里程不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        unit = safety_unit.getText().toString().trim();
        if (TextUtils.isEmpty(unit)) {
            Toast.makeText(getContext(), "所属单位不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        xiangqing = safety_xq.getText().toString().trim();
        if (TextUtils.isEmpty(xiangqing)) {
            Toast.makeText(getContext(), "详情不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        fxunit = safety_fxunit.getText().toString().trim();
        if (TextUtils.isEmpty(fxunit)) {
            Toast.makeText(getContext(), "发现不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        shangbao = safety_shangbao.getText().toString().trim();
        if (TextUtils.isEmpty(shangbao)) {
            Toast.makeText(getContext(), "是否上报不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (shangbao.equals("是")||shangbao.equals("否")){

        }else {
            Toast.makeText(getContext(), "是否上报只能填“是”和”否", Toast.LENGTH_SHORT).show();
            return;
        }

        zhenggai = safety_zhenggai.getText().toString().trim();
        if (TextUtils.isEmpty(zhenggai)) {
            Toast.makeText(getContext(), "是否整改不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (zhenggai.equals("是")||zhenggai.equals("否")){

        }else {
            Toast.makeText(getContext(), "是否上报只能填“是”和”否", Toast.LENGTH_SHORT).show();
            return;
        }
        zgtime = safety_zgtime.getText().toString().trim();
        if (TextUtils.isEmpty(zgtime)) {
            Toast.makeText(getContext(), "整改时间不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectList.size()==0){
            Toast.makeText(getContext(), "请选择上传图片！", Toast.LENGTH_SHORT).show();
            return;
        }
        set_data_into_server();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sg_add_submit:
                submit();
                break;
            case R.id.sg_select_time:
                DatePickDialog dialog = new DatePickDialog(getActivity());
                //设置上下年分限制
                dialog.setYearLimt(5);
                //设置标题
                dialog.setTitle("选择时间");
                //设置类型
                dialog.setType(DateType.TYPE_ALL);
                //设置消息体的显示格式，日期格式
                dialog.setMessageFormat("yyyy-MM-dd HH:mm");
                //设置点击确定按钮回调
                dialog.setOnSureLisener(new OnSureLisener() {
                    @Override
                    public void onSure(Date date) {
                        @SuppressLint("SimpleDateFormat") String string=new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(date);
                        safety_time.setText(string);
                    }
                });
                dialog.show();
                break;
        }
    }

    String time,paddr,licheng,unit,fxunit,shangbao,zhenggai,zgtime;
    private void set_data_into_server() {
        PostFormBuilder builder=new PostFormBuilder();
        for (int i = 0; i < selectList.size(); i++) {
            builder.addFile("file[]",new File(selectList.get(0).getPath()).getName(),new File(selectList.get(i).getPath()));
        }
        builder.url("https://api.jjedd.net:9000/v1/uploadImg")
                .addHeader("token",App.userInfo.getToken())
                .addHeader("user",App.userInfo.getUser().getUser())
                .addHeader("Content-Type","multipart/form-data")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id, int code) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        com.alibaba.fastjson.JSONObject json=JSON.parseObject(response);
                        DoNet doNet = new DoNet() {
                            @Override
                            public void doWhat(String response, int id) {
                                if (!GsonUtil.verifyResult_show(response)) {
                                    return;
                                }
                                UIUtils.t(JSON.parseObject(response).getString("message"),false,UIUtils.T_SUCCESS);
                                getActivity().finish();
                            }
                        };
                        com.alibaba.fastjson.JSONObject jsonObject=new com.alibaba.fastjson.JSONObject();
                        jsonObject.put("work_time",time);
                        jsonObject.put("biroad_id", biroad_id);
                        jsonObject.put("mileage",licheng);
                        jsonObject.put("biunitnature_id", biunitnature_id);
                        jsonObject.put("biorganization_id", biorganization_id);
                        if (shangbao.equals("是")) {
                            jsonObject.put("summit", "1");
                        }else {
                            jsonObject.put("summit", "0");
                        }
                        if (zhenggai.equals("是")) {
                            jsonObject.put("reform", "1");
                        }else {
                            jsonObject.put("reform", "0");
                        }
                        jsonObject.put("reform_time", zgtime);
                        jsonObject.put("pic", json.getJSONObject("data").getString("filepath"));
                        jsonObject.put("detail", xiangqing);
                        doNet.doPost(jsonObject,Consts.URL_AQYHPCADD, getActivity(), true);
                    }
                });
    }

    private List<LocalMedia> selectList=new ArrayList<>();
    @Override
    public void getPhoto(List<LocalMedia> selectList) {
        this.selectList.addAll(selectList);
        tv_photo.setText(String.format(getResources().getString(R.string.photo),this.selectList.size()+""));
        selectList.clear();
    }
}
