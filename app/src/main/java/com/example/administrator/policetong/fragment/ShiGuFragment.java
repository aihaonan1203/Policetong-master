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
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.activity.AccidentActivity;
import com.example.administrator.policetong.activity.ModulesActivity;
import com.example.administrator.policetong.activity.PreviewActivity;
import com.example.administrator.policetong.base.BaseActivity;
import com.example.administrator.policetong.base.BaseBean;
import com.example.administrator.policetong.base.BaseFragment;
import com.example.administrator.policetong.bean.EvenMsg;
import com.example.administrator.policetong.httppost.getNetInfo;
import com.example.administrator.policetong.network.Network;
import com.example.administrator.policetong.new_bean.AccidentBean;
import com.example.administrator.policetong.new_bean.JingBaoBean;
import com.example.administrator.policetong.utils.LoadingDialog;
import com.example.administrator.policetong.utils.NetworkChangeListener;
import com.example.administrator.policetong.utils.Utils;
import com.google.gson.Gson;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ShiGuFragment extends BaseFragment implements View.OnClickListener {

    private Button sg_add_submit;
    private EditText sg_time;
    private EditText sg_type;
    private EditText sg_canyu;
    private EditText sg_car_type;
    private EditText sg_shoushang;
    private EditText sg_chesun;
    private Button sg_select_time;
    private LinearLayout llyt_photo;
    private static String SD_CARD_TEMP_DIR;

    private File file;
    private Button btn_preview;
    private ImageView iv_take_photo;
    private TextView tv_photo;
    private ModulesActivity activity;
    private ImageView iv_right;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = (ModulesActivity) getActivity();
        View view=inflater.inflate(R.layout.fragment_shi_gu, container, false);
        view.setClickable(true);
        initView(view);
        return view;
    }

    private AccidentBean bean;
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void XX(AccidentBean bean) {
        this.bean = bean;
    }

    private void initView(View view) {
        sg_add_submit =view.findViewById(R.id.sg_add_submit);
        sg_add_submit.setOnClickListener(this);
        sg_time = (EditText) view.findViewById(R.id.sg_time);
        sg_type = (EditText) view.findViewById(R.id.sg_type);
        sg_canyu = (EditText) view.findViewById(R.id.jb_lingdao);
        sg_car_type = (EditText) view.findViewById(R.id.sg_car_type);
        sg_shoushang = (EditText) view.findViewById(R.id.sg_shoushang);
        sg_chesun = (EditText) view.findViewById(R.id.sg_chesun);
        sg_select_time = (Button) view.findViewById(R.id.sg_select_time);
        llyt_photo =view.findViewById(R.id.llyt_photo);
        sg_select_time.setOnClickListener(this);
        btn_preview = (Button) view.findViewById(R.id.btn_preview);
        iv_take_photo=view.findViewById(R.id.iv_take_photo);
        tv_photo=view.findViewById(R.id.tv_photo);
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
        iv_right = ModulesActivity.getmContext().findViewById(R.id.ac_tv_right);
        iv_right.setVisibility(View.VISIBLE);
        iv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),AccidentActivity.class));
            }
        });
        if (bean!=null&& activity.id==12){
            llyt_photo.setVisibility(View.INVISIBLE);
            sg_time.setText(bean.getDate());
            sg_type.setText(bean.getType());
            sg_canyu.setText(bean.getParticipant());
            sg_car_type.setText(bean.getCarType());
            sg_shoushang.setText(bean.getHurtType());
            sg_chesun.setText(bean.getCarAmage());
        }
    }


    private void submit() throws JSONException{
        assert activity != null;
        if (activity.j==null) {
            Toast.makeText(getActivity(), "位置信息获取失败，请检查网络", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectList==null||selectList.size()==0){
            if (activity.id!=12){
                Toast.makeText(getActivity(), "请先选择上传的图片!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // validate
        String time = sg_time.getText().toString().trim();
        if (TextUtils.isEmpty(time)) {
            Toast.makeText(getContext(), "时间不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String type = sg_type.getText().toString().trim();
        if (TextUtils.isEmpty(type)) {
            Toast.makeText(getContext(), "类型不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String canyu = sg_canyu.getText().toString().trim();
        if (TextUtils.isEmpty(canyu)) {
            Toast.makeText(getContext(), "参与方不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String car_type = sg_car_type.getText().toString().trim();
        if (TextUtils.isEmpty(car_type)) {
            Toast.makeText(getContext(), "车辆类型不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String shoushang = sg_shoushang.getText().toString().trim();
        if (TextUtils.isEmpty(shoushang)) {
            Toast.makeText(getContext(), "受伤情况不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String chesun = sg_chesun.getText().toString().trim();
        if (TextUtils.isEmpty(chesun)) {
            Toast.makeText(getContext(), "车损不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        AccidentBean bean=new AccidentBean();
        bean.setUserId(userInfo.getUserId());
        bean.setDate(time);
        bean.setLongitude(activity.j.getString("longitude"));
        bean.setLatitude(activity.j.getString("latitude"));
        bean.setType(type);
        bean.setParticipant(canyu);
        bean.setCarType(car_type);
        bean.setHurtType(shoushang);
        bean.setCarAmage(chesun);
        if (activity.id==12){
            bean.setId(this.bean.getId());
            bean.setOperate("2");
        }else {
            bean.setOperate("1");
        }
        String s = new Gson().toJson(bean);
        LoadingDialog.showDialog(getActivity(),"正在提交...");
        disposable=Network.getPoliceApi().addAccident(RequestBody.create(MediaType.parse("application/json"),s))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<BaseBean>() {
                    @Override
                    public void accept(BaseBean bean) throws Exception {
                        if (bean.getCode()==0&&activity.id==12){
                            LoadingDialog.disDialog();
                            Toast.makeText(getActivity(), "修改成功!", Toast.LENGTH_SHORT).show();
                            disposable.dispose();
                            EventBus.getDefault().post("1");
                            Objects.requireNonNull(getActivity()).finish();
                        }
                    }
                }).observeOn(Schedulers.io())
                .flatMap(new Function<BaseBean, ObservableSource<BaseBean>>() {
                    @Override
                    public ObservableSource<BaseBean> apply(BaseBean bean) throws Exception {
                        MultipartBody.Part[] part = new MultipartBody.Part[selectList.size()];
                        for (int i = 0; i < selectList.size(); i++) {
                            createFilePart(part, i, new File(selectList.get(i).getPath()));
                        }
                        return Network.getPoliceApi().uploadImage("accident/uploadImg",part);
                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<BaseBean>() {
                    @Override
                    public void accept(BaseBean bean) throws Exception {
                        if (bean.getCode()==0){
                            LoadingDialog.disDialog();
                            Toast.makeText(getActivity(), "提交成功...", Toast.LENGTH_SHORT).show();
                            Objects.requireNonNull(getActivity()).finish();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("accept: ","" );
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sg_add_submit:
                try {
                    submit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.sg_select_time:
                DatePickDialog dialog = new DatePickDialog(getActivity());
                //设置上下年分限制
                dialog.setYearLimt(5);
                //设置标题
                dialog.setTitle("选择时间");
                //设置类型
                dialog.setType(DateType.TYPE_YMD);
                //设置消息体的显示格式，日期格式
                dialog.setMessageFormat("yyyy-MM-dd");
                //设置点击确定按钮回调
                dialog.setOnSureLisener(new OnSureLisener() {
                    @Override
                    public void onSure(Date date) {
                        @SuppressLint("SimpleDateFormat") String string=new SimpleDateFormat("yyyy-MM-dd").format(date);
                        sg_time.setText(string);
                    }
                });
                dialog.show();
                break;
        }
    }

    @Override
    public void getPhoto(List<LocalMedia> selectList) {
        tv_photo.setText(String.format(getResources().getString(R.string.photo),selectList.size()+""));
    }
}
