package com.example.administrator.policetong.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.administrator.policetong.R;

public class ChangePwdActivity extends AppCompatActivity {

    private ImageView acArrowBack;
    private EditText etOldPwd;
    private EditText etNewPwd;
    private EditText etNewPwd2;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        initView();
    }

    private void initView() {
        acArrowBack = findViewById(R.id.ac_arrow_back);
        etOldPwd = findViewById(R.id.et_old_pwd);
        etNewPwd = findViewById(R.id.et_new_pwd);
        etNewPwd2 = findViewById(R.id.et_new_pwd2);
        btnSubmit = findViewById(R.id.btn_submit);


        acArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
