package com.example.administrator.policetong.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.administrator.policetong.R;

/**
 * Created by Administrator on 2018/5/30/030.
 */

public class MorePopupWindow extends PopupWindow {

    public MorePopupWindow(final Context context, final PopCallback callback) {
        super(context);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View contentView = LayoutInflater.from(context).inflate(R.layout.popupwindow_layout,
                null, false);
        TextView textView=contentView.findViewById(R.id.pop_yijian);
        TextView changepwd=contentView.findViewById(R.id.pop_change_pwd);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                callback.onSuccess(0);
            }
        });
        changepwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                callback.onSuccess(1);
            }
        });

        setContentView(contentView);
    }

    public interface PopCallback {
        void onSuccess(int i);
    }
}
