// (c)2016 Flipboard Inc, All Rights Reserved.

package com.example.administrator.policetong.network;


import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.example.administrator.policetong.R;
import com.example.administrator.policetong.base.App;
import com.example.administrator.policetong.base.Consts;
import com.example.administrator.policetong.network.api.PoliceApi;
import com.example.administrator.policetong.utils.UIUtils;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Network {
    private static PoliceApi gankApi;
    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJava2CallAdapterFactory.create();

    public static PoliceApi getPoliceApi(boolean needDialog) {
        if (gankApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(Consts.COMMOM_URL)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            gankApi = retrofit.create(PoliceApi.class);
        }
        return gankApi;
    }

    public static PoliceApi getPoliceApi(boolean needDialog, Context context) {
        setDialog(needDialog,context);
        if (gankApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(Consts.COMMOM_URL)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            gankApi = retrofit.create(PoliceApi.class);
        }
        return gankApi;
    }

    private static Dialog dialog;

    public static void setDialog(Boolean needDialog,Context context) {
        if (!needDialog) {
            return;
        }
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_loading);
        if (dialog.isShowing()) {
            return;
        }
        UIUtils.doDialog(context, dialog);
        dialog.setCancelable(false);
    }

    public static void closeDialog() {
        if (dialog == null) {
            return;
        }
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        UIUtils.closeDialog(App.getApplication(), dialog);
    }

}
