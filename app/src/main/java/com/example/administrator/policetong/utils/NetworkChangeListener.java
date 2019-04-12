package com.example.administrator.policetong.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/8/9.
 */

public class NetworkChangeListener {
    public static boolean onAvailable=true;
    public static void network(final Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            // 请注意这里会有一个版本适配bug，所以请在这里添加非空判断
            if (connectivityManager != null) {
                connectivityManager.requestNetwork(new NetworkRequest.Builder().build(), new ConnectivityManager.NetworkCallback() {

                    /**
                     * 网络可用的回调
                     */
                    @Override
                    public void onAvailable(Network network) {
                        super.onAvailable(network);
                        if (!onAvailable){
                            Toast.makeText(context, "网络已恢复连接", Toast.LENGTH_SHORT).show();
                        }
                        onAvailable=true;
                    }

                    /**
                     * 网络丢失的回调
                     */
                    @Override
                    public void onLost(Network network) {
                        super.onLost(network);
                        onAvailable=false;
                        Toast.makeText(context, "网络已失去连接<“_”>", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
