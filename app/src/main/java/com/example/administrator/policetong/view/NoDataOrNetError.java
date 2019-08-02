package com.example.administrator.policetong.view;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.policetong.R;

/**
 * Created by Administrator on 2018/1/17 0017.
 */

public class NoDataOrNetError {


    public static View noData(View recyclerView, Context activity, String content) {
        View view;
        if (recyclerView == null) {
            view = View.inflate(activity, R.layout.recyclerview_emptyview, null);
        } else {
            view = ((Activity) activity).getLayoutInflater().inflate(R.layout.recyclerview_emptyview, (ViewGroup) recyclerView.getParent(), false);
        }
        TextView tv = view.findViewById(R.id.emptyview_content);
        if (!TextUtils.isEmpty(content)) {
            tv.setText(content);
        }
        return view;
    }

    public static View netError(View recyclerView, Context activity) {
        View view;
        if (recyclerView == null) {
            view = View.inflate(activity, R.layout.recyclerview_neterrorview, null);
        } else {
            view = ((Activity) activity).getLayoutInflater().inflate(R.layout.recyclerview_neterrorview, (ViewGroup) recyclerView.getParent(), false);
        }


        return view;
    }


//
//    private static OnClickListener mOnClickListener;
//    interface OnClickListener{
//        void onClick();
//    }
//    public static void setOnClickListener(OnClickListener onClickListener){
//        mOnClickListener = onClickListener;
//    }


}
