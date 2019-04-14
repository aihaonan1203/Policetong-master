package com.example.administrator.policetong.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.policetong.MainActivity;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.base.BaseActivity;
import com.example.administrator.policetong.bean.EvenMsg;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.List;

public class PreviewActivity extends BaseActivity {

    private ViewPager vierpagerAd;
    private RadioGroup rgPoint;
    private List<LocalMedia> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        initView();
    }

    private void initView() {
        vierpagerAd = findViewById(R.id.vierpager_ad);
        rgPoint = findViewById(R.id.rg_point);

        if (rgPoint != null) {
            rgPoint.removeAllViews();
        }
        data = evenMsg.getData();
        for (int i = 0; i < data.size(); i++) {
            RadioButton rbtn = new RadioButton(this);
            rbtn.setButtonDrawable(null);
            rbtn.setBackgroundResource(R.drawable.radiobutton_ischecked);
            rbtn.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            rbtn.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
            rgPoint.addView(rbtn);
        }
        RadioButton rbtn = (RadioButton) rgPoint.getChildAt(0);
        rbtn.setChecked(true);
        vierpagerAd.setAdapter(new MyViewPagerAdapter());
        vierpagerAd.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                RadioButton rbtn = (RadioButton) rgPoint.getChildAt(position);
                rbtn.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


    private EvenMsg<List<LocalMedia>> evenMsg;
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true) //在主线程执行
    public void onDataSynEvent(EvenMsg<List<LocalMedia>> evenMsg) {
        this.evenMsg=evenMsg;
    }



    private class MyViewPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView=new ImageView(PreviewActivity.this);
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(params);
            Glide.with(PreviewActivity.this).load(new File(data.get(position).getPath())).into(imageView);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
