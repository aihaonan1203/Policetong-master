package com.example.administrator.policetong.fragment;


import android.annotation.SuppressLint;
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
import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.activity.PreviewActivity;
import com.example.administrator.policetong.base.BaseActivity;
import com.example.administrator.policetong.base.BaseBean;
import com.example.administrator.policetong.base.BaseFragment;
import com.example.administrator.policetong.bean.EvenMsg;
import com.example.administrator.policetong.httppost.getNetInfo;
import com.example.administrator.policetong.network.Network;
import com.example.administrator.policetong.new_bean.StudyBean;
import com.example.administrator.policetong.new_bean.ZDBean;
import com.example.administrator.policetong.utils.LoadingDialog;
import com.example.administrator.policetong.utils.NetworkChangeListener;
import com.google.gson.Gson;
import com.luck.picture.lib.entity.LocalMedia;

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
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class StuddyFragment extends BaseFragment implements View.OnClickListener {

    SharedPreferences sp;
    private EditText stu_study_time;
    private EditText stu_zd;
    private EditText stu_context;
    private Button stu_submit;
    private Button stu_study_time_btn;
    private Button btn_preview;
    private ImageView iv_take_photo;
    private TextView tv_photo;
    private EditText stuStudySite;
    private Map<Integer, String> map;

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
        getSqu();
        return view;
    }

    private void getSqu() {
        disposable = Network.getPoliceApi().getSqu()
                .compose(BaseActivity.<BaseBean<List<ZDBean>>>applySchedulers())
                .subscribe(new Consumer<BaseBean<List<ZDBean>>>() {
                    @SuppressLint("UseSparseArrays")
                    @Override
                    public void accept(BaseBean<List<ZDBean>> bean) throws Exception {
                        if (bean.getCode() != 0) {
                            Toast.makeText(getActivity(), "获取中队信息失败", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        List<ZDBean> data = bean.getData();
                        map = new HashMap<>();
                        for (int i = 0; i <data.size() ; i++) {
                            ZDBean zdBean = data.get(i);
                            map.put(zdBean.getId(),zdBean.getSquName());
                        }
                        stu_zd.setText(map.get(userInfo.getSquId()));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    private void initView(View view) {
        stu_study_time = (EditText) view.findViewById(R.id.stu_study_time);
        stuStudySite = (EditText) view.findViewById(R.id.stu_study_site);
        stu_zd = (EditText) view.findViewById(R.id.stu_zd);
        stu_context = (EditText) view.findViewById(R.id.stu_context);
        stu_submit = (Button) view.findViewById(R.id.stu_submit);
        stu_study_time_btn = (Button) view.findViewById(R.id.stu_study_time_btn);
        btn_preview = (Button) view.findViewById(R.id.btn_preview);
        iv_take_photo = view.findViewById(R.id.iv_take_photo);
        tv_photo = view.findViewById(R.id.tv_photo);
        stu_submit.setOnClickListener(this);
        sp = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        tv_photo.setText(String.format(getResources().getString(R.string.photo), "0"));
        iv_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });
        btn_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectList == null || selectList.size() == 0) {
                    Toast.makeText(getActivity(), "请先选择照片!!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                EventBus.getDefault().postSticky(new EvenMsg<>("", selectList));
                startActivity(new Intent(getActivity(), PreviewActivity.class));
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
                dialog.setType(DateType.TYPE_YMD);
                //设置消息体的显示格式，日期格式
                dialog.setMessageFormat("yyyy-MM-dd hh:mm:ss");
                //设置点击确定按钮回调
                dialog.setOnSureLisener(new OnSureLisener() {
                    @Override
                    public void onSure(Date date) {
                        @SuppressLint("SimpleDateFormat") String string=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
                        stu_study_time.setText(string);
                    }
                });
                dialog.show();
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

        if (selectList == null || selectList.size() == 0) {
            Toast.makeText(getContext(), "请先选择图片!!", Toast.LENGTH_SHORT).show();
            return;
        }
        String site = stuStudySite.getText().toString().trim();
        if (TextUtils.isEmpty(site)) {
            Toast.makeText(getContext(), "学习地点不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String time = stu_study_time.getText().toString().trim();
        if (TextUtils.isEmpty(time)) {
            Toast.makeText(getContext(), "学习时间不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String zd = stu_zd.getText().toString().trim();
        if (TextUtils.isEmpty(zd)) {
            Toast.makeText(getContext(), "所属中队能为空", Toast.LENGTH_SHORT).show();
            return;
        }


        String context = stu_context.getText().toString().trim();
        if (TextUtils.isEmpty(context)) {
            Toast.makeText(getContext(), "学习内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showDialog(getActivity(), "正在提交...");
        StudyBean bean=new StudyBean(userInfo.getUserId(),context,site,time,userInfo.getSquId(),"1");
        String s = new Gson().toJson(bean);
        disposable=Network.getPoliceApi().addStudy(RequestBody.create(MediaType.parse("application/json"),s))
                .flatMap(new Function<BaseBean, ObservableSource<BaseBean>>() {
                    @Override
                    public ObservableSource<BaseBean> apply(BaseBean bean) throws Exception {
                        MultipartBody.Part[] part = new MultipartBody.Part[selectList.size()];
                        for (int i = 0; i < selectList.size(); i++) {
                            createFilePart(part, i, new File(selectList.get(i).getPath()));
                        }
                        return Network.getPoliceApi().uploadImage("study/uploadImg",part);
                    }
                }).compose(BaseActivity.<BaseBean>applySchedulers()).subscribe(new Consumer<BaseBean>() {
                    @Override
                    public void accept(BaseBean bean) throws Exception {
                        if (bean.getCode()==0){
                            LoadingDialog.disDialog();
                            Toast.makeText(getActivity(), "提交成功...", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LoadingDialog.disDialog();
                        Toast.makeText(getActivity(), "提交失败...", Toast.LENGTH_SHORT).show();
                        Log.e("accept: ","" );
                    }
                });
    }

    private void setDataToService(String name, String zd, String time, String context) {
        Map info = new HashMap();
        info.put("username", name);
        info.put("userid", sp.getString("userid", ""));
        info.put("date", time);
        info.put("content", context);
        info.put("detachment", zd);
        getNetInfo.NetInfo(getActivity(), "insertstudys", new JSONObject(info), new getNetInfo.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject object) throws JSONException {
                Log.e("onSuccess: ", object.toString());
                if (object.getString("RESULT").equals("S")) {
                    Toast.makeText(getActivity(), "提交成功", Toast.LENGTH_SHORT).show();
                    LoadingDialog.disDialog();
                    getActivity().finish();
                } else {
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
        tv_photo.setText(String.format(getResources().getString(R.string.photo), selectList.size() + ""));
    }

}
