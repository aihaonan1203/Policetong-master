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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.activity.ModulesActivity;
import com.example.administrator.policetong.activity.PreviewActivity;
import com.example.administrator.policetong.base.BaseFragment;
import com.example.administrator.policetong.bean.EvenMsg;
import com.example.administrator.policetong.httppost.getNetInfo;
import com.example.administrator.policetong.utils.LoadingDialog;
import com.example.administrator.policetong.utils.NetworkChangeListener;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShiGuFragment extends BaseFragment implements View.OnClickListener {

    private Button sg_add_submit;
    private EditText sg_time;
    private EditText sg_type;
    private EditText sg_canyu;
    private EditText sg_car_type;
    private EditText sg_shoushang;
    private EditText sg_chesun;
    private Button sg_select_time;
    private static String SD_CARD_TEMP_DIR;

    private File file;
    private Button btn_preview;
    private ImageView iv_take_photo;
    private TextView tv_photo;
    private ModulesActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_shi_gu, container, false);
        view.setClickable(true);
        initView(view);
        return view;
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
        sg_select_time.setOnClickListener(this);
        sg_time.setText(LoadingDialog.getTime());
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
    }


    private void submit() {
        activity = (ModulesActivity) getActivity();
        assert activity != null;
        if (activity.j==null) {
            Toast.makeText(getActivity(), "位置信息获取失败，请检查网络", Toast.LENGTH_SHORT).show();
            return;
        }
        // validate
        time = sg_time.getText().toString().trim();
        if (TextUtils.isEmpty(time)) {
            Toast.makeText(getContext(), "时间不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        paddr = sg_type.getText().toString().trim();
        if (TextUtils.isEmpty(paddr)) {
            Toast.makeText(getContext(), "道路不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        licheng = sg_canyu.getText().toString().trim();
        if (TextUtils.isEmpty(licheng)) {
            Toast.makeText(getContext(), "里程不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        unit = sg_car_type.getText().toString().trim();
        if (TextUtils.isEmpty(unit)) {
            Toast.makeText(getContext(), "所属单位不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        fxunit = sg_shoushang.getText().toString().trim();
        if (TextUtils.isEmpty(fxunit)) {
            Toast.makeText(getContext(), "发现不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        shangbao = sg_chesun.getText().toString().trim();
        if (TextUtils.isEmpty(shangbao)) {
            Toast.makeText(getContext(), "是否上报不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (NetworkChangeListener.onAvailable){
            showDialog("请选择是否要上传照片");
        }else {
            Toast.makeText(getActivity(), "您的网络出现了问题", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.stu_submit:
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
                        sg_time.setText(string);
                    }
                });
                dialog.show();
                break;
        }
    }


    private void showDialog(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("选择");
        builder.setMessage(str);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                LoadingDialog.showDialog(getActivity(),"正在提交...");
                set_data_into_server();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CAMERA},
                            1);
                }else {
                    SharedPreferences sharedPreferences=getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                    file = new File(Environment.getExternalStorageDirectory()
                            + File.separator + sharedPreferences.getString("userid","")+LoadingDialog.getTime2()+".jpg");
                    file.getParentFile().mkdirs();
                    SD_CARD_TEMP_DIR = file.getPath();
                    dialog.dismiss();
                    Intent cameraIntent = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);
                    if (Build.VERSION.SDK_INT>=24){
                        //改变Uri  com.xykj.customview.fileprovider注意和xml中的一致
                        Uri uri = FileProvider.getUriForFile(getActivity(), "com.example.administrator.policetong.fileprovider", file);
                        //添加权限
                        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        startActivityForResult(cameraIntent, 1);
                    }else{
                        SD_CARD_TEMP_DIR = Environment.getExternalStorageDirectory()
                                + File.separator + sharedPreferences.getString("userid","")+LoadingDialog.getTime2()+".jpg";//设定照相后保存的文件名，类似于缓存文件
                        dialog.dismiss();
                        cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(new File(SD_CARD_TEMP_DIR)));
                        startActivityForResult(cameraIntent, 1);
                    }

                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }
    String time,paddr,licheng,unit,fxunit,shangbao,zhenggai,zgtime;
    private void set_data_into_server() {
        Map info=new HashMap();
        SharedPreferences sp=getActivity().getSharedPreferences("userinfo",Context.MODE_PRIVATE);
        info.put("username",sp.getString("username",""));
        info.put("userid",sp.getString("userid",""));
        info.put("date",time);
        info.put("road",paddr);
        info.put("distance",licheng);
        info.put("asunit",unit);
        info.put("fdunit",fxunit);
        info.put("report",shangbao);
        info.put("img","");
        info.put("rectify",zhenggai);
        info.put("rectifydate",zgtime);
        info.put("group",sp.getString("group",""));
        info.put("detachment",sp.getString("detachment",""));
        getNetInfo.NetInfo(getActivity(), "insertdanger", new JSONObject(info), new getNetInfo.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject object) throws JSONException {
                Log.e("onSuccess: ",object.toString() );
                if (object.getString("RESULT").equals("S")){
                    Toast.makeText(getActivity(), "提交成功", Toast.LENGTH_SHORT).show();
                    LoadingDialog.disDialog();
                    getActivity().finish();
                }else {
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
        tv_photo.setText(String.format(getResources().getString(R.string.photo),selectList.size()+""));
    }
}
