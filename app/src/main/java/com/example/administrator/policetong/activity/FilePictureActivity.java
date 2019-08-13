package com.example.administrator.policetong.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.base.BaseActivity;
import com.example.administrator.policetong.view.PhotoViewPager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;


public class FilePictureActivity extends BaseActivity {


    PhotoViewPager mViewPager;
    private TextView title_name;
    private Toolbar tl_custom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        initView();
        initToolbar();
    }


    private List<String> urls;

    private void initToolbar() {
        setupToolBar(tl_custom, false);
        title_name.setText("照片详情");
    }

    protected void initView() {
//        1 获取图片地址的List 和当前图片的地址
        mViewPager = findViewById(R.id.picture_viewpager);

        List<String> images = getIntent().getStringArrayListExtra("images");

        String currentImage = (String) getIntent().getSerializableExtra("image");

        int position = getIntent().getIntExtra("position", -1);

        if (images.size() > 0) {
            urls = images;
        } else {
            urls = new ArrayList<>();
            urls.add(currentImage);
        }
        int currentItem = 0;
        //如果没有获取
        if (position == -1) {
            //次函数貌似没有用 不能比较出两个字符串是否相同
            for (int i = 0; i < urls.size(); i++) {
                if ((urls.get(i)).equals(currentImage)) {
                    currentItem = i;
                }
            }
        } else {
            currentItem = position;
        }
        SamplePagerAdapter adapter = new SamplePagerAdapter();
        //设置网址
        adapter.setUrls(urls);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(currentItem);
        title_name =  findViewById(R.id.title_name);
        tl_custom =  findViewById(R.id.tl_custom);
    }


    static class SamplePagerAdapter extends PagerAdapter {

        public void setUrls(List urls) {
            this.urls = urls;
        }

        private List<String> urls;

        @Override
        public int getCount() {
            return urls.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, final int position) {


            final PhotoView photoView = new PhotoView(container.getContext());
            Glide.with(container.getContext())
                    .load(new File(urls.get(position)))
                    .into(photoView);
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return photoView;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

}
