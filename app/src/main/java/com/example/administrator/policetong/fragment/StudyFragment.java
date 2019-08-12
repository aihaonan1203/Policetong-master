package com.example.administrator.policetong.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.activity.LabelManageActivity;
import com.example.administrator.policetong.activity.ManageActivity;
import com.example.administrator.policetong.activity.PreviewActivity;
import com.example.administrator.policetong.activity.pass_card.more.AssignActivity;
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
import com.example.administrator.policetong.new_bean.StudyBean;
import com.example.administrator.policetong.new_bean.ZDBean;
import com.example.administrator.policetong.utils.GsonUtil;
import com.example.administrator.policetong.utils.LoadingDialog;
import com.example.administrator.policetong.utils.UIUtils;
import com.example.administrator.policetong.utils.Util;
import com.google.gson.Gson;
import com.luck.picture.lib.entity.LocalMedia;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudyFragment extends BaseFragment implements View.OnClickListener {

    private EditText stu_study_time;
    private EditText stu_zd;
    private EditText stu_context;
    private Button btn_study_zd;
    private TextView tv_photo;
    private EditText stuStudySite;
    private EditText ds_police;
    private ArrayList<String> idPoliceList=new ArrayList<>();
    private StringBuilder users_id=new StringBuilder();
    private int department_id;

    public StudyFragment() {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_studdy, container, false);
        view.setClickable(true);
        initView(view);
        getDpt(true);
        Objects.requireNonNull(getActivity()).findViewById(R.id.ac_tv_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),ManageActivity.class).putExtra("type",6));
            }
        });
        return view;
    }

    public void getDpt(boolean needDialog) {
        DoNet doNet = new DoNet() {
            @Override
            public void doWhat(String response, int id) {
                Log.e("doWhat: ", response);
                if (!GsonUtil.verifyResult_show(response)){
                    return;
                }
                List<PointBean> list = GsonUtil.parseJsonArrayWithGson(JSON.parseObject(response).getJSONObject("data").getJSONArray("info").toString(), PointBean.class);
                Util.setRadioDateIntoDialog(getActivity(), stu_zd, btn_study_zd, list, new Util.SelectOpintCallBack() {
                    @Override
                    public void selectItem(int itemId) {
                        department_id = itemId;
                    }
                });
            }
        };
        doNet.setOnErrorListener(new DoNet.OnErrorListener() {
            @Override
            public void onError(int code) {

            }
        });
        doNet.doGet(Consts.URL_GETDPT, getActivity(), needDialog);
    }

    private void initView(View view) {
        stu_study_time =  view.findViewById(R.id.stu_study_time);
        stuStudySite =  view.findViewById(R.id.stu_study_site);
        ds_police =  view.findViewById(R.id.ds_police);
        stu_zd =  view.findViewById(R.id.stu_zd);
        stu_context =  view.findViewById(R.id.stu_context);
        Button stu_submit = view.findViewById(R.id.ds_add_submit);
        Button stu_study_time_btn = view.findViewById(R.id.stu_study_time_btn);
        btn_study_zd =  view.findViewById(R.id.btn_study_zd);
        Button iv_take_photo = view.findViewById(R.id.iv_take_photo);
        tv_photo = view.findViewById(R.id.tv_photo);
        stu_submit.setOnClickListener(this);
        tv_photo.setText(String.format(getResources().getString(R.string.photo), "0"));
        iv_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeOnePhoto();
            }
        });

        stu_study_time_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                        stu_study_time.setText(string);
                    }
                });
                dialog.show();
            }
        });
        view.findViewById(R.id.btn_ds_police).setOnClickListener(new View.OnClickListener() {
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
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ds_add_submit:
                submit();
                break;
        }
    }

    private void submit() {

        final String site = stuStudySite.getText().toString().trim();
        if (TextUtils.isEmpty(site)) {
            Toast.makeText(getContext(), "学习地点不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(stu_study_time.getText().toString().trim())) {
            Toast.makeText(getContext(), "学习时间不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String zd = stu_zd.getText().toString().trim();
        if (TextUtils.isEmpty(zd)) {
            Toast.makeText(getContext(), "所属中队不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(ds_police.getText().toString().trim())) {
            Toast.makeText(getContext(), "参与学习人员不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        final String context = stu_context.getText().toString().trim();
        if (TextUtils.isEmpty(context)) {
            Toast.makeText(getContext(), "学习内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectList == null || selectList.size() == 0) {
            Toast.makeText(getContext(), "请先选择图片!!", Toast.LENGTH_SHORT).show();
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
                                startActivity(new Intent(getActivity(),ManageActivity.class).putExtra("type",6));
                            }
                        };
                        doNet.setOnErrorListener(new DoNet.OnErrorListener() {
                            @Override
                            public void onError(int code) {
                                closeDialog();
                            }
                        });
                        com.alibaba.fastjson.JSONObject jsonObject =new com.alibaba.fastjson.JSONObject();
                        jsonObject.put("work_time",stu_study_time.getText().toString());
                        jsonObject.put("users_id", users_id);
                        jsonObject.put("department_id", department_id);
                        jsonObject.put("place", site);
                        jsonObject.put("users", users_id);
                        jsonObject.put("content", context);
                        jsonObject.put("pic", json.getJSONObject("data").getString("filepath"));
                        doNet.doPost(jsonObject, Consts.URL_STUDYADD, getActivity(), false);
                    }
                });
    }


    private List<LocalMedia> selectList=new ArrayList<>();
    @Override
    public void getPhoto(List<LocalMedia> selectList) {
        tv_photo.setText(String.format(getResources().getString(R.string.photo), selectList.size() + ""));
        this.selectList.addAll(selectList);
        selectList.clear();
    }
}
