package com.example.administrator.policetong.fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.activity.ManageActivity;
import com.example.administrator.policetong.fragment.manage.Accident_Manage;
import com.example.administrator.policetong.activity.LabelManageActivity;
import com.example.administrator.policetong.base.App;
import com.example.administrator.policetong.base.BaseFragment;
import com.example.administrator.policetong.base.Consts;
import com.example.administrator.policetong.bean.new_bean.PointBean;
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

public class ShiGuFragment extends BaseFragment implements View.OnClickListener {

    private Button sg_type_btn;
    private Button sg_shoushang_s;
    private Button sg_chesun_s;
    private Button sg_canyu_s;
    private EditText sg_time;
    private EditText ds_police;
    private EditText sg_type;
    private EditText sg_canyu;
    private EditText ds_context;
    private EditText sg_shoushang;
    private EditText sg_chesun;
    private ArrayList<String> idPoliceList=new ArrayList<>();
    private TextView tv_photo;
    private StringBuilder users_id=new StringBuilder();
    private int participant_id;
    private int injured_id;
    private int vehicledamage_id;
    private int accident_id;
    private RecyclerView mPhotoRecyclerView;
    private BaseQuickAdapter<LocalMedia,BaseViewHolder> photoAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_shi_gu, container, false);
        view.setClickable(true);
        initView(view);
        Objects.requireNonNull(getActivity()).findViewById(R.id.ac_tv_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),ManageActivity.class).putExtra("type",5));
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

    private void initView(View view) {
        mPhotoRecyclerView =  view.findViewById(R.id.mPhotoRecyclerView);
        Button sg_add_submit = view.findViewById(R.id.sg_add_submit);
        sg_add_submit.setOnClickListener(this);
        sg_time =  view.findViewById(R.id.sg_time);
        ds_police =  view.findViewById(R.id.ds_police);
        Button btn_ds_police = view.findViewById(R.id.btn_ds_police);
        sg_chesun_s =  view.findViewById(R.id.sg_chesun_s);
        sg_shoushang_s =  view.findViewById(R.id.sg_shoushang_s);
        sg_type =  view.findViewById(R.id.sg_type);
        sg_canyu =  view.findViewById(R.id.jb_lingdao);
        ds_context =  view.findViewById(R.id.ds_context);
        sg_shoushang =  view.findViewById(R.id.sg_shoushang);
        sg_canyu_s =  view.findViewById(R.id.sg_canyu_s);
        sg_chesun =  view.findViewById(R.id.sg_chesun);
        Button sg_select_time = view.findViewById(R.id.sg_select_time);
        sg_type_btn =view.findViewById(R.id.sg_type_btn);
        sg_select_time.setOnClickListener(this);
        Button iv_take_photo = view.findViewById(R.id.iv_take_photo);
        tv_photo=view.findViewById(R.id.tv_photo);
        tv_photo.setText(String.format(getResources().getString(R.string.photo),"0"));
        iv_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });
        iv_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeOnePhoto();
            }
        });

        Util.RequestOption(getActivity(), "biAccident", new Util.OptionCallBack() {
            @Override
            public void CallBack(List<PointBean> list) {
                if (list.size() != 0) {
                    Util.setRadioDateIntoDialog(getActivity(), sg_type,sg_type_btn , list, new Util.SelectOpintCallBack() {
                        @Override
                        public void selectItem(int itemId) {
                            accident_id = itemId;
                        }
                    });
                } else {
                    sg_type_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getActivity(), "服务器没有数据，无法选择，请手动输入", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        Util.RequestOption(getActivity(), "biParticipant", new Util.OptionCallBack() {
            @Override
            public void CallBack(List<PointBean> list) {
                if (list.size() != 0) {
                    Util.setRadioDateIntoDialog(getActivity(), sg_canyu,sg_canyu_s , list, new Util.SelectOpintCallBack() {
                        @Override
                        public void selectItem(int itemId) {
                            participant_id = itemId;
                        }
                    });
                } else {
                    sg_canyu_s.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getActivity(), "服务器没有数据，无法选择，请手动输入", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        Util.RequestOption(getActivity(), "biInjured", new Util.OptionCallBack() {
            @Override
            public void CallBack(List<PointBean> list) {
                if (list.size() != 0) {
                    Util.setRadioDateIntoDialog(getActivity(), sg_shoushang,sg_shoushang_s , list, new Util.SelectOpintCallBack() {
                        @Override
                        public void selectItem(int itemId) {
                            injured_id = itemId;
                        }
                    });
                } else {
                    sg_shoushang_s.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getActivity(), "服务器没有数据，无法选择，请手动输入", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        Util.RequestOption(getActivity(), "biVehicledamage", new Util.OptionCallBack() {
            @Override
            public void CallBack(List<PointBean> list) {
                if (list.size() != 0) {
                    Util.setRadioDateIntoDialog(getActivity(), sg_chesun,sg_chesun_s , list, new Util.SelectOpintCallBack() {
                        @Override
                        public void selectItem(int itemId) {
                            vehicledamage_id = itemId;
                        }
                    });
                } else {
                    sg_chesun_s.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getActivity(), "服务器没有数据，无法选择，请手动输入", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
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
        }
    }

    private void submit(){

        if (App.getInstance().getLocationBean()==null) {
            Toast.makeText(getActivity(), "位置信息获取失败", Toast.LENGTH_SHORT).show();
            return;
        }
        if ( selectList.size()==0){
            Toast.makeText(getActivity(), "请先选择上传的图片!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(sg_time.getText().toString().trim())) {
            Toast.makeText(getContext(), "时间不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty( sg_type.getText().toString().trim())) {
            Toast.makeText(getContext(), "类型不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(sg_canyu.getText().toString().trim())) {
            Toast.makeText(getContext(), "参与方不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (users_id.length()==0){
            Toast.makeText(getContext(), "出动警力不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(sg_shoushang.getText().toString().trim())) {
            Toast.makeText(getContext(), "受伤情况不能为空", Toast.LENGTH_SHORT).show();
            return;
        }


        if (TextUtils.isEmpty(sg_chesun.getText().toString().trim())) {
            Toast.makeText(getContext(), "车损情况不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(ds_context.getText().toString().trim())) {
            Toast.makeText(getContext(), "事故出警详情描述不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
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
                                startActivity(new Intent(getActivity(),ManageActivity.class).putExtra("type",5));
                            }
                        };
                        doNet.setOnErrorListener(new DoNet.OnErrorListener() {
                            @Override
                            public void onError(int code) {
                                closeDialog();
                            }
                        });
                        com.alibaba.fastjson.JSONObject jsonObject =new com.alibaba.fastjson.JSONObject();

                        jsonObject.put("work_time",sg_time.getText().toString());
                        jsonObject.put("users_id", users_id);
                        jsonObject.put("longitude", App.getInstance().getLocationBean().getLongitude());
                        jsonObject.put("latitude", App.getInstance().getLocationBean().getLatitude());
                        jsonObject.put("accident_id", String.valueOf(accident_id));
                        jsonObject.put("participant_id", String.valueOf(participant_id));
                        jsonObject.put("injured_id", String.valueOf(injured_id));
                        jsonObject.put("vehicledamage_id", String.valueOf(vehicledamage_id));
                        jsonObject.put("detail",ds_context.getText().toString() );
                        jsonObject.put("pic", json.getJSONObject("data").getString("filepath"));
                        doNet.doPost(jsonObject, Consts.URL_ACCIDENTADD, getActivity(), true);
                    }
                });
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
                dialog.setType(DateType.TYPE_YMDHM);
                //设置消息体的显示格式，日期格式
                dialog.setMessageFormat("yyyy-MM-dd hh:mm");
                //设置点击确定按钮回调
                dialog.setOnSureLisener(new OnSureLisener() {
                    @Override
                    public void onSure(Date date) {
                        @SuppressLint("SimpleDateFormat") String string=new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
                        sg_time.setText(string);
                    }
                });
                dialog.show();
                break;
        }
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
}
