package com.example.administrator.policetong.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.base.BaseActivity;
import com.example.administrator.policetong.base.BaseBean;
import com.example.administrator.policetong.bean.EvenMsg;
import com.example.administrator.policetong.network.Network;
import com.example.administrator.policetong.new_bean.CarBean;
import com.example.administrator.policetong.utils.LoadingDialog;
import com.example.administrator.policetong.utils.Utils;
import com.google.gson.Gson;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ParkOutActivity extends BaseActivity implements View.OnClickListener, BaseActivity.PhotoCallBack {

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
    private ImageView acArrowBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park_out);
        initView();
        initListener();
    }


    private void initListener() {
        setPhoto(this);
        btnParkSubmit.setOnClickListener(this);
        btnParkTime.setOnClickListener(this);
        btnPreview.setOnClickListener(this);
        ivTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });
        btnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectList == null || selectList.size() == 0) {
                    Toast.makeText(ParkOutActivity.this, "请先选择照片!!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                EventBus.getDefault().postSticky(new EvenMsg<>("", selectList));
                startActivity(new Intent(ParkOutActivity.this, PreviewActivity.class));
            }
        });
    }

    private CarBean carBean;

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void XXX(CarBean carBean) {
        this.carBean = carBean;
    }

    private void initView() {
        etParkTime = findViewById(R.id.et_park_time);
        btnParkTime = findViewById(R.id.btn_park_time);
        etParkCarType = findViewById(R.id.et_park_car_type);
        etParkCarNumber = findViewById(R.id.et_park_carNumber);
        etParkType = findViewById(R.id.et_park_type);
        tvParkPhoto = findViewById(R.id.tv_park_photo);
        ivTakePhoto = findViewById(R.id.iv_take_photo);
        btnPreview = findViewById(R.id.btn_preview);
        tvParkRemark = findViewById(R.id.tv_park_remark);
        btnParkSubmit = findViewById(R.id.btn_park_submit);
        sgAddCancle = findViewById(R.id.sg_add_cancle);

        if (carBean != null) {
            etParkCarNumber.setText(carBean.getLicence());
            etParkCarType.setText(carBean.getModels());
            etParkType.setText(carBean.getType());
        }
        tvParkPhoto.setText(String.format(getResources().getString(R.string.photo), "0"));

        acArrowBack = findViewById(R.id.ac_arrow_back);
        acArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_park_time:
                DatePickDialog dialog = new DatePickDialog(ParkOutActivity.this);
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
        if (selectList == null || selectList.size() == 0) {
            Toast.makeText(ParkOutActivity.this, "请先选择照片!!!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (carNumber.isEmpty() || carType.isEmpty() || time.isEmpty() || type.isEmpty()) {
            Toast.makeText(ParkOutActivity.this, "请把信息补充完整!!!", Toast.LENGTH_SHORT).show();
        }
//        Map<String, Object> map = new HashMap<>();
//        map.put("operate", "1");
//        map.put("userId", userInfo.getUserId());
//        map.put("entranceTime", Utils.getStringToDate(time, "yyyy-MM-dd HH:mm:ss") / 1000);
//        map.put("models", carType);
//        map.put("license", carNumber);
//        map.put("type", type);
//        map.put("remark", remark);
//        String s = new JSONObject(map).toString();
        carBean.setLeaveLicense(remark);
        carBean.setLeaveTime(Utils.getStringToDate(time, "yyyy-MM-dd HH:mm:ss")/1000);
        carBean.setOperate("2");
        String json = new Gson().toJson(carBean);
        LoadingDialog.showDialog(ParkOutActivity.this, "正在提交表单");
        disposable = Network.getPoliceApi().setPark(RequestBody.create(MediaType.parse("application/json"),json ))
                .flatMap(new Function<BaseBean, ObservableSource<BaseBean>>() {
                    @Override
                    public ObservableSource<BaseBean> apply(BaseBean bean) throws Exception {
                        MultipartBody.Part[] part = new MultipartBody.Part[selectList.size()];
                        for (int i = 0; i < selectList.size(); i++) {
                            createFilePart(part, i, new File(selectList.get(i).getPath()));
                        }
                        return Network.getPoliceApi().uploadImage("park/uploadLeaveImg", part);
                    }
                }).compose(BaseActivity.<BaseBean>applySchedulers()).subscribe(new Consumer<BaseBean>() {
                    @Override
                    public void accept(BaseBean bean) throws Exception {
                        LoadingDialog.disDialog();
                        Toast.makeText(ParkOutActivity.this, "提交成功!", Toast.LENGTH_SHORT).show();
                        ParkOutActivity.this.finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LoadingDialog.disDialog();
                        Toast.makeText(ParkOutActivity.this, "提交失败!", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    //创建Multipart, fieldName为表单字段名
    public static void createFilePart(MultipartBody.Part[] part, int i, File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        part[i] = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
    }

    List<LocalMedia> selectList;

    @Override
    public void getPhoto(List<LocalMedia> selectList) {
        this.selectList = selectList;
        tvParkPhoto.setText(String.format(getResources().getString(R.string.photo), selectList.size() + ""));
    }
}
