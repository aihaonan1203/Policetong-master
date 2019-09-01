package com.example.administrator.policetong.fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.activity.ManageActivity;
import com.example.administrator.policetong.base.App;
import com.example.administrator.policetong.base.BaseFragment;
import com.example.administrator.policetong.base.Consts;
import com.example.administrator.policetong.bean.new_bean.PointBean;
import com.example.administrator.policetong.fragment.carpark.ParkActivity;
import com.example.administrator.policetong.network.DoNet;
import com.example.administrator.policetong.utils.GsonUtil;
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
 * A simple {@link Fragment} subclass.
 */
public class ParkManageFragment extends BaseFragment implements View.OnClickListener {

    private EditText etTime;
    private Button btnTime;
    private EditText etOwnerName;
    private EditText etOwnerPhone;
    private EditText etCarNo;
    private EditText etEngineNo;
    private EditText etFrameNo;
    private EditText etCause;
    private EditText etType;
    private Button btnType;
    private EditText etPark;
    private Button btnPark;
    private TextView tv_photo;
    private Button iv_take_photo;
    private Button ds_add_submit;
    private int parkid;
    private int typeid;
    private String imageUrl;
    private RecyclerView mPhotoRecyclerView;
    private BaseQuickAdapter<LocalMedia,BaseViewHolder> photoAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_park_manage, container, false);
        view.setClickable(true);
        initView(view);
        initSelect();
        Objects.requireNonNull(getActivity()).findViewById(R.id.ac_tv_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ParkActivity.class));
            }
        });
        init();
        return view;
    }

    private void init() {
        photoAdapter=new BaseQuickAdapter<LocalMedia, BaseViewHolder>(R.layout.photo_item_layout) {
            @Override
            protected void convert(BaseViewHolder helper, LocalMedia item) {
                Glide.with(mContext).asBitmap().load(new File(item.getPath())).into((ImageView) helper.getView(R.id.ivImage));
            }
        };
        mPhotoRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        mPhotoRecyclerView.setAdapter(photoAdapter);
        photoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter mAdapter, View view, int position) {
                showPicture(photoAdapter.getData().get(position).getPath());
            }
        });
        photoAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("确认操作")
                        .setMessage("是否删除该张照片！")
                        .setPositiveButton("取消", null)
                        .setNegativeButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                photoAdapter.remove(position);
                                selectList.remove(position);
                                tv_photo.setText(String.format(getResources().getString(R.string.photo), selectList.size() + ""));
                                if (selectList.size()==0){
                                    mPhotoRecyclerView.setVisibility(View.GONE);
                                }
                            }
                        })
                        .create().show();
                return true;
            }
        });
    }

    private void submit() {

        final String cause = etCause.getText().toString().trim();
        final String engineNo = etEngineNo.getText().toString().trim();
        final String frameNo = etFrameNo.getText().toString().trim();
        final String ownerName = etOwnerName.getText().toString().trim();
        final String carNo = etCarNo.getText().toString().trim();
        final String ownerPhone = etOwnerPhone.getText().toString().trim();
        String park = etPark.getText().toString().trim();
        final String time = etTime.getText().toString().trim();
        String type = etType.getText().toString().trim();

        if (cause.isEmpty()||engineNo.isEmpty()||frameNo.isEmpty()||ownerPhone.isEmpty()||park.isEmpty()||time.isEmpty()||type.isEmpty()||carNo.isEmpty()||ownerName.isEmpty()){
            UIUtils.t("请把所有信息补充完整！！！",false,UIUtils.T_WARNING);
            return;
        }
        if (selectList.size()==0){
            UIUtils.t("请拍摄车辆照片！！！",false,UIUtils.T_WARNING);
            return;
        }

        setDialog();
        if (imageUrl==null||imageUrl.isEmpty()){
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
                            imageUrl = json.getJSONObject("data").getString("filepath");
                            submitData(time,ownerName,ownerName,carNo,engineNo,frameNo,cause);
                        }
                    });
        }else {
            submitData(time,ownerName,ownerName,carNo,engineNo,frameNo,cause);
        }


    }

    private void submitData(String time,String ownerName,String ownerPhone,String carNo,String engineNo,String frameNo,String cause) {
        DoNet doNet = new DoNet() {
            @Override
            public void doWhat(String response, int id) {
                closeDialog();
                if (!GsonUtil.verifyResult_show(response)) {
                    return;
                }
                UIUtils.t(JSON.parseObject(response).getString("message"), false, UIUtils.T_SUCCESS);
                Objects.requireNonNull(getActivity()).finish();
                startActivity(new Intent(getActivity(),ParkActivity.class));
            }
        };
        doNet.setOnErrorListener(new DoNet.OnErrorListener() {
            @Override
            public void onError(int code) {
                closeDialog();
                UIUtils.t("提交失败",false,UIUtils.T_ERROR);
            }
        });
        com.alibaba.fastjson.JSONObject jsonObject =new com.alibaba.fastjson.JSONObject();
        jsonObject.put("work_time",time);
        jsonObject.put("ownername", ownerName);
        jsonObject.put("tel", ownerPhone);
        jsonObject.put("parkid", parkid);
        jsonObject.put("plateno", carNo);
        jsonObject.put("typeid", typeid);
        jsonObject.put("engineno", engineNo);
        jsonObject.put("frameno", frameNo);
        jsonObject.put("indesc",cause );
        jsonObject.put("inpic", imageUrl);
        doNet.doPost(jsonObject, Consts.URL_CAR_IN, getActivity(), false);
    }

    private void initSelect() {
        Util.RequestOption(getActivity(), "biVehicle", new Util.OptionCallBack() {
            @Override
            public void CallBack(List<PointBean> list) {
                if (list.size() != 0) {
                    Util.setRadioDateIntoDialog(getActivity(), etType, btnType, list, new Util.SelectOpintCallBack() {
                        @Override
                        public void selectItem(int itemId) {
                            typeid =itemId;
                        }
                    });
                } else {
                    btnType.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getActivity(), "服务器没有数据，无法选择，请手动输入", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        Util.RequestOption(getActivity(), "biPark", new Util.OptionCallBack() {
            @Override
            public void CallBack(List<PointBean> list) {
                if (list.size() != 0) {
                    Util.setRadioDateIntoDialog(getActivity(), etPark, btnPark, list, new Util.SelectOpintCallBack() {
                        @Override
                        public void selectItem(int itemId) {
                            parkid =itemId;
                        }
                    });
                } else {
                    btnType.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getActivity(), "服务器没有数据，无法选择，请手动输入", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private List<LocalMedia> selectList = new ArrayList<>();
    @Override
    public void getPhoto(List<LocalMedia> selectList) {
        mPhotoRecyclerView.setVisibility(View.VISIBLE);
        this.selectList.addAll(selectList);
        tv_photo.setText(String.format(getResources().getString(R.string.photo), this.selectList.size() + ""));
        photoAdapter.addData(selectList);
        selectList.clear();
    }

    private void initView(View view) {
        mPhotoRecyclerView =  view.findViewById(R.id.mPhotoRecyclerView);
        etTime =  view.findViewById(R.id.etTime);
        btnTime =  view.findViewById(R.id.btnTime);
        etOwnerName = view.findViewById(R.id.etOwnerName);
        etOwnerPhone =  view.findViewById(R.id.etOwnerPhone);
        etCarNo =  view.findViewById(R.id.etCarNo);
        etEngineNo =  view.findViewById(R.id.etEngineNo);
        etFrameNo =  view.findViewById(R.id.etFrameNo);
        etCause =  view.findViewById(R.id.etCause);
        etType =  view.findViewById(R.id.etType);
        btnType =  view.findViewById(R.id.btnType);
        etPark =  view.findViewById(R.id.etPark);
        btnPark =  view.findViewById(R.id.btnPark);
        tv_photo =  view.findViewById(R.id.tv_photo);
        iv_take_photo =  view.findViewById(R.id.iv_take_photo);
        iv_take_photo.setOnClickListener(this);
        ds_add_submit =  view.findViewById(R.id.ds_add_submit);
        ds_add_submit.setOnClickListener(this);
        btnTime.setOnClickListener(this);

        tv_photo.setText(String.format(getResources().getString(R.string.photo),"0"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_take_photo:
                int num = photoAdapter.getData().size();
                if (num>=5){
                    UIUtils.t("您最多只能选择5张照片上传！",false,UIUtils.T_WARNING);
                    return;
                }
                takeOnePhoto(5-num);
                break;
            case R.id.ds_add_submit:
                submit();
                break;
            case R.id.btnTime:
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
                        etTime.setText(string);
                    }
                });
                dialog.show();
                break;
        }
    }

}
