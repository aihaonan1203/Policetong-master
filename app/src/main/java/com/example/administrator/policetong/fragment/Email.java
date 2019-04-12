package com.example.administrator.policetong.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.administrator.policetong.R;
import com.example.administrator.policetong.em.LoginActivity;
import com.example.administrator.policetong.utils.Em;

/**
 * 邮件收发
 */
public class Email extends Fragment {


    public Email() {
        // Required empty public constructor
    }
//        Uri uri = Uri.parse("mailto:3802**92@qq.com");
//        String[] email = {"3802**92@qq.com"};
//        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
//        intent.putExtra(Intent.EXTRA_CC, email); // 抄送人
//        intent.putExtra(Intent.EXTRA_SUBJECT, "这是邮件的主题部分"); // 主题
//        intent.putExtra(Intent.EXTRA_TEXT, "这是邮件的正文部分"); // 正文
//        startActivity(Intent.createChooser(intent, "请选择邮件类应用"));

    ListView lv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_email, container, false);
        lv=view.findViewById(R.id.em_lv);

        return view;
    }

}
