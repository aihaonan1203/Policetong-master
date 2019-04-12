package com.example.administrator.policetong.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.httppost.getNetInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * 项目名称：
 * 类描述：
 * 创建人：aihaonan
 * 创建时间：2018/5/29 9:20
 * 修改人：Administrator
 * 修改时间：2018/5/29 9:20
 * 修改备注：
 */
public class Util {

    public static String urlHttp;
    public static String urlPort ;

    /**
     * 描述：保存数据到SharedPreferences对象中
     * @param ipUrl
     */

    public static void saveSetting(String ipUrl,String urlPort, Context context) {
        SharedPreferences spSettingSave = context.getSharedPreferences("setting", MODE_PRIVATE);// 将需要记录的数据保存在setting.xml文件中
        SharedPreferences.Editor editor = spSettingSave.edit();
        editor.putString("ipUrl", ipUrl);
        editor.putString("urlPort", urlPort);
        editor.commit();
    }

    /**
     * 描述：获取数据到SharedPreferences对象中
     * @return
     */
    public static UrlBean loadSetting(Context context) {
        UrlBean urlBean=new UrlBean();
        SharedPreferences loadSettingLoad = context.getSharedPreferences("setting", MODE_PRIVATE);
        //这里是将setting.xml 中的数据读出来
        String ipUrl=loadSettingLoad.getString("ipUrl", "null");
        String urlPort=loadSettingLoad.getString("urlPort", "null");
        urlBean.setUrl(ipUrl);
        urlBean.setUrlPort(urlPort);
        return urlBean;
    }

    public static void setRadioDateIntoDialog(final Context context, final EditText editText, Button button, final String[] items){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(context).setTitle("请选择")
                        .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editText.setText(items[which]);
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
            }
        });
    }


    public static void setMultiSelectDateIntoDialog(final Context context, final EditText editText, Button button, final String[] items){
        final boolean[] flags=new boolean[]{false,false,false,false,false,false,false,false};//初始复选情况
        AlertDialog.Builder builder=new android.app.AlertDialog.Builder(context);
        //设置对话框的标题
        builder.setTitle("复选框对话框");
        builder.setMultiChoiceItems(items, flags, new DialogInterface.OnMultiChoiceClickListener(){
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                flags[which]=isChecked;
                String result = "";
                for (int i = 0; i < flags.length; i++) {
                    if(flags[i]){
                        result=result+items[i]+"、";
                    }
                }
                editText.setText(result.substring(0, result.length()-1));
            }
        });
        //添加一个确定按钮
        builder.setPositiveButton(" 确 定 ", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        //创建一个复选框对话框
        final AlertDialog dialog=builder.create();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
    }


    public static void RequestOption(final Context context, String option, final RequestOptionCallBack callBack){
        Map map=new HashMap();
        map.put("typecode",option);
        final List<String> list=new ArrayList<>();
        getNetInfo.NetInfoArray(context, "selectoption", new JSONObject(map), new getNetInfo.VolleyArrayCallback() {
            @Override
            public void onSuccess(JSONArray object) throws JSONException {
                Log.e("onSuccess: ",object.toString() );
                if (object.length()!=0){
                    for (int i = 0; i < object.length(); i++) {
                        JSONObject jsonObject= (JSONObject) object.get(i);
                        list.add(jsonObject.getString("opcontent"));
                    }
                }
                callBack.CallBack(list);
            }

            @Override
            public void onError(VolleyError volleyError) {
                Toast.makeText(context, "网络出现问题，检查后重试", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface RequestOptionCallBack{
         void CallBack(List<String> list);
    }

}
