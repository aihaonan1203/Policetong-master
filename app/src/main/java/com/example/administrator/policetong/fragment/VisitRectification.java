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

import com.android.volley.VolleyError;
import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.example.administrator.policetong.BuildConfig;
import com.example.administrator.policetong.MainActivity;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.activity.ModulesActivity;
import com.example.administrator.policetong.activity.PreviewActivity;
import com.example.administrator.policetong.base.BaseFragment;
import com.example.administrator.policetong.bean.EvenMsg;
import com.example.administrator.policetong.httppost.getNetInfo;
import com.example.administrator.policetong.utils.LoadingDialog;
import com.example.administrator.policetong.utils.NetworkChangeListener;
import com.example.administrator.policetong.utils.FileUtils;
import com.example.administrator.policetong.utils.Util;
import com.luck.picture.lib.entity.LocalMedia;

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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class VisitRectification extends BaseFragment implements View.OnClickListener {

    private Button btn_preview;
    private ImageView iv_take_photo;
    private TextView tv_photo;
    private EditText visit_unitname;
    private EditText visit_unit;
    private EditText visit_purpose;
    private EditText visit_context;
    private EditText visit_time;
    private Button visit_submit,vr_danwei,vr_mudi,vr_xz,visit_select_time;
    private static String SD_CARD_TEMP_DIR;
    public VisitRectification() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_visitrectification_add, container, false);
        view.setClickable(true);
        initView(view);
        ModulesActivity modulesActivity = (ModulesActivity) getActivity();
        modulesActivity.shilihua(new ModulesActivity.Myjiekou() {
            @Override
            public void callback(String ooo) {
                Log.e("onActivityResult: ",file.getPath()+"      "+file.getName() );
                LoadingDialog.showDialog(getActivity(),"正在上传");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        formUpload("http://" + Util.loadSetting(getActivity()).getUrl() + ":"+Util.loadSetting(getActivity()).getUrlPort()+"/pointsman/uploadimg",SD_CARD_TEMP_DIR);
                    }
                }).start();
            }
        });
        return view;
    }

    private void initView(View view) {
        visit_unitname = (EditText) view.findViewById(R.id.visit_unitname);
        visit_unit = (EditText) view.findViewById(R.id.visit_unit);
        visit_purpose = (EditText) view.findViewById(R.id.visit_purpose);
        visit_context = (EditText) view.findViewById(R.id.visit_context);
        visit_submit = (Button) view.findViewById(R.id.visit_submit);
        vr_danwei = (Button) view.findViewById(R.id.vr_unit);
        vr_mudi = (Button) view.findViewById(R.id.vr_mudi);
        vr_xz = (Button) view.findViewById(R.id.vr_unit_xz);
        visit_time=view.findViewById(R.id.visit_time);
        visit_select_time=view.findViewById(R.id.vr_select_time);
        visit_submit.setOnClickListener(this);
        visit_select_time.setOnClickListener(this);
        visit_time.setText(LoadingDialog.getTime());
        btn_preview = (Button) view.findViewById(R.id.btn_preview);
        iv_take_photo=view.findViewById(R.id.iv_take_photo);
        tv_photo=view.findViewById(R.id.tv_photo);
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
        Util.RequestOption(getActivity(), "option1", new Util.RequestOptionCallBack() {
            @Override
            public void CallBack(List<String> list) {
                if (list.size()!=0){
                    int size = list.size();
                    String[] arr =list.toArray(new String[size]);
                    Util.setRadioDateIntoDialog(getActivity(),visit_unitname,vr_danwei,arr);
                }else {
                    vr_danwei.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getActivity(), "服务器没有数据，无法选择，请手动输入", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                Util.RequestOption(getActivity(), "option4", new Util.RequestOptionCallBack() {
                    @Override
                    public void CallBack(List<String> list) {
                        if (list.size()!=0){
                            int size = list.size();
                            String[] arr =list.toArray(new String[size]);
                            Util.setRadioDateIntoDialog(getActivity(),visit_purpose,vr_mudi,arr);
                        }else {
                            vr_mudi.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(getActivity(), "服务器没有数据，无法选择，请手动输入", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        Util.RequestOption(getActivity(), "option3", new Util.RequestOptionCallBack() {
                            @Override
                            public void CallBack(List<String> list) {
                                if (list.size()!=0){
                                    int size = list.size();
                                    String[] arr =list.toArray(new String[size]);
                                    Util.setRadioDateIntoDialog(getActivity(),visit_unit,vr_xz,arr);
                                }else {
                                    vr_mudi.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Toast.makeText(getActivity(), "服务器没有数据，无法选择，请手动输入", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });
                    }
                });
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
                dialog.setType(DateType.TYPE_ALL);
                //设置消息体的显示格式，日期格式
                dialog.setMessageFormat("yyyy-MM-dd HH:mm");
                //设置点击确定按钮回调
                dialog.setOnSureLisener(new OnSureLisener() {
                    @Override
                    public void onSure(Date date) {
                        @SuppressLint("SimpleDateFormat") String string=new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(date);
                        visit_time.setText(string);
                    }
                });
                dialog.show();
                break;
        }
    }
    String unitname,context,purpose,unit;
    private void submit() {
        // validate
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
        if (NetworkChangeListener.onAvailable){
            showDialog("请选择是否要上传照片");
        }else {
            Toast.makeText(getActivity(), "您的网络出现了问题", Toast.LENGTH_SHORT).show();
        }

    }


    private File file;
    private String photoPath;
    String img="";
    private void showDialog(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("选择");
        builder.setMessage(str);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                LoadingDialog.showDialog(getActivity(),"正在提交...");
                get_data_form_server();
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

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    LoadingDialog.disDialog();
                    Toast.makeText(getActivity(), "上传成功", Toast.LENGTH_SHORT).show();
                    showDialog("上传成功,请选择是否要继续上传照片");
                    img=img+photo_name+"/";
                    break;
                case 2:
                    LoadingDialog.disDialog();
                    Toast.makeText(getActivity(), "上传失败,请检查网络", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    public void get_data_form_server() {
        Map info=new HashMap();
        SharedPreferences sp=getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        info.put("username",sp.getString("username",""));
        info.put("userid",sp.getString("userid",""));
        info.put("unit",unitname);
        info.put("unitnature",unit);
        info.put("objective",purpose);
        info.put("content",context);
        info.put("img",img);
        info.put("date",LoadingDialog.getTime());
        info.put("group",sp.getString("group",""));
        info.put("detachment",sp.getString("detachment",""));
        getNetInfo.NetInfo(getActivity(), "insertvisit", new JSONObject(info), new getNetInfo.VolleyCallback() {
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

    /**
     * 文件上传
     *
     * @param urlStr   接口路径
     * @param filePath 本地图片路径
     * @return
     */
    String photo_name;
    public String formUpload(String urlStr, String filePath) {
        String rsp = "";
        int rews=0;
        HttpURLConnection conn;
        conn = null;
        String BOUNDARY = "|"; // request头和上传文件内容分隔符
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + BOUNDARY);
            OutputStream out = new DataOutputStream(conn.getOutputStream());
            File file = new File(filePath);
            String filename = file.getName();
            photo_name=filename;
            Log.e("formUpload: ", filename);
            Log.e("formUpload: ", file.getPath());
            String contentType = "";
            if (filename.endsWith(".png")) {
                contentType = "image/png";
            }
            if (filename.endsWith(".jpg")) {
                contentType = "image/jpg";
            }
            if (filename.endsWith(".gif")) {
                contentType = "image/gif";
            }
            if (filename.endsWith(".bmp")) {
                contentType = "image/bmp";
            }
            if (contentType == null || contentType.equals("")) {
                contentType = "application/octet-stream";
            }
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
            strBuf.append("Content-Disposition: form-data; name=\"" + filePath
                    + "\"; filename=\"" + filename + "\"\r\n");
            strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
            out.write(strBuf.toString().getBytes());
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            int bytes = 0;
            byte[] bufferOut = new byte[1024];
            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
            in.close();
            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            rews = conn.getResponseCode();
            Log.w("formUpload: ", rews+"==");
            out.close();

            // 读取返回数据
            StringBuffer buffer = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            rsp = buffer.toString();
            reader.close();
            reader = null;
        } catch (Exception e) {
            LoadingDialog.disDialog();
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        Log.e("formUpload: ", rsp);
        if (rews==200){
            handler.sendEmptyMessage(1);
        }else {
            handler.sendEmptyMessage(2);
        }
        return rsp;
    }

    @Override
    public void getPhoto(List<LocalMedia> selectList) {
        tv_photo.setText(String.format(getResources().getString(R.string.photo),selectList.size()+""));
    }
}
