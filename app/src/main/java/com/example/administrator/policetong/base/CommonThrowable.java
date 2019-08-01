package com.example.administrator.policetong.base;


import android.widget.Toast;

import com.example.administrator.policetong.network.Network;

import io.reactivex.functions.Consumer;

public abstract class CommonThrowable implements Consumer<Throwable> {



    @Override
    public void accept(Throwable throwable) throws Exception {
        Network.closeDialog();
        Toast.makeText(App.getApplication(), "登陆失败，请检查网络是否连接!", Toast.LENGTH_SHORT).show();
        onThrowable(throwable);
    }

    public abstract void onThrowable(Throwable throwable);
}
