package com.example.administrator.policetong.network;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;

import com.alibaba.fastjson.JSONObject;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.base.App;
import com.example.administrator.policetong.base.Consts;
import com.example.administrator.policetong.utils.GsonUtil;
import com.example.administrator.policetong.utils.UIUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;


import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;


/**
 * Created by Administrator on 2017/11/21 0021.
 */

public abstract class DoNet {

    public static final int DOGET = 0;
    public static final int DOPOST = 1;

    private Dialog dialog;
    private static boolean flag = true;
    private static boolean needShowNetError = true;
    private Context mContext;
    private static Handler handler;

    private Dialog mDialog;


    @SuppressLint("HandlerLeak")
    public DoNet() {

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                if ((msg.what == Consts.DONET || msg.what == Consts.TOAST)) {
                    switch (msg.what) {
                        case Consts.DONET:
                            flag = true;
                            break;
                        case Consts.TOAST:
                            needShowNetError = true;
                            break;

                    }


                }

            }
        };

    }


    public void setDialog(Boolean needDialog) {
        if (!needDialog) {
            return;
        }
        dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_loading);
        if (dialog.isShowing()) {
            return;
        }
        UIUtils.doDialog(mContext, dialog);
//        dialog.setCanceledOnTouchOutside(false);  //能取消
        dialog.setCancelable(false);
    }

    public void closeDialog() {
        if (dialog == null) {
            return;
        }
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        UIUtils.closeDialog(mContext, dialog);
    }

    public void setMDialog() {
        mDialog = new Dialog(mContext);
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            mDialog.setContentView(R.layout.dialog_loading);
            if (mDialog.isShowing()) {
            return;
        }
        UIUtils.doDialog(mContext, mDialog);
//        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
    }

    public void closeMDialog() {
//        if (mDialog == null) {
//            return;
//        }
//        if (mDialog.isShowing()) {
//            mDialog.dismiss();
//        }
        UIUtils.closeDialog(mContext, mDialog);
    }


    private JSONObject mJson;
    private String mUrl;
    private Boolean mNeedDialog;


    //--------------------------------------------------------网络连接失败了会尝试登陆一次---------------------------------//


    //post方法
    public void doPost(final JSONObject json, final String url, final Context context, final Boolean needDialog) {
        this.mJson = json;
        this.mUrl = url;
        this.mContext = context;
        this.mNeedDialog = needDialog;


        setDialog(needDialog);

        //头文件
        final String time = String.valueOf( new Date().getTime());
        Log.e("doPost: ",time );
        //设置无标题
        OkHttpUtils
                .postString()
                .url(url)
                .addHeader("token", App.userInfo.getToken())
//                .addHeader("time",time)
                .addHeader("user",String.valueOf(App.userInfo.getUser().getUser()))
                .content(json.toString())
                .mediaType(MediaType.parse("application/json;charset=utf-8"))
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                    }

                    @Override
                    public void onResponse(String response, int id) {


                        netMission(response,id,DOPOST);

//                        requestException(response,mUrl);
//
//                        if (TextUtils.isEmpty(accessToken)) {
//                            DoNet.this.doWhat(response, area_id);
//                            return;
//                        }
//                        //如果response是空
//                        if (TextUtils.isEmpty(response)) {
////                            UIUtils.t("返回为空", false, UIUtils.T_ERROR);
//                            return;
//                        }
//
//                        //如果失败 并且 是 已过期，并且登陆一次再执行dowhat
//                        if ((JsonUtil.jsonMsg(response)).contains("当前用户已过期")) {
//                            doLogin(DOPOST);
//                        } else {
//                             DoNet.this.doWhat(response, area_id);
//                        }
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        closeDialog();
                        if(null!= onAfterListener){
                            onAfterListener.doAfter();
                        }

                    }

                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id,int code) {
                        closeDialog();
                        closeMDialog();
                        if (onErrorListener != null) {
                            onErrorListener.onError(code);
                        }
                        if (needShowNetError) {
                            needShowNetError = false;
                            handler.sendEmptyMessageDelayed(Consts.TOAST, 5000);
//                            UIUtils.t("网络连接异常", false, UIUtils.T_ERROR);
                        }
                    }
                });
    }

    protected  void netMission(String response, int id, int mode){

        requestException(response,mUrl);

        //如果response是空
        if (TextUtils.isEmpty(response)) {
            return;
        }

        DoNet.this.doWhat(response, id);

    }

    protected void requestException(String response, String url) {

        try {
            if (!GsonUtil.verifyResult(response)) {
                throw new SuccessException(url);
            }
        } catch (SuccessException e) {
            Writer writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
        }

    }



    //get方法
    public void doGet(String url, final Context context, final Boolean needDialog) {
        this.mUrl = url;
        this.mContext = context;
        this.mNeedDialog = needDialog;

        //判断是否需要dialog
        setDialog(needDialog);


        OkHttpUtils
                .get()
                .url(url)
                .addHeader("token", App.userInfo.getToken())
                .addHeader("user",String.valueOf(App.userInfo.getUser().getUser()))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id,int code) {
                        closeDialog();
                        closeMDialog();
                        if (onErrorListener != null) {
                            onErrorListener.onError(code);
                        }
                        if (needShowNetError) {
                            needShowNetError = false;
                            handler.sendEmptyMessageDelayed(Consts.TOAST, 5000);
//                            UIUtils.t("连接服务器异常", false, UIUtils.T_ERROR);
                        }
                    }
                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        netMission(response,id,DOGET);
