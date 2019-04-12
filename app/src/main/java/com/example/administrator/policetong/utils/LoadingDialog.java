package com.example.administrator.policetong.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.policetong.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LoadingDialog {


	public static Dialog dialog;
	public static Date data;
	public static SimpleDateFormat format;
	
	public static void showDialog(Context context){
		dialog=new Dialog(context, R.style.MyDialogStyle);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.loading_dialog);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.show();
	}


	public static void showDialog(Context context,String title){
		dialog=new Dialog(context, R.style.MyDialogStyle);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		View view=View.inflate(context,R.layout.loading_dialog,null);
		TextView textView=view.findViewById(R.id.dialog_title);
		dialog.setContentView(view);
		textView.setText(title);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.show();
	}
	public static void disDialog(){
		dialog.dismiss();
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String getTime(){
		data=new Date(System.currentTimeMillis());
		format=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		String string=format.format(data);
		return string;
	}

	@SuppressLint("SimpleDateFormat")
	public static String getTime2(){
		data=new Date(System.currentTimeMillis());
		format=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String string=format.format(data);
		return string;
	}

	@SuppressLint("SimpleDateFormat")
	public static String getTime3(){
		data=new Date(System.currentTimeMillis());
		format=new SimpleDateFormat("yyyy年MM月");
		String string=format.format(data);
		return string;
	}

	@SuppressLint("SimpleDateFormat")
	public static String getTime4(){
		data=new Date(System.currentTimeMillis());
		format=new SimpleDateFormat("yyyy年MM月dd日");
		String string=format.format(data);
		return string;
	}

	public static Dialog getDialog() {
		return dialog;
	}

	public static void setDialog(Dialog dialog) {
		LoadingDialog.dialog = dialog;
	}

	public static void showToast_shibai(Context context){
		Toast.makeText(context, "服务器访问失败，请检查网络", Toast.LENGTH_SHORT).show();
	}
}
