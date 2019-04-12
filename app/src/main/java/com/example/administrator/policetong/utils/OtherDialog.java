package com.example.administrator.policetong.utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.policetong.R;
import com.example.administrator.policetong.bean.Notice_bean;

import java.util.List;

/**
 * Created by Administrator on 2018/7/25.
 */

public class OtherDialog {

    private static AlertDialog dialog;

    public static AlertDialog getDialog() {
        return dialog;
    }

    public static void showWaiterAuthorizationDialog(final Context context) {
        // LayoutInflater是用来找layout文件夹下的xml布局文件，并且实例化
        LayoutInflater factory = LayoutInflater.from(context);
        // 把布局文件中的控件定义在View中
        final View textEntryView = factory.inflate(R.layout.hppt_input_dialog, null);
        final EditText et_check_code = (EditText) textEntryView.findViewById(R.id.http_input);
        // 将自定义xml文件中的控件显示在对话框中
        dialog = new AlertDialog.Builder(context)
                .setTitle("服务器ip地址")
                .setView(textEntryView)
                .setPositiveButton("完成", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String checkCode = et_check_code.getText().toString().trim();
//                        Util.saveSetting(checkCode,context);
                    }
                })
                // 对话框的“退出”单击事件
                .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //不做操作，关闭对话框
                    }
                })
                // 设置dialog是否为模态，false表示模态，true表示非模态
                .setCancelable(false)
                // 对话框的创建、显示
                .create();
        dialog.show();
        et_check_code.setText(Util.loadSetting(context).getUrl());
    }





    @SuppressLint("SetTextI18n")
    public static void showDialog(Context context, int i, final List<Notice_bean> noticelist,boolean is) {
        final SharedPreferences sharedPreferences=context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        AlertDialog.Builder builder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
        View view = View.inflate(context, R.layout.notice_dialog, null);
        builder.setView(view);
        dialog = builder.create();
        final CheckBox checkBox = view.findViewById(R.id.notice_cb);
        Button btn = view.findViewById(R.id.notice_btn);
        ListView lv = view.findViewById(R.id.notice_lv);
        TextView title = view.findViewById(R.id.notice_title);

        lv.setAdapter(new MyAdapter(context,noticelist));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        if (!is){
            title.setText("您本月有" + i + "条公告信息");
            checkBox.setVisibility(View.GONE);
        }else {
            title.setText("您今日有" + i + "条公告信息");
        }
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                String date = "";
                if (checkBox.isChecked()){
                    for (Notice_bean bean:noticelist){
                        if (date.length()==0){
                            date=bean.getDate();
                        }else {
                            date=date+"|"+bean.getDate();
                        }
                    }
                    if (sharedPreferences.getString("notice","null").equals("null")){
                        sharedPreferences.edit().putString("notice",date).apply();
                    }else {
                        String time=sharedPreferences.getString("notice","null");
                        String str1 =time .substring(0, date.indexOf("日")+1);
                        String str2 =date .substring(0, date.indexOf("日")+1);
                        if (str1.equals(str2)){
                            sharedPreferences.edit().putString("notice",time+"|"+date).apply();
                        }else {
                            sharedPreferences.edit().putString("notice",date).apply();
                        }
                    }

                }
            }
        });
        dialog.show();
    }

    public static void disDialog() {
        dialog.dismiss();
    }

    private static class MyAdapter extends BaseAdapter {
        Context context;
        List<Notice_bean> noticelist;

        public MyAdapter(Context context, List<Notice_bean> noticelist) {
            this.context = context;
            this.noticelist = noticelist;
        }

        @Override
        public int getCount() {
            return noticelist.size();
        }

        @Override
        public Object getItem(int i) {
            return noticelist.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.notice_lv_item, null);
                view.setTag(new ViewHolder(view));
            }
            set_data_into_item(noticelist.get(i),(ViewHolder)view.getTag());
            return view;
        }

        private void set_data_into_item(Notice_bean bean, ViewHolder holder) {
            holder.notice_item_txt.setText("\r\r\r\r"+bean.getMsg());
            holder.notice_item_time.setText(bean.getDate());
        }

        private   class ViewHolder {
            public View rootView;
            public TextView notice_item_txt;
            public TextView notice_item_time;

            public ViewHolder(View rootView) {
                this.rootView = rootView;
                this.notice_item_txt = (TextView) rootView.findViewById(R.id.notice_item_txt);
                this.notice_item_time = (TextView) rootView.findViewById(R.id.notice_item_time);
            }

        }
    }

}
