package com.example.administrator.policetong.activity;

import android.os.Bundle;

import com.example.administrator.policetong.R;
import com.example.administrator.policetong.base.BaseActivity;
import com.example.administrator.policetong.fragment.manage.PathParameter_manage;

public class ManageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        int type = getIntent().getIntExtra("type", -1);
        switch (type) {
            case 1:
                addFragment(new PathParameter_manage(),R.id.mFrameLayout);
                break;
        }

    }
}
