package com.example.administrator.policetong.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.administrator.policetong.R;
import com.example.administrator.policetong.base.BaseActivity;
import com.example.administrator.policetong.fragment.manage.PathParameter_manage;
import com.example.administrator.policetong.fragment.manage.SafetyChecks_manage;
import com.example.administrator.policetong.fragment.manage.VisitRectification_manage;

public class ManageActivity extends BaseActivity {

    private Toolbar tl_custom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        initView();
        setupToolBar(tl_custom,false);
        int type = getIntent().getIntExtra("type", -1);
        switch (type) {
            case 1:
                addFragment(new PathParameter_manage(), R.id.mFrameLayout);
                break;
            case 2:
                addFragment(new SafetyChecks_manage(), R.id.mFrameLayout);
                break;
            case 3:
                addFragment(new VisitRectification_manage(), R.id.mFrameLayout);
                break;
        }

    }

    private void initView() {
        tl_custom =  findViewById(R.id.tl_custom);
    }
}
