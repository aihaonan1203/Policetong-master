// (c)2016 Flipboard Inc, All Rights Reserved.

package com.example.administrator.policetong.network;


import com.example.administrator.policetong.base.Consts;
import com.example.administrator.policetong.network.api.PoliceApi;
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

    public static PoliceApi getPoliceApi() {
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

}
