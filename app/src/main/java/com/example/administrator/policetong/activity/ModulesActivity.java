package com.example.administrator.policetong.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.policetong.R;
import com.example.administrator.policetong.fragment.DailyService;
import com.example.administrator.policetong.fragment.Email;
import com.example.administrator.policetong.fragment.Fragment_manage;
import com.example.administrator.policetong.fragment.PathParameter;
import com.example.administrator.policetong.fragment.PoliceConfirmed;
import com.example.administrator.policetong.fragment.SafetyChecks;
import com.example.administrator.policetong.fragment.StuddyFragment;
import com.example.administrator.policetong.fragment.VisitRectification;
import com.example.administrator.policetong.utils.LocationUtil;

public class ModulesActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView ac_arrow_back;
    private TextView ac_tv_title;
    public boolean tv_state=false;
    public double tv_latitude,tv_lontitude;
    LocationUtil locationUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                window.setNavigationBarColor(Color.TRANSPARENT);
            }
            setContentView(R.layout.activity_modules);
            locationUtil = new LocationUtil(new LocationUtil.LocationCall_back() {
                @Override
                public void getLocationData(boolean state, double latitude, double lontitude, String addr) {
                    tv_latitude=latitude;
                    tv_lontitude=lontitude;
                    double v = 4.9E-324;
                    if (tv_latitude!=v){
                        tv_state=true;
                        LocationUtil.mLocationClient.stop();
                    }
                    Log.e("getLocationData: ",tv_latitude+"    "+tv_lontitude+"     "+tv_state );
                }
            });
            locationUtil.startLocate(this);
            initView();
            Bundle bundle = getIntent().getBundleExtra("data");
            int id = bundle.getInt("id");
            switch (id) {
                case 1:
                    getSupportFragmentManager().beginTransaction().replace(R.id.ac_context, new Email()).commit();
                    ac_tv_title.setText("邮件收发");
                    break;
                case 2:
                    getSupportFragmentManager().beginTransaction().replace(R.id.ac_context, new PoliceConfirmed()).commit();
                    ac_tv_title.setText("警保台账");
                    break;
                case 3:
                    getSupportFragmentManager().beginTransaction().replace(R.id.ac_context, new SafetyChecks(),"SafetyChecks").commit();
                    ac_tv_title.setText("安全排查");
                    break;
                case 4:
                    getSupportFragmentManager().beginTransaction().replace(R.id.ac_context, new VisitRectification()).commit();
                    ac_tv_title.setText("走访整改");
                    break;
                case 5:
                    getSupportFragmentManager().beginTransaction().replace(R.id.ac_context, new DailyService()).commit();
                    ac_tv_title.setText("日常勤务");
                    break;
                case 6:
                    getSupportFragmentManager().beginTransaction().replace(R.id.ac_context, new PathParameter()).commit();
                    ac_tv_title.setText("道路台账");
                    break;
                case 7:
                    getSupportFragmentManager().beginTransaction().replace(R.id.ac_context, new Fragment_manage()).commit();
                    ac_tv_title.setText("管理/查看我的提交");
                    break;
                case 8:
                    getSupportFragmentManager().beginTransaction().replace(R.id.ac_context, new StuddyFragment()).commit();
                    ac_tv_title.setText("学习台账");
                    break;
            }
        }catch (Exception e){

        }
    }

    private void initView() {
        ac_arrow_back = (ImageView) findViewById(R.id.ac_arrow_back);
        ac_arrow_back.setOnClickListener(this);
        ac_tv_title = (TextView) findViewById(R.id.ac_tv_title);
    }

    @Override
    public void onClick(View view) {
        finish();
    }

    public void shilihua(Myjiekou myjiekou){
        this.myjiekou=myjiekou;
    }

    public interface Myjiekou{
        void callback(String ooo);
    }

    Myjiekou myjiekou;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
// TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("onActivityResult: ",resultCode+"    "+(resultCode == RESULT_OK) );
        if ( resultCode == RESULT_OK) {
            myjiekou.callback("拍照");
        }else {
            Toast.makeText(this, "已取消本次上传！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (LocationUtil.mLocationClient!=null){
            LocationUtil.mLocationClient.stop();
        }
    }
}