//                        requestException(response,mUrl);
//                        //如果 accessToken是空   表示在进行注册等操作  //如果
//                        if (TextUtils.isEmpty(accessToken)) {
//                            DoNet.this.doWhat(response, area_id);
////                            DoNet.this.doAfter(area_id);
//                            return;
//                        }
////如果response是空
//                        if (TextUtils.isEmpty(response)) {
//                            return;
//                        }
//
//                        //如果失败 并且 是 已过期   ，并且登陆一次再执行dowhat
//                        if ((JsonUtil.jsonMsg(response)).contains("当前用户已过期")) {
//                            doLogin(DOGET);
//                        } else {
//                            DoNet.this.doWhat(response, area_id);
////                            DoNet.this.doAfter(area_id);
//                        }
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        closeDialog();
                        if(null!= onAfterListener){
                            onAfterListener.doAfter();
                        }

                    }
                });

    }


    public void doPut(RequestBody requestBody, final String url, final Context context, final Boolean needDialog) {
        this.mUrl = url;
        this.mContext = context;
        this.mNeedDialog = needDialog;


        setDialog(needDialog);

        //设置无标题
        OkHttpUtils
                .put()
                .requestBody(requestBody)
                .url(url)
                .addHeader("token", App.userInfo.getToken())
                .addHeader("user",String.valueOf(App.userInfo.getUser().getUser()))
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                    }

                    @Override
                    public void onResponse(String response, int id) {


                        netMission(response,id,DOPOST);

//                        requestException(response,mUrl);
//
//                        if (TextUtils.isEmpty(accessToken)) {
//                            DoNet.this.doWhat(response, area_id);
//                            return;
//                        }
//                        //如果response是空
//                        if (TextUtils.isEmpty(response)) {
////                            UIUtils.t("返回为空", false, UIUtils.T_ERROR);
//                            return;
//                        }
//
//                        //如果失败 并且 是 已过期，并且登陆一次再执行dowhat
//                        if ((JsonUtil.jsonMsg(response)).contains("当前用户已过期")) {
//                            doLogin(DOPOST);
//                        } else {
//                             DoNet.this.doWhat(response, area_id);
//                        }
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        closeDialog();
                        if(null!= onAfterListener){
                            onAfterListener.doAfter();
                        }

                    }

                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id,int code) {
                        closeDialog();
                        closeMDialog();
                        if (onErrorListener != null) {
                            onErrorListener.onError(code);
                        }
                        if (needShowNetError) {
                            needShowNetError = false;
                            handler.sendEmptyMessageDelayed(Consts.TOAST, 5000);
//                            UIUtils.t("网络连接异常", false, UIUtils.T_ERROR);
                        }
                    }
                });
    }

    public void doDelete(Map<String,String> requestBody, final String url, final Context context, final Boolean needDialog) {
        this.mUrl = url;
        this.mContext = context;
        this.mNeedDialog = needDialog;


        setDialog(needDialog);


        //设置无标题
        OkHttpUtils
                .delete()
                .url(url)
                .requestBody(getRequestBody(requestBody))
                .addHeader("token", App.userInfo.getToken())
                .addHeader("user",String.valueOf(App.userInfo.getUser().getUser()))
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                    }

                    @Override
                    public void onResponse(String response, int id) {


                        netMission(response,id,DOPOST);

//                        requestException(response,mUrl);
//
//                        if (TextUtils.isEmpty(accessToken)) {
//                            DoNet.this.doWhat(response, area_id);
//                            return;
//                        }
//                        //如果response是空
//                        if (TextUtils.isEmpty(response)) {
////                            UIUtils.t("返回为空", false, UIUtils.T_ERROR);
//                            return;
//                        }
//
//                        //如果失败 并且 是 已过期，并且登陆一次再执行dowhat
//                        if ((JsonUtil.jsonMsg(response)).contains("当前用户已过期")) {
//                            doLogin(DOPOST);
//                        } else {
//                             DoNet.this.doWhat(response, area_id);
//                        }
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        closeDialog();
                        if(null!= onAfterListener){
                            onAfterListener.doAfter();
                        }

                    }

                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id,int code) {
                        closeDialog();
                        closeMDialog();
                        if (onErrorListener != null) {
                            onErrorListener.onError(code);
                        }
                        if (needShowNetError) {
                            needShowNetError = false;
                            handler.sendEmptyMessageDelayed(Consts.TOAST, 5000);
//                            UIUtils.t("网络连接异常", false, UIUtils.T_ERROR);
                        }
                    }
                });
    }


    private RequestBody getRequestBody(Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        Iterator<Map.Entry<String, String>> entries = params.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            builder.add(entry.getKey(), entry.getValue());
        }
        return builder.build();
    }





    public abstract void doWhat(String response, int id);



    public interface OnErrorListener {
        void onError(int code);
    }
    private OnErrorListener onErrorListener;

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

    public interface onAfterListener {
        void doAfter();
    }
    private DoNet.onAfterListener onAfterListener;
    public void setOnAfterListener(DoNet.onAfterListener onAfterListener) {
        this.onAfterListener = onAfterListener;
    }
}