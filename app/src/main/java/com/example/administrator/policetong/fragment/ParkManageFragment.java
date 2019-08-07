package com.example.administrator.policetong.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.activity.ModulesActivity;
import com.example.administrator.policetong.activity.ParkActivity;
import com.example.administrator.policetong.activity.PreviewActivity;
import com.example.administrator.policetong.base.BaseActivity;
import com.example.administrator.policetong.base.BaseBean;
import com.example.administrator.policetong.base.BaseFragment;
import com.example.administrator.policetong.bean.EvenMsg;
import com.example.administrator.policetong.network.Network;
import com.example.administrator.policetong.utils.LoadingDialog;
import com.example.administrator.policetong.utils.Utils;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
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
public class ParkManageFragment extends BaseFragment implements View.OnClickListener {


    private ImageView iv_right;
    private EditText etParkTime;
    private Button btnParkTime;
    private EditText etParkCarType;
    private EditText etParkCarNumber;
    private EditText etParkType;
    private TextView tvParkPhoto;
    private ImageView ivTakePhoto;
    private Button btnPreview;
    private EditText tvParkRemark;
    private Button btnParkSubmit;
    private Button sgAddCancle;

    public ParkManageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_park_manage, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initListener();
    }

    private void initListener() {
        btnParkSubmit.setOnClickListener(this);
        btnParkTime.setOnClickListener(this);
        btnPreview.setOnClickListener(this);
        iv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),ParkActivity.class));
            }
        });
        ivTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });
        btnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BaseselectList == null || BaseselectList.size() == 0) {
                    Toast.makeText(getActivity(), "请先选择照片!!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                EventBus.getDefault().postSticky(new EvenMsg<>("", BaseselectList));
                startActivity(new Intent(getActivity(), PreviewActivity.class));
            }
        });
    }

    private void initView(View view) {
        iv_right = ModulesActivity.getmContext().findViewById(R.id.ac_tv_right);
        iv_right.setVisibility(View.VISIBLE);
        etParkTime = view.findViewById(R.id.et_park_time);
        btnParkTime = view.findViewById(R.id.btn_park_time);
        etParkCarType = view.findViewById(R.id.et_park_car_type);
        etParkCarNumber = view.findViewById(R.id.et_park_carNumber);
        etParkType = view.findViewById(R.id.et_park_type);
        tvParkPhoto = view.findViewById(R.id.tv_park_photo);
        ivTakePhoto = view.findViewById(R.id.iv_take_photo);
        btnPreview = view.findViewById(R.id.btn_preview);
        tvParkRemark = view.findViewById(R.id.tv_park_remark);
        btnParkSubmit = view.findViewById(R.id.btn_park_submit);
        sgAddCancle = view.findViewById(R.id.sg_add_cancle);
        tvParkPhoto.setText(String.format(getResources().getString(R.string.photo), "0"));
    }

    @Override
    public void getPhoto(List<LocalMedia> selectList) {
        tvParkPhoto.setText(String.format(getResources().getString(R.string.photo), selectList.size() + ""));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_park_time:
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
                        @SuppressLint("SimpleDateFormat") String string = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                        etParkTime.setText(string);
                    }
                });
                dialog.show();
                break;
            case R.id.btn_park_submit:
                submit();
                break;
        }
    }

    private void submit() {
        String carNumber = etParkCarNumber.getText().toString().trim();
        String carType = etParkCarType.getText().toString().trim();
        String time = etParkTime.getText().toString().trim();
        String type = etParkType.getText().toString().trim();
        String remark = tvParkRemark.getText().toString().trim();
        if (BaseselectList == null || BaseselectList.size() == 0) {
            Toast.makeText(getActivity(), "请先选择照片!!!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (carNumber.isEmpty() || carType.isEmpty() || time.isEmpty() || type.isEmpty()) {
            Toast.makeText(getActivity(), "请把信息补充完整!!!", Toast.LENGTH_SHORT).show();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("operate", "1");
        map.put("userId", userInfo.getUserId());
        map.put("entranceTime", Utils.getStringToDate(time,"yyyy-MM-dd HH:mm:ss")/1000);
        map.put("models", carType);
        map.put("license", carNumber);
        map.put("type", type);
        map.put("remark", remark);
        String s = new JSONObject(map).toString();
        LoadingDialog.showDialog(getActivity(),"正在提交表单");
        disposable = Network.getPoliceApi(false).setPark(RequestBody.create(MediaType.parse("application/json"),s ))
                .flatMap(new Function<BaseBean, ObservableSource<BaseBean>>() {
                    @Override
                    public ObservableSource<BaseBean> apply(BaseBean bean) throws Exception {
                        MultipartBody.Part[] part = new MultipartBody.Part[BaseselectList.size()];
                        for (int i = 0; i < BaseselectList.size(); i++) {
                            createFilePart(part, i, new File(BaseselectList.get(i).getPath()));
                        }
                        return Network.getPoliceApi(false).uploadImage("park/uploadEntranceImg", part);
                    }
                }).compose(BaseActivity.<BaseBean>applySchedulers()).subscribe(new Consumer<BaseBean>() {
                    @Override
                    public void accept(BaseBean bean) throws Exception {
                        LoadingDialog.disDialog();
                        Toast.makeText(getActivity(), "提交成功!", Toast.LENGTH_SHORT).show();
                        Objects.requireNonNull(getActivity()).finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LoadingDialog.disDialog();
                        Toast.makeText(getActivity(), "提交失败!", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
