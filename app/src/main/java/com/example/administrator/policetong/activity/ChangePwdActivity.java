package com.example.administrator.policetong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.base.App;
import com.example.administrator.policetong.base.BaseActivity;
import com.example.administrator.policetong.base.BaseBean;
import com.example.administrator.policetong.network.Network;
import com.example.administrator.policetong.utils.GsonUtil;
import com.example.administrator.policetong.utils.SPUtils;
import com.example.administrator.policetong.utils.UIUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class ChangePwdActivity extends BaseActivity {

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
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldpwd=etOldPwd.getText().toString().trim();
                String newpwd=etNewPwd.getText().toString().trim();
                String newpwd2=etNewPwd2.getText().toString().trim();
                if (oldpwd.equals(newpwd2)){
                    Toast.makeText(ChangePwdActivity.this, "旧密码不能与新密码一样", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!newpwd.equals(newpwd2)){
                    Toast.makeText(ChangePwdActivity.this, "两次输入的密码不一样", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (newpwd.length()<=5){
                    Toast.makeText(ChangePwdActivity.this, "密码长度最少为六位！", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String,String> map=new HashMap<>();
                map.put("oldpsd",oldpwd);
                map.put("newpsd",newpwd2);
                String string = new JSONObject(map).toString();
                disposable= Network.getPoliceApi(true).changePwd(App.userInfo.getToken(),App.userInfo.getUser().getUser(),RequestBody.create(MediaType.parse("application/json"),string))
                        .compose(BaseActivity.<ResponseBody>applySchedulers())
                        .subscribe(new Consumer<ResponseBody>() {
                            @Override
                            public void accept(ResponseBody bean) throws Exception {
                                String repsonse = bean.string();
                                if (!GsonUtil.verifyResult_show(repsonse)) {
                                    return;
                                }
                                UIUtils.t(String.valueOf(JSON.parseObject(repsonse).getString("message")),false,UIUtils.T_SUCCESS);
                                SPUtils.saveBoolean("isLogin", false);
                                finish();
                                startActivity(new Intent(ChangePwdActivity.this,LoginActivity.class));
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e("accept: ","" );
                                UIUtils.t("修改失败，请稍后再试！",false,UIUtils.T_SUCCESS);
                            }
                        });
            }
        });
        acArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
