package com.example.administrator.policetong.activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.policetong.MainActivity;
import com.example.administrator.policetong.R;
import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnPageChangeListener;

public class HelpActivity extends AppCompatActivity {
    private int p;
    SharedPreferences sharedPreferences;
    private TextView title;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        title = findViewById(R.id.help_title);
        sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        final int mypage=sharedPreferences.getInt("page",1);
        title.setText("用户操作手册"+mypage+"/30");
        PDFView pdfView = (PDFView) findViewById(R.id.pdfview); // 这个测试例子中，assets目录下sample.pdf // 缺省把该pdf定位到第一页。
        pdfView.fromAsset("help.pdf").defaultPage(mypage).onPageChange(new OnPageChangeListener() {
            @Override
            public void onPageChanged(int page, int pageCount) { // 当用户在翻页时候将回调。
                p = page;
                title.setText("用户操作手册"+p+"/30");
            }
        }).load();
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences.edit().putInt("page",p).apply();
    }
}
