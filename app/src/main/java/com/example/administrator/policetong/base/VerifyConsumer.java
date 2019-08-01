package com.example.administrator.policetong.base;

import android.util.Log;

import com.example.administrator.policetong.network.Network;
import com.example.administrator.policetong.utils.GsonUtil;

import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

/**
 * Created by Hjb on 2019/8/1.
 */

public abstract class VerifyConsumer implements Consumer<okhttp3.ResponseBody>{

    @Override
    public void accept(ResponseBody responseBody) throws Exception {
        String string = responseBody.string();
        if (!GsonUtil.verifyResult_show(string)) {
            return;
        }
        Network.closeDialog();
        result(GsonUtil.getResult(string));
    }


    public abstract void result(String result);
}
